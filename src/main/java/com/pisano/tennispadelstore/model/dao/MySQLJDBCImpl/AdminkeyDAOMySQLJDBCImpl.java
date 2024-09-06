package com.pisano.tennispadelstore.model.dao.MySQLJDBCImpl;

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

    @Override
    public Adminkey create (String key) {
        Adminkey adminkey = new Adminkey();
        adminkey.setKey(key);

        PreparedStatement ps;
        try {
            String sql = "INSERT INTO adminkey (key) VALUES (?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, adminkey.getKey());

            ps.executeUpdate();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return adminkey;
    }

    @Override
    public void update (Adminkey adminkey) {
        PreparedStatement ps;
        try {
            String sql = "UPDATE adminkey SET key = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, adminkey.getKey());
            ps.executeUpdate();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String Checkkey() {
        String sql = "SELECT `key` FROM adminkey LIMIT 1"; // Una sola riga con la chiave
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("key");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Se non viene trovata nessuna chiave
    }
}

