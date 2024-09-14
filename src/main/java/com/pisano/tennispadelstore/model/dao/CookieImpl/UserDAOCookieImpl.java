package com.pisano.tennispadelstore.model.dao.CookieImpl;

import com.pisano.tennispadelstore.model.mo.Order;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.pisano.tennispadelstore.model.mo.User;
import com.pisano.tennispadelstore.model.dao.UserDAO;

import java.util.List;

public class UserDAOCookieImpl implements UserDAO{
    HttpServletRequest request;
    HttpServletResponse response;

    public UserDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public User create(
            Long userid,
            String username,
            String password,
            String nome,
            String cognome,
            boolean admin) {

        User loggedUser = new User();
        loggedUser.setUserId(userid);
        loggedUser.setNome(nome);
        loggedUser.setAdmin(admin);

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;
    }

    @Override
    public void update(User loggedUser) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public void delete(User loggedUser) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public User findLoggedUser() {

        Cookie[] cookies = request.getCookies();
        User loggedUser = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {
                if (cookies[i].getName().equals("loggedUser")) {
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }
        return loggedUser;

    }
    @Override
    public User findByUserId(Long userId) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public User findByUsername(String username) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void makeAdmin (Long userid) { throw new UnsupportedOperationException("Not supported.");}

    @Override
    public void removeAdmin (Long userid) { throw new UnsupportedOperationException("Not supported.");}

    @Override
    public List<Order> getOrdersByUserId(Long userId) { throw new UnsupportedOperationException("Not supported");}

    @Override
    public List<User> findAllUsers() {throw new UnsupportedOperationException("Not supported");}

    private String encode(User loggedUser) {

        String encodedLoggedUser;
        encodedLoggedUser = loggedUser.getUserId() + "#" + loggedUser.getNome() + "#" + loggedUser.isAdmin();

        return encodedLoggedUser;
    }

    private User decode(String encodedLoggedUser) {

        User loggedUser = new User();

        String[] values = encodedLoggedUser.split("#");

        loggedUser.setUserId(Long.parseLong(values[0]));
        loggedUser.setNome(values[1]);
        boolean isAdmin = values[2].equals("S");    //variabile isAdmin per convertire da stringa a boolean
        loggedUser.setAdmin(isAdmin);

        return loggedUser;

    }

}
