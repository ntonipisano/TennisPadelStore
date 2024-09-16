package com.pisano.tennispadelstore.model.dao.MySQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.pisano.tennispadelstore.model.dao.UserDAO;
import com.pisano.tennispadelstore.model.mo.User;
import com.pisano.tennispadelstore.model.mo.Order;

public class UserDAOMySQLJDBCImpl implements UserDAO {

    Connection conn;
    public UserDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    /*Metodo per creare un nuovo utente nel db*/
    @Override
    public User create (
            Long userid,
            String username,
            String password,
            String nome,
            String cognome,
            boolean admin) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNome(nome);
        user.setCognome(cognome);
        user.setAdmin(admin);
        user.setDeleted(false);

        PreparedStatement ps = null;
        ResultSet generatedKeys = null;

        try {
            String sql = "INSERT INTO user (username, password, nome, cognome, admin, deleted) VALUES (?, ?, ?, ?, ?, 'N')";

            // Specifica che vuoi ottenere le chiavi generate
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNome());
            ps.setString(4, user.getCognome());
            ps.setString(5, admin ? "S" : "N");

            // Esegui l'inserimento
            ps.executeUpdate();

            // Recupera le chiavi userid generate
            generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setUserId(generatedKeys.getLong(1)); // Imposta l'ID generato nel tuo oggetto User
            } else {
                throw new SQLException("Creazione utente fallita, nessun userid generato.");
            }
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                throw new RuntimeException("Errore durante la chiusura delle risorse", e);
            }
        }

        return user;
    }

    /*Metodo per aggiornare uno o più campi di un utente nel db*/
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

    /*Metodo per eliminare un utente nel db*/
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

    /*Metodo per trovare i dati di un utente nel db a partire dallo userid*/
    public User findByUserId(Long userId) {
        User user = null;
        String sql = "SELECT * FROM user WHERE userid = ? AND deleted = 'N'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = read(rs);
                    /*
                    User user = new User();
                    user.setUserId(rs.getLong("userid"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setNome(rs.getString("nome"));
                    user.setCognome(rs.getString("cognome"));
                    user.setAdmin(rs.getString("admin").equals("S"));
                    user.setDeleted(rs.getString("deleted").equals("S"));
                    return user;
                     */
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user; // Restituisce null se non viene trovato nessun utente
    }

    /*
    public User findLoggedUser() {
        Long loggedInUserId = return findByUserId(loggedInUserId);
    }
    Qui loggedInUserId deve essere un token o una variabile di sessione
    Lo implementerò più in là quando mi servirà
    */

    /*Metodo per trovare i dati di un utente nel db a partire dal suo username */
    public User findByUsername(String username) {
        User user = null;
        String sql = "SELECT * FROM user WHERE username = ? AND deleted = 'N'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = read(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    /*Metodo per dare i privilegi di admin a un utente (settando admin a S nel db)*/
    @Override
    public void makeAdmin(Long userId) {
        updateAdminStatus(userId, "S");
    }

    /*Metodo per togliere i privilegi di admin a un utente (settando admin a N nel db)*/
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

    /*Metodo per visualizzare gli ordini relativi a un utente*/
    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM order WHERE userid = ? AND deleted = 'N'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getLong("orderid"));
                    order.setUserId(rs.getLong("userid"));
                    order.setCosto(rs.getString("costo"));
                    order.setStato(rs.getString("stato"));
                    order.setIndirizzo(rs.getString("indirizzo"));
                    order.setDataordine(rs.getString("dataordine"));
                    order.setDeleted(rs.getBoolean("deleted"));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero degli ordini dell'utente", e);
        }
        return orders;
    }

    @Override
    public User findLoggedUser()  {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public List <User> findAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE deleted ='N'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(read(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
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
            user.setAdmin(rs.getString("admin").equals("S"));
        } catch (SQLException sqle) {
        }
        try {
            user.setDeleted(rs.getString("deleted").equals("S"));
        } catch (SQLException sqle) {
        }
        return user;
    }
}
