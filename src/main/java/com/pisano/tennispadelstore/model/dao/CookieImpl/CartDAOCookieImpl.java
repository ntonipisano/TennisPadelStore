package com.pisano.tennispadelstore.model.dao.CookieImpl;

import com.pisano.tennispadelstore.model.dao.CartDAO;
import com.pisano.tennispadelstore.model.dao.ProductDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

public class CartDAOCookieImpl implements CartDAO {
    private static final String CART_COOKIE_NAME = "cart";
    private static final String COOKIE_DELIMITER = ";";
    private static final String ITEM_DELIMITER = ":";
    HttpServletRequest request;
    HttpServletResponse response;
    ProductDAO productDAO;
    public CartDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.productDAO = productDAO;
    }

    @Override
    public void addProductToCart(Long productId, int quantity) {
        Map<Long, Integer> cartItems = getCartItems();
        cartItems.put(productId, cartItems.getOrDefault(productId, 0) + quantity);
        saveCartItems(cartItems);
    }

    @Override
    public void removeProductFromCart(Long productId) {
        Map<Long, Integer> cartItems = getCartItems();
        cartItems.remove(productId);
        saveCartItems(cartItems);
    }

    @Override
    public Map<Long, Integer> getCartItems() {
        Map<Long, Integer> cartItems = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CART_COOKIE_NAME.equals(cookie.getName())) {
                    String[] items = cookie.getValue().split(COOKIE_DELIMITER);
                    for (String item : items) {
                        String[] parts = item.split(ITEM_DELIMITER);
                        if (parts.length == 2) {
                            try {
                                Long productId = Long.parseLong(parts[0]);
                                int quantity = Integer.parseInt(parts[1]);
                                cartItems.put(productId, quantity);
                            } catch (NumberFormatException e) {
                            }
                        }
                    }
                }
            }
        }
        return cartItems;
    }

    @Override
    public void updateProductQuantity(Long productId, int quantity) {
        Map<Long, Integer> cartItems = getCartItems();
        if (cartItems.containsKey(productId)) {
            cartItems.put(productId, quantity);
            saveCartItems(cartItems);
        }
    }

    @Override
    public void clearCart() {
        Cookie cartCookie = new Cookie(CART_COOKIE_NAME, "");
        cartCookie.setMaxAge(0);
        cartCookie.setPath("/");
        response.addCookie(cartCookie);
    }

    @Override
    public String getTotalPrice() {
        double total = 0;  // Usa double per gestire i decimali dei prezzi
        Map<Long, Integer> cartItems = getCartItems();
        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            Long productId = entry.getKey();
            int quantity = entry.getValue();
            String price = getProductPrice(productId);
            try {
                double priceDouble = Double.parseDouble(price);
                total += priceDouble * quantity;
            } catch (NumberFormatException e) {
                // Log error or handle exception
            }
        }
        return String.format("%.2f", total);  // Formatta il totale a due decimali
    }

    private void saveCartItems(Map<Long, Integer> cartItems) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            sb.append(entry.getKey()).append(ITEM_DELIMITER).append(entry.getValue()).append(COOKIE_DELIMITER);
        }
        Cookie cartCookie = new Cookie(CART_COOKIE_NAME, sb.toString());
        cartCookie.setPath("/");
        response.addCookie(cartCookie);
    }

    private String getProductPrice(Long productId) {
        String total = null;
        Map<Long, Integer> cartItems = getCartItems();
        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            Long productId2 = entry.getKey();
            int quantity = entry.getValue();
            String price = productDAO.getProductPricebyId(productId); // Usa il DAO per ottenere il prezzo
            int priceok = Integer.parseInt(price);
            total += priceok * quantity;
        }
        return total;
    }
    }

