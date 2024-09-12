package com.pisano.tennispadelstore.controller;

import com.pisano.tennispadelstore.model.dao.DAOFactory;
import com.pisano.tennispadelstore.model.dao.ProductDAO;
import com.pisano.tennispadelstore.model.dao.UserDAO;
import com.pisano.tennispadelstore.model.mo.Product;
import com.pisano.tennispadelstore.model.mo.User;
import com.pisano.tennispadelstore.services.config.Configuration;
import com.pisano.tennispadelstore.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Shop {
    private Shop () {
    }
    public static void view(HttpServletRequest request, HttpServletResponse response) {
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
            Map<String, Object> productFactoryParameters = new HashMap<>();
            productFactoryParameters.put("request", request);
            productFactoryParameters.put("response", response);
            productDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, productFactoryParameters);
            productDAOFactory.beginTransaction();

            ProductDAO productDAO = productDAOFactory.getProductDAO();
            List<Product> allProducts = productDAO.findAllProducts();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "shop/view");
            request.setAttribute("allProducts", allProducts);

            sessionDAOFactory.commitTransaction();
            productDAOFactory.commitTransaction();

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
