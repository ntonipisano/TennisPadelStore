package com.pisano.tennispadelstore.model.mo;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Long, Integer> items = new HashMap<>();

    public Map<Long, Integer> getItems() {
        return items;
    }

    public void addItem(Long productId, int quantity) {
        items.put(productId, items.getOrDefault(productId, 0) + quantity);
    }

    public void removeItem(Long productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }
}

