package com.pisano.tennispadelstore.controller;

import com.pisano.tennispadelstore.model.dao.DAOFactory;
import com.pisano.tennispadelstore.model.dao.ProductDAO;
import com.pisano.tennispadelstore.model.dao.UserDAO;
import com.pisano.tennispadelstore.model.dao.OrderDAO;
import com.pisano.tennispadelstore.model.mo.Product;
import com.pisano.tennispadelstore.model.mo.User;
import com.pisano.tennispadelstore.model.mo.Order;

import com.pisano.tennispadelstore.services.logservice.LogService;
import com.pisano.tennispadelstore.services.config.Configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.List;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Management {
    private Management (){
    }

    public static void view(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        DAOFactory productDAOFactory = null;
        DAOFactory orderDAOFactory = null;
        User loggedUser;
        Logger logger = LogService.getApplicationLogger();

        try {

            /*Factory per User che usa i cookie*/
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            /*Factory per Product che va nel db */
            Map productFactoryParameters = new HashMap<String, Object>();
            productFactoryParameters.put("request", request);
            productFactoryParameters.put("response", response);
            productDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, productFactoryParameters);
            productDAOFactory.beginTransaction();

            ProductDAO productDAO = productDAOFactory.getProductDAO();
            List<Product> allProducts = productDAO.findAllProducts();

            /*Factory per Order che va nel db*/
            Map orderFactoryParameters = new HashMap<String, Object>();
            orderFactoryParameters.put("request", request);
            orderFactoryParameters.put("response", response);
            orderDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, orderFactoryParameters);
            orderDAOFactory.beginTransaction();

            OrderDAO orderDAO = orderDAOFactory.getOrderDAO();
            List <Order> allOrders = orderDAO.findAllOrders();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("allProducts", allProducts);
            request.setAttribute("allOrders",allOrders);
            request.setAttribute("viewUrl", "management/view");

            sessionDAOFactory.commitTransaction();
            productDAOFactory.commitTransaction();
            orderDAOFactory.commitTransaction();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
                if (productDAOFactory != null) productDAOFactory.rollbackTransaction();
                if (orderDAOFactory != null) orderDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Rollback Error", t);
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
                if (productDAOFactory != null) productDAOFactory.closeTransaction();
                if (orderDAOFactory != null) orderDAOFactory.closeTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Close Transaction Error", t);
            }
        }
    }

    public static void usermanag(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        DAOFactory userDAOFactory = null;
        User loggedUser;
        Logger logger = LogService.getApplicationLogger();

        try {
            /* Factory user*/
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            /*Factory user che va nel db*/
            Map userFactoryParameters = new HashMap<String, Object>();
            userFactoryParameters.put("request", request);
            userFactoryParameters.put("response", response);
            userDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, userFactoryParameters);
            userDAOFactory.beginTransaction();

            UserDAO userUserDAO = userDAOFactory.getUserDAO();
            List<User> allUsers = userUserDAO.findAllUsers();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "management/usermanag");
            request.setAttribute("allUsers", allUsers);

            sessionDAOFactory.commitTransaction();
            userDAOFactory.commitTransaction();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
                if (userDAOFactory != null) userDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Rollback Error", t);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
                if (userDAOFactory != null) userDAOFactory.closeTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Close Transaction Error", t);
            }
        }
        }

    public static void productmanag(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        DAOFactory productDAOFactory = null;
        User loggedUser;
        Logger logger = LogService.getApplicationLogger();

        try {
            /*Factory user*/
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            /*Factory Product che va nel db */
            Map productFactoryParameters = new HashMap<String, Object>();
            productFactoryParameters.put("request", request);
            productFactoryParameters.put("response", response);
            productDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, productFactoryParameters);
            productDAOFactory.beginTransaction();

            ProductDAO productDAO = productDAOFactory.getProductDAO();
            List<Product> allProducts = productDAO.findAllProducts();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "management/productmanag");
            request.setAttribute("allProducts", allProducts);

            sessionDAOFactory.commitTransaction();
            productDAOFactory.commitTransaction();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
                if (productDAOFactory != null) productDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
                if (productDAOFactory != null) productDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void ordermanag(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        DAOFactory orderDAOFactory = null;
        User loggedUser;
        Logger logger = LogService.getApplicationLogger();

        try {
            /*Factory user*/
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            /*Factory Order che va nel db */
            Map orderFactoryParameters = new HashMap<String, Object>();
            orderFactoryParameters.put("request", request);
            orderFactoryParameters.put("response", response);
            orderDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, orderFactoryParameters);
            orderDAOFactory.beginTransaction();

            OrderDAO orderDAO = orderDAOFactory.getOrderDAO();
            List<Order> allOrders = orderDAO.findAllOrders();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "management/ordermanag");
            request.setAttribute("allOrders", allOrders);

            sessionDAOFactory.commitTransaction();
            orderDAOFactory.commitTransaction();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
                if (orderDAOFactory != null) orderDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
                if (orderDAOFactory != null) orderDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    //Elimina utente
    public static void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory userDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();

        try {
        Map userFactoryParameters = new HashMap<String, Object>();
        userFactoryParameters.put("request", request);
        userFactoryParameters.put("response", response);
        userDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, userFactoryParameters);
        userDAOFactory.beginTransaction();

            Long userId = Long.parseLong(request.getParameter("userId"));
            UserDAO userDAO = userDAOFactory.getUserDAO();
            User user = userDAO.findByUserId(userId); // Trova l'utente da eliminare

            if (user != null) {
                userDAO.delete(user);
            }
            userDAOFactory.commitTransaction();

            Management.usermanag(request, response);
            request.setAttribute("viewUrl","management/usermanag");

            /*Includo nella risposta uno script per ricaricare la pagina e quindi vedere subito l'aggiornamento*/
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<script type='text/javascript'>");
            out.println("window.location.href = 'Dispatcher?controllerAction=Management.usermanag';");
            out.println("</script>");
            out.println("</body></html>");

            // Inoltra alla pagina di successo o mostra un messaggio appropriato
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (userDAOFactory != null) userDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Rollback Error", t);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (userDAOFactory != null) userDAOFactory.closeTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Close Transaction Error", t);
            }
        }
    }

    // Promuovi ad amministratore
    public static void makeAdmin(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory userDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();

        try {
        Map userFactoryParameters = new HashMap<String, Object>();
        userFactoryParameters.put("request", request);
        userFactoryParameters.put("response", response);
        userDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, userFactoryParameters);
        userDAOFactory.beginTransaction();

            Long userId = Long.parseLong(request.getParameter("userId"));
            UserDAO userDAO = userDAOFactory.getUserDAO();

            if (userId != null) {
                userDAO.makeAdmin(userId);
            }
            userDAOFactory.commitTransaction();

            Management.usermanag(request, response);
            request.setAttribute("viewUrl","management/usermanag");

            /*Includo nella risposta uno script per ricaricare la pagina e quindi vedere subito l'aggiornamento*/
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<script type='text/javascript'>");
            out.println("window.location.href = 'Dispatcher?controllerAction=Management.usermanag';");
            out.println("</script>");
            out.println("</body></html>");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (userDAOFactory != null) userDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Rollback Error", t);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (userDAOFactory != null) userDAOFactory.closeTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Close Transaction Error", t);
            }
        }
    }

    // Rimuovi amministratore
    public static void removeAdmin(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory userDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map userFactoryParameters = new HashMap<String, Object>();
            userFactoryParameters.put("request", request);
            userFactoryParameters.put("response", response);
            userDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, userFactoryParameters);
            userDAOFactory.beginTransaction();

            Long userId = Long.parseLong(request.getParameter("userId"));
            UserDAO userDAO = userDAOFactory.getUserDAO();

            if (userId != null) {
                userDAO.removeAdmin(userId);
            }
            userDAOFactory.commitTransaction();

            Management.usermanag(request, response);
            request.setAttribute("viewUrl","management/usermanag");
            /*Includo nella risposta uno script per ricaricare la pagina e quindi vedere subito l'aggiornamento*/
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<script type='text/javascript'>");
            out.println("window.location.href = 'Dispatcher?controllerAction=Management.usermanag';");
            out.println("</script>");
            out.println("</body></html>");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (userDAOFactory != null) userDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Rollback Error", t);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (userDAOFactory != null) userDAOFactory.closeTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Close Transaction Error", t);
            }
        }
    }
    }