package com.pisano.tennispadelstore.model.dao;

import com.pisano.tennispadelstore.model.mo.Cart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.HashMap;

public interface CartDAO {
    public void addProductToCart(Long productId, int quantity);
    public void removeProductFromCart(Long productId);
    public Map<Long, Integer> getCartItems();
    public void updateProductQuantity(Long productId, int quantity);
    public void clearCart();
    public void decreaseProductQuantity(Long productId, HttpServletRequest request, HttpServletResponse response);
    public void increaseProductQuantity(Long productId, HttpServletRequest request, HttpServletResponse response);
    //public Map<Long, Integer> getCartFromCookie(HttpServletRequest request);
}
