package com.pisano.tennispadelstore.model.dao.CookieImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.pisano.tennispadelstore.model.mo.User;
import com.pisano.tennispadelstore.model.dao.UserDAO;
public class UserDAOCookieImpl implements UserDAO{
    HttpServletRequest request;
    HttpServletResponse response;

    public UserDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}
