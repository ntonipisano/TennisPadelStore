package com.pisano.tennispadelstore.controller;

import com.pisano.tennispadelstore.model.dao.*;
import com.pisano.tennispadelstore.model.mo.User;
import com.pisano.tennispadelstore.model.mo.Product;
import com.pisano.tennispadelstore.model.mo.Order;
import com.pisano.tennispadelstore.services.config.Configuration;
import com.pisano.tennispadelstore.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderController {
    private OrderController() {
    }

    public static void createOrder(HttpServletRequest request, HttpServletResponse response) {

        Logger logger = LogService.getApplicationLogger();
        DAOFactory sessionDAOFactory = null;
        User loggedUser;
        DAOFactory cartDAOFactory = null;
        DAOFactory productDAOFactory = null;
        DAOFactory orderDAOFactory = null;
        boolean allAvailable = true;
        List<Product> unavailableProducts = new ArrayList<>();
        String costo = request.getParameter("costototale");
        String indirizzo = request.getParameter("indirizzo");
        String metododipagamento = request.getParameter("metodoPagamento");
        String cap = request.getParameter("cap");
        String cellulare = request.getParameter("cell");

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

            Map productFactoryParameters = new HashMap<String, Object>();
            productFactoryParameters.put("request", request);
            productFactoryParameters.put("response", response);
            productDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, productFactoryParameters);
            productDAOFactory.beginTransaction();

            ProductDAO productDAO = productDAOFactory.getProductDAO();

            Map<Product, Integer> productsAndQuantity = new HashMap<>();
            if (!cartItems.isEmpty()) {

                for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
                    Long productId = entry.getKey();
                    Integer quantity = entry.getValue();

                    Product product = productDAO.findByProductId(productId);
                    if (product != null) {
                        productsAndQuantity.put(product, quantity);
                    }
                }
            }



            // Controlla disponibilità dei prodotti
            for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
                Long productId = entry.getKey();
                Integer quantity = entry.getValue();

                Product product = productDAO.findByProductId(productId); // recupera il prodotto
                String dispString = product.getDisponibilita();
                Integer disponibilita = Integer.parseInt(dispString);
                if (product == null || disponibilita < quantity) {
                    allAvailable = false;
                    unavailableProducts.add(product);
                }
            }

            if (allAvailable) {
                // Crea l'ordine

                Map orderFactoryParameters = new HashMap<String, Object>();
                orderFactoryParameters.put("request", request);
                orderFactoryParameters.put("response", response);
                orderDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, orderFactoryParameters);
                orderDAOFactory.beginTransaction();

                OrderDAO orderDAO = orderDAOFactory.getOrderDAO();

                //Ricavo la data corrente dalla classe localdate per passarla alla creazione dell'ordine
                LocalDate dataCorrente = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String dataFormattata = dataCorrente.format(formatter);
                //Ricavo lo userid dal loggeduser da passare all'ordine
                Long userid = loggedUser.getUserId();

                //Creo l'ordine
                Order order = orderDAO.create(userid, costo, "Preso in carico", indirizzo, dataFormattata, metododipagamento, cap, cellulare);

                //Aggiorno la disponibilità dei prodotti nel database
                for (Map.Entry<Product, Integer> entry : productsAndQuantity.entrySet()) {
                    Product product = entry.getKey();
                    Integer quantity = entry.getValue();
                    Integer currentDisponibilita = Integer.parseInt(product.getDisponibilita());
                    Integer nuovaDisponibilita = currentDisponibilita - quantity;

                    // Aggiorna la disponibilità nel database
                    productDAO.modDispbyProductid(product.getProductid(), String.valueOf(nuovaDisponibilita));
                }

                productDAOFactory.commitTransaction();
                orderDAOFactory.commitTransaction();

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl","cart/confirmationPage");
            } else {
                // Passa l'elenco dei prodotti non disponibili alla pagina di errore
                request.setAttribute("unavailableProducts", unavailableProducts);
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl","cart/errorPage");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
                if (cartDAOFactory != null) cartDAOFactory.rollbackTransaction();
                if (productDAOFactory != null) productDAOFactory.rollbackTransaction();
                if (orderDAOFactory != null) orderDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
                if (cartDAOFactory != null) cartDAOFactory.closeTransaction();
                if (productDAOFactory != null) productDAOFactory.closeTransaction();
                if (orderDAOFactory != null) orderDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

}
