package com.pisano.tennispadelstore.model.dao.MySQLImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.pisano.tennispadelstore.model.dao.OrderDAO;
import com.pisano.tennispadelstore.model.mo.Order;

public class OrderDAOMySQLJDBCImpl implements OrderDAO {

    Connection conn;
    public OrderDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

}
