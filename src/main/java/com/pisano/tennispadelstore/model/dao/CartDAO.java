package com.pisano.tennispadelstore.model.dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
public interface CartDAO {
    public void addProductToCart(Long productId, int quantity);
    public Map<Long, Integer> getCartItems();
    public Map<Long, Integer> decreaseProductQuantity(Long productId, HttpServletRequest request, HttpServletResponse response);
    public Map<Long, Integer> increaseProductQuantity(Long productId, HttpServletRequest request, HttpServletResponse response);

}
