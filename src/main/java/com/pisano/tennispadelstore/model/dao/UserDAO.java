package com.pisano.tennispadelstore.model.dao;

import com.pisano.tennispadelstore.model.mo.User;

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

    public User findLoggedUser();

    public User findByUserId(Long userId);

    public User findByUsername(String username);

}
