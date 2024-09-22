package com.pisano.tennispadelstore.model.dao.CookieImpl;

import com.pisano.tennispadelstore.model.dao.CartDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

public class CartDAOCookieImpl implements CartDAO {
    private static final String CART_COOKIE_NAME = "cart";
    private static final String COOKIE_DELIMITER = "#";
    private static final String ITEM_DELIMITER = ":";
    HttpServletRequest request;
    HttpServletResponse response;

    public CartDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void addProductToCart(Long productId, int quantity) {
        Map<Long, Integer> cartItems = getCartItems();
        cartItems.put(productId, cartItems.getOrDefault(productId, 0) + quantity);
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

    private void saveCartItems(Map<Long, Integer> cartItems) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            sb.append(entry.getKey()).append(ITEM_DELIMITER).append(entry.getValue()).append(COOKIE_DELIMITER);
        }
        Cookie cartCookie = new Cookie(CART_COOKIE_NAME, sb.toString());
        cartCookie.setPath("/");
        response.addCookie(cartCookie);
    }

    @Override
    public Map <Long,Integer> decreaseProductQuantity(Long productId, HttpServletRequest request, HttpServletResponse response) {
        // Ottieni il carrello dal cookie
        Map<Long, Integer> cart = getCartFromCookie(request);

        if (cart.containsKey(productId)) {
            int currentQuantity = cart.get(productId);

            if (currentQuantity > 1) {
                cart.put(productId, currentQuantity - 1);
            } else {
                cart.remove(productId); // Se la quantità è 1 rimuovi il prodotto
            }
        }
        updateCartCookie(cart, response);
        return cart;
    }

    @Override
    public Map<Long,Integer> increaseProductQuantity(Long productId, HttpServletRequest request, HttpServletResponse response) {
        Map<Long, Integer> cart = getCartFromCookie(request);

        if (cart.containsKey(productId)) {
            int currentQuantity = cart.get(productId);
            cart.put(productId, currentQuantity + 1);
        } else {
            cart.put(productId, 1);
        }
        updateCartCookie(cart, response);
        return cart;
    }

    private Map<Long, Integer> getCartFromCookie(HttpServletRequest request) {
        Map<Long, Integer> cart = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart".equals(cookie.getName())) {
                    String[] cartItems = cookie.getValue().split(COOKIE_DELIMITER);
                    for (String item : cartItems) {
                        String[] parts = item.split(":");
                        try {
                            Long productId = Long.parseLong(parts[0]);
                            Integer quantity = Integer.parseInt(parts[1]);
                            cart.put(productId, quantity);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        }
        return cart;
    }

    private void updateCartCookie(Map<Long, Integer> cart, HttpServletResponse response) {
        StringBuilder cartValue = new StringBuilder();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            if (cartValue.length() > 0) {
                cartValue.append(COOKIE_DELIMITER);
            }
            cartValue.append(entry.getKey()).append(":").append(entry.getValue());
        }

        Cookie cartCookie = new Cookie("cart", cartValue.toString());
        cartCookie.setMaxAge(60 * 60 * 24);
        cartCookie.setPath("/");
        response.addCookie(cartCookie);
    }

    }

