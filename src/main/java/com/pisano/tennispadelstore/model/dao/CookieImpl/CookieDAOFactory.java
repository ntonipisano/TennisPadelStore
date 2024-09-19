package com.pisano.tennispadelstore.model.dao.CookieImpl;

import com.pisano.tennispadelstore.model.dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class CookieDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public CookieDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            this.request=(HttpServletRequest) factoryParameters.get("request");
            this.response=(HttpServletResponse) factoryParameters.get("response");;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {}

    @Override
    public void rollbackTransaction() {}

    @Override
    public void closeTransaction() {}

    @Override
    public UserDAO getUserDAO() { return new UserDAOCookieImpl(request,response); }

    @Override
    public CartDAO getCartDAO() { return new CartDAOCookieImpl(request,response); }

    @Override
    public ProductDAO getProductDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OrderDAO getOrderDAO() { throw new UnsupportedOperationException("Not supported yet.");}

    @Override
    public AdminkeyDAO getAdminkeyDAO() { throw new UnsupportedOperationException("Not supported yet.");}

}
