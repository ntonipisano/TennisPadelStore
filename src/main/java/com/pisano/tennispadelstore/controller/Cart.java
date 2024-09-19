package com.pisano.tennispadelstore.controller;

import com.pisano.tennispadelstore.model.dao.*;
import com.pisano.tennispadelstore.model.mo.User;
import com.pisano.tennispadelstore.model.mo.Product;
import com.pisano.tennispadelstore.model.mo.Order;
import com.pisano.tennispadelstore.services.config.Configuration;
import com.pisano.tennispadelstore.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Cart {
    private Cart() {
    }

    public static void addtoCart(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory cartDAOFactory = null;
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


        try {
            Map cartFactoryParameters = new HashMap<String, Object>();
            cartFactoryParameters.put("request", request);
            cartFactoryParameters.put("response", response);
            cartDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, cartFactoryParameters);
            cartDAOFactory.beginTransaction();

            CartDAO cartDAO = cartDAOFactory.getCartDAO();

            String productIdStr = request.getParameter("productid");
            int quantity = 1;

            try {
                Long productId = Long.parseLong(productIdStr);
                cartDAO.addProductToCart(productId, quantity);
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING, "Invalid product ID format", e);
                request.setAttribute("errorMessage", "Invalid product ID format");
            }

            cartDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Rollback failed", t);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Close transaction failed", t);
            }
        }

    }

}
