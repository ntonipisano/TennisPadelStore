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
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderController {
    private OrderController() {
    }

    public static void createOrder(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        User loggedUser;
        DAOFactory cartDAOFactory = null;
        DAOFactory productDAOFactory = null;
        DAOFactory orderDAOFactory = null;
        boolean allAvailable = true;
        List<Product> unavailableProducts = new ArrayList<>();
        String costo = request.getParameter("costototale");
        String indirizzo = request.getParameter("indirizzo");

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

            productDAOFactory.commitTransaction();

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
            //Order order = new Order();
            //order.setUserId(loggedUser.getUserId());
            // Imposta altri dettagli dell'ordine (costo, indirizzo, ecc.)

            Map orderFactoryParameters = new HashMap<String, Object>();
            orderFactoryParameters.put("request", request);
            orderFactoryParameters.put("response", response);
            orderDAOFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, orderFactoryParameters);
            orderDAOFactory.beginTransaction();

            OrderDAO orderDAO = orderDAOFactory.getOrderDAO();

            orderDAO.create(null,loggedUser,costo,"Preso in carico",indirizzo,); // Usa il DAO per creare l'ordine

            // Redirect alla pagina di conferma
            response.sendRedirect("confirmationPage.jsp");
        } else {
            // Passa l'elenco dei prodotti non disponibili alla pagina di errore
            request.setAttribute("unavailableProducts", unavailableProducts);
            RequestDispatcher dispatcher = request.getRequestDispatcher("unavailablePage.jsp");
            dispatcher.forward(request, response);
        }
    }

}
