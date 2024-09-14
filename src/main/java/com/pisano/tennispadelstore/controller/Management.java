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
}



