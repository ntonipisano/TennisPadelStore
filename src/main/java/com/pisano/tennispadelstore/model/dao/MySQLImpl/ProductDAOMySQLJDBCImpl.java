package com.pisano.tennispadelstore.model.dao.MySQLImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.pisano.tennispadelstore.model.dao.ProductDAO;
import com.pisano.tennispadelstore.model.mo.Product;

public class ProductDAOMySQLJDBCImpl implements ProductDAO {

    Connection conn;
    public ProductDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

}
