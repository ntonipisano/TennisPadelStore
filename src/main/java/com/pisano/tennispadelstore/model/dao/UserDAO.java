package com.pisano.tennispadelstore.model.dao;

import com.pisano.tennispadelstore.model.mo.User;
import com.pisano.tennispadelstore.model.mo.Order;

import java.util.List;
public interface UserDAO {

    public User create(
            Long userid,
            String username,
            String password,
            String nome,
            String cognome,
            boolean admin);

    public void update(User user);

    public void delete(User user);

    /* public User findLoggedUser(); */

    public User findByUserId(Long userId);

    public User findByUsername(String username);

    public void makeAdmin(Long userid);
    public void removeAdmin(Long userid);
    public List<Order> getOrdersByUserId(Long userId);

}
