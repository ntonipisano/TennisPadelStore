package com.pisano.tennispadelstore.model.dao.MySQLImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pisano.tennispadelstore.model.dao.AdminkeyDAO;
import com.pisano.tennispadelstore.model.mo.Adminkey;
public class AdminkeyDAOMySQLJDBCImpl implements AdminkeyDAO {

    Connection conn;
    public AdminkeyDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

}
