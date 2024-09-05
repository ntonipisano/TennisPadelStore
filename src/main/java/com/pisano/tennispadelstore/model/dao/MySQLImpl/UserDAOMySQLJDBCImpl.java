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

    @Override
    public User create (
            Long userid,
            String username,
            String password,
            String nome,
            String cognome,
            boolean admin) {

        User user = new User();
        user.setUserId(userid);
        user.setUsername(username);
        user.setPassword(password);
        user.setNome(nome);
        user.setCognome(cognome);
        user.setAdmin(admin);
        user.setDeleted(false);

        PreparedStatement ps;

        try {
            String sql = "INSERT INTO user (username, password, nome, cognome, admin, deleted) VALUES (?, ?, ?, ?, ?, 'N')";

            ps = conn.prepareStatement(sql);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNome());
            ps.setString(4, user.getCognome());
            ps.setBoolean(5, user.isAdmin());
            //deleted passato come parametro sopra

            ps.executeUpdate();
            //Per fare operazioni di insert si utilizza executeUpdate()
        }
        catch(SQLException e) {
throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public void update(User user) {
        PreparedStatement ps;

        try {
            String sql = "UPDATE user SET username = ?, password = ?, nome = ?, cognome = ?, admin = ?, deleted = 'N' WHERE userid = ?";

            ps = conn.prepareStatement(sql);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNome());
            ps.setString(4, user.getCognome());
            ps.setBoolean(5, user.isAdmin());
            ps.setLong(6, user.getUserId());
            //Setto deleted a N per update

            ps.executeUpdate();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(User user) {
        PreparedStatement ps;

        try {
            String sql = "UPDATE user SET deleted = 'S' WHERE userid = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getUserId());

            ps.executeUpdate();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByUserId(Long userId) {
        String sql = "SELECT * FROM user WHERE userid = ? AND deleted = 'N'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getLong("userid"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setNome(rs.getString("nome"));
                    user.setCognome(rs.getString("cognome"));
                    user.setAdmin(rs.getString("admin").equals("S"));
                    user.setDeleted(rs.getString("deleted").equals("S"));
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Restituisce null se non viene trovato nessun utente
    }

    /*
    public User findLoggedUser() {
        Long loggedInUserId = return findByUserId(loggedInUserId);
    }
    Qui loggedInUserId deve essere un token o una variabile di sessione
    Lo implementerò più in là quando mi servirà
    */

    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ? AND deleted = 'N'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getLong("userid"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setNome(rs.getString("nome"));
                    user.setCognome(rs.getString("cognome"));
                    user.setAdmin(rs.getString("admin").equals("S"));
                    user.setDeleted(rs.getString("deleted").equals("S"));
                    return user;
                }
                rs.close();
                ps.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Restituisce null se non viene trovato nessun utente
    }

    @Override
    public void makeAdmin(Long userId) {
        updateAdminStatus(userId, "S");
    }

    @Override
    public void removeAdmin(Long userId) {
        updateAdminStatus(userId, "N");
    }

    //Implemento come metodo privato direttamente qui updateAdminStatus per motivi di sicurezza
    //Essendo privato non potevo dichiararlo nell'interfaccia UserDAO senza implementarlo
    private void updateAdminStatus(Long userId, String adminStatus) {
        String sql = "UPDATE user SET admin = ? WHERE userid = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, adminStatus);
            ps.setLong(2, userId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Nessun utente trovato con l'ID: " + userId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la modifica dei privilegi dell'utente", e);
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
