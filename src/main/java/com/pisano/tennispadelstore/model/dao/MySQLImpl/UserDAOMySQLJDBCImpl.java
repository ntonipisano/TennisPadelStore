package com.pisano.tennispadelstore.model.dao.MySQLImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.pisano.tennispadelstore.model.dao.UserDAO;
import com.pisano.tennispadelstore.model.mo.User;

public class UserDAOMySQLJDBCImpl implements UserDAO {

    Connection conn;
    public UserDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    public User create (
            Long userid,
            String username,
            String password,
            String nome,
            String cognome,
            boolean admin) {

        PreparedStatement ps;

        try {
            String sql = "INSERT INTO user (username, password, nome, cognome, admin, deleted) VALUES (?, ?, ?, ?, ?, 'N')";

            ps = conn.prepareStatement(sql);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNome());
            ps.setString(4, user.getCognome());
            ps.setBoolean(5, user.isAdmin());
            ps.setBoolean(6, user.isDeleted());
            ps.setLong(7, user.getUserId());
        }
        catch(SQLException e) {
throw new RuntimeException(e);
        }
    }


    User read(ResultSet rs) {

        User user = new User();
        try {
            user.setUserId(rs.getLong("userid"));
        } catch (SQLException sqle) {
        }
        try {
            user.setUsername(rs.getString("username"));
        } catch (SQLException sqle) {
        }
        try {
            user.setPassword(rs.getString("password"));
        } catch (SQLException sqle) {
        }
        try {
            user.setNome(rs.getString("nome"));
        } catch (SQLException sqle) {
        }
        try {
            user.setCognome(rs.getString("cognome"));
        } catch (SQLException sqle) {
        }
        try {
            user.setAdmin(rs.getString("admin").equals("Y"));
        } catch (SQLException sqle) {
        }
        try {
            user.setDeleted(rs.getString("deleted").equals("Y"));
        } catch (SQLException sqle) {
        }
        System.out.println(user);
        return user;
    }

}
