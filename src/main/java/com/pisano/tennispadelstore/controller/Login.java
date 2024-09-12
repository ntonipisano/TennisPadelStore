package com.pisano.tennispadelstore.controller;

import com.pisano.tennispadelstore.model.dao.AdminkeyDAO;
import com.pisano.tennispadelstore.model.dao.DAOFactory;
import com.pisano.tennispadelstore.model.dao.UserDAO;
import com.pisano.tennispadelstore.model.mo.User;
import com.pisano.tennispadelstore.services.config.Configuration;
import com.pisano.tennispadelstore.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login {
    private Login () {
    }
    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "login/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void handleLogin(HttpServletRequest request, HttpServletResponse response) {
        String loginType = request.getParameter("loginType");

        if ("admin".equals(loginType)) {
            adminLogin(request, response);
        } else {
            login(request, response);
        }
    }

    public static void login(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUsername(username);

            if (user == null || !user.getPassword().equals(password)) {
                applicationMessage = "Username o password errati!";
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("loggedUser", user);
                loggedUser = user;
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", loggedUser != null ? "homeManagement/view" : "login/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void adminLogin(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser = null;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            // Creazione della factory per la sessione
            Map<String, Object> sessionFactoryParameters = new HashMap<>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            // Creazione della factory per il DAO
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            // Recupero dei parametri dalla richiesta
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String adminkey = request.getParameter("adminkey");

            // Verifica della chiave amministrativa
            AdminkeyDAO adminkeyDAO = daoFactory.getAdminkeyDAO();
            boolean isValidKey = adminkeyDAO.Checkkey() != null && adminkeyDAO.Checkkey().equals(adminkey);

            if (isValidKey) {
                // Verifica delle credenziali dell'amministratore
                UserDAO userDAO = daoFactory.getUserDAO();
                User admin = userDAO.findByUsername(username);

                if (admin != null && admin.getPassword().equals(password) && admin.isAdmin()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("loggedUser", admin);
                    loggedUser = admin;
                } else {
                    applicationMessage = "Username, password o chiave amministratore non validi.";
                }

                daoFactory.commitTransaction();
            } else {
                applicationMessage = "Chiave amministratore non valida.";
            }

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", loggedUser != null ? "management/view" : "login/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            /*HttpSession session = request.getSession();
            session.invalidate();*/
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            sessionUserDAO.delete(null);

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }
}
