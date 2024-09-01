package com.pisano.tennispadelstore.model.dao.MySQLImpl;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.pisano.tennispadelstore.model.dao.DAOFactory;
import com.pisano.tennispadelstore.model.dao.UserDAO;
import com.pisano.tennispadelstore.model.dao.OrderDAO;
import com.pisano.tennispadelstore.model.dao.ProductDAO;
import com.pisano.tennispadelstore.model.dao.AdminkeyDAO;
import com.pisano.tennispadelstore.services.config.Configuration;
public class MySQLJDBCDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private Connection connection;

    public MySQLJDBCDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {

        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOMySQLJDBCImpl(connection);
    }

    @Override
    public ProductDAO getProductDAO() { return new ProductDAOMySQLJDBCImpl(connection);}

    @Override
    public OrderDAO getOrderDAO() { return new OrderDAOMySQLJDBCImpl(connection);}

    @Override
    public AdminkeyDAO getAdminkeyDAO() { return new AdminkeyDAOMySQLJDBCImpl(connection);}

    //SCRIVERE ALTRI SE SERVONO ALTRI MODEL
}
