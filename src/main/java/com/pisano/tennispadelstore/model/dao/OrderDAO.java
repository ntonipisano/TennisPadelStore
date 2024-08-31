package com.pisano.tennispadelstore.model.dao;

import com.pisano.tennispadelstore.model.mo.Order;

public interface OrderDAO {
    public Order create(
            Long orderid,
            Long userid,
            String costo,
            String stato,
            String indirizzo,
            String dataordine);

    public void update(Order order);

    public void delete(Order order);

    public Order findByOrderId(Order order);
}
