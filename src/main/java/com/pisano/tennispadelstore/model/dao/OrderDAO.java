package com.pisano.tennispadelstore.model.dao;

import com.pisano.tennispadelstore.model.mo.Order;

import java.util.List;

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

    public List<Order> findByUserid (Long userid);

    public void modStatobyOrderid (Long orderid, String stato);
}
