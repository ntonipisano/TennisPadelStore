package com.pisano.tennispadelstore.model.dao;

import com.pisano.tennispadelstore.model.mo.Cart;

import java.util.Map;
import java.util.HashMap;

public interface CartDAO {
    public void addProductToCart(Long productId, int quantity);
    public void removeProductFromCart(Long productId);
    public Map<Long, Integer> getCartItems();
    public void updateProductQuantity(Long productId, int quantity);
    public void clearCart();
    public String getTotalPrice();
}
