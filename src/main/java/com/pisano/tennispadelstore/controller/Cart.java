package com.pisano.tennispadelstore.controller;

import com.pisano.tennispadelstore.model.dao.*;
import com.pisano.tennispadelstore.model.mo.User;
import com.pisano.tennispadelstore.model.mo.Product;
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

    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        User loggedUser;
        DAOFactory cartDAOFactory = null;
        DAOFactory productDAOFactory = null;
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

            Map cartFactoryParameters = new HashMap<String, Object>();
            cartFactoryParameters.put("request", request);
            cartFactoryParameters.put("response", response);
            cartDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, cartFactoryParameters);
            cartDAOFactory.beginTransaction();

            CartDAO cartDAO = cartDAOFactory.getCartDAO();
            Map<Long, Integer> cartItems = cartDAO.getCartItems();

            cartDAOFactory.commitTransaction();

            //Recupero dal database le informazioni sui prodotti nel carrello
            Map productFactoryParameters = new HashMap<String, Object>();
            productFactoryParameters.put("request", request);
            productFactoryParameters.put("response", response);
            productDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, productFactoryParameters);
            productDAOFactory.beginTransaction();

            Map<Product, Integer> productsAndQuantity = new HashMap<>();
            if (!cartItems.isEmpty()) {
                ProductDAO productDAO = productDAOFactory.getProductDAO();
                for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
                    Long productId = entry.getKey();
                    Integer quantity = entry.getValue();

                    Product product = productDAO.findByProductId(productId);
                    if (product != null) {
                        productsAndQuantity.put(product, quantity);
                    }
                }
            }

            productDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("productsAndQuantity",productsAndQuantity);
            request.setAttribute("viewUrl", "cart/view");

            }
         catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
                if (cartDAOFactory != null) cartDAOFactory.rollbackTransaction();
                if (productDAOFactory != null) productDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
                if (cartDAOFactory != null) cartDAOFactory.closeTransaction();
                if (productDAOFactory != null) productDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
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
            
            String productIdStr = request.getParameter("productId");
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
            Shop.view(request,response);
            request.setAttribute("viewUrl", "shop/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (cartDAOFactory != null) cartDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Rollback failed", t);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (cartDAOFactory != null) cartDAOFactory.closeTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Close transaction failed", t);
            }
        }
    }

    public static void decreaseQuantity (HttpServletRequest request, HttpServletResponse response) {
        DAOFactory cartDAOFactory = null;
        DAOFactory sessionDAOFactory = null;
        DAOFactory productDAOFactory = null;
        User loggedUser;
        Logger logger = LogService.getApplicationLogger();
        String productIdStr = request.getParameter("productId");

        if (productIdStr == null || productIdStr.isEmpty()) {
            logger.log(Level.WARNING, "Product ID is missing");
            request.setAttribute("applicationMessage", "Product ID is required.");
            view(request, response); // Questo leggerà ancora il vecchio cookie
            return;
        }
        Long productId;
        try {
            productId = Long.parseLong(productIdStr);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid product ID format", e);
            request.setAttribute("applicationMessage", "Invalid product ID format.");
            view(request, response); // Questo leggerà ancora il vecchio cookie
            return;
        }

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            sessionDAOFactory.commitTransaction();

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


        Map<Long, Integer> updatedCartItems;
        try {
            Map cartFactoryParameters = new HashMap<String, Object>();
            cartFactoryParameters.put("request", request);
            cartFactoryParameters.put("response", response);
            cartDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, cartFactoryParameters);
            cartDAOFactory.beginTransaction();

            CartDAO cartDAO = cartDAOFactory.getCartDAO();
            updatedCartItems = cartDAO.decreaseProductQuantity(productId, request, response);

            cartDAOFactory.commitTransaction();

        }catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (cartDAOFactory != null) cartDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Rollback failed", t);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (cartDAOFactory != null) cartDAOFactory.closeTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Close transaction failed", t);
            }
        }


        Map<Product, Integer> productsAndQuantity = new HashMap<>();
        try {
            Map productFactoryParameters = new HashMap<String, Object>();
            productFactoryParameters.put("request", request);
            productFactoryParameters.put("response", response);
            productDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, productFactoryParameters);
            productDAOFactory.beginTransaction();

            //Map<Long, Integer> cartItems = cartDAO.getCartItems();
            ProductDAO productDAO = productDAOFactory.getProductDAO();

            for (Map.Entry<Long, Integer> entry : updatedCartItems.entrySet()) {
                Long productId2 = entry.getKey();
                Integer quantity = entry.getValue();
                Product product = productDAO.findByProductId(productId2);
                if (product != null) {
                    productsAndQuantity.put(product, quantity);
                }
            }

            productDAOFactory.commitTransaction();

        }catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (productDAOFactory != null) productDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Rollback failed", t);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (productDAOFactory != null) productDAOFactory.closeTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Close transaction failed", t);
            }
        }

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("productsAndQuantity", productsAndQuantity);
            request.setAttribute("viewUrl", "cart/view");
            //Cart.view(request, response);
    }



    public static void increaseQuantity(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory cartDAOFactory = null;
        DAOFactory sessionDAOFactory = null;
        DAOFactory productDAOFactory = null;
        User loggedUser;
        Logger logger = LogService.getApplicationLogger();
        String productIdStr = request.getParameter("productId");

        // Validazione del parametro productId
        if (productIdStr == null || productIdStr.isEmpty()) {
            logger.log(Level.WARNING, "Product ID is missing");
            request.setAttribute("applicationMessage", "Product ID is required.");
            view(request, response);
            return;
        }

        Long productId;
        try {
            productId = Long.parseLong(productIdStr);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid product ID format", e);
            request.setAttribute("applicationMessage", "Invalid product ID format.");
            view(request, response);
            return;
        }

        try {
            // Recupero utente loggato
            Map<String, Object> sessionFactoryParameters = new HashMap<>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            sessionDAOFactory.commitTransaction();
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

        Map<Long, Integer> updatedCartItems;
        try {
            // Aggiornamento del carrello
            Map<String, Object> cartFactoryParameters = new HashMap<>();
            cartFactoryParameters.put("request", request);
            cartFactoryParameters.put("response", response);
            cartDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, cartFactoryParameters);
            cartDAOFactory.beginTransaction();

            CartDAO cartDAO = cartDAOFactory.getCartDAO();
            updatedCartItems = cartDAO.increaseProductQuantity(productId, request, response);

            cartDAOFactory.commitTransaction();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (cartDAOFactory != null) cartDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Rollback failed", t);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (cartDAOFactory != null) cartDAOFactory.closeTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Close transaction failed", t);
            }
        }

        Map<Product, Integer> productsAndQuantity = new HashMap<>();
        try {
            // Recupero delle informazioni sui prodotti dal database
            Map<String, Object> productFactoryParameters = new HashMap<>();
            productFactoryParameters.put("request", request);
            productFactoryParameters.put("response", response);
            productDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, productFactoryParameters);
            productDAOFactory.beginTransaction();

            ProductDAO productDAO = productDAOFactory.getProductDAO();
            for (Map.Entry<Long, Integer> entry : updatedCartItems.entrySet()) {
                Long pId = entry.getKey();
                Integer quantity = entry.getValue();
                Product product = productDAO.findByProductId(pId);
                if (product != null) {
                    productsAndQuantity.put(product, quantity);
                }
            }
            productDAOFactory.commitTransaction();
            logger.log(Level.INFO, "Products and quantities after increase: " + productsAndQuantity);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (productDAOFactory != null) productDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Rollback failed", t);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (productDAOFactory != null) productDAOFactory.closeTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Close transaction failed", t);
            }
        }

        logger.log(Level.INFO, "Updated cart items: " + updatedCartItems);
        logger.log(Level.INFO, "Products and quantities after increase: " + productsAndQuantity);

        request.setAttribute("loggedOn", loggedUser != null);
        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("productsAndQuantity", productsAndQuantity);
        request.setAttribute("viewUrl", "cart/view");
    }

}
