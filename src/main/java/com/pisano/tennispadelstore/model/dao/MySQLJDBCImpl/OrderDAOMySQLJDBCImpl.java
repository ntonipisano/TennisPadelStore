package com.pisano.tennispadelstore.model.dao.MySQLJDBCImpl;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import com.pisano.tennispadelstore.model.dao.OrderDAO;
import com.pisano.tennispadelstore.model.mo.Order;
import com.pisano.tennispadelstore.model.mo.Product;

public class OrderDAOMySQLJDBCImpl implements OrderDAO {
    Connection conn;
    public OrderDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    /*Metodo per salvare un ordine sul db*/
    @Override
    public Order create(Long userid, String costo, String stato, String indirizzo, String dataordine, String metododipagamento, String cap, String cellulare, String nomecognome, String provincia, String citta) {

        Order order = new Order();
        order.setUserId(userid);
        order.setCosto(costo);
        order.setStato(stato);
        order.setIndirizzo(indirizzo);
        order.setDataordine(dataordine);
        order.setMetododipagamento(metododipagamento);
        order.setCap(cap);
        order.setCellulare(cellulare);
        order.setNomecognome(nomecognome);
        order.setProvincia(provincia);
        order.setCitta(citta);
        order.setDeleted(false);

        PreparedStatement ps;

        try {
            String sql = "INSERT INTO `order` (userid, costo, stato, indirizzo, dataordine, metododipagamento, cap, cellulare, nomecognome, provincia, citta, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'N')";
            // Usa RETURN_GENERATED_KEYS per ottenere l'ID generato dal database
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, userid);
            ps.setString(2, costo);
            ps.setString(3, stato);
            ps.setString(4, indirizzo);
            ps.setString(5, dataordine);
            ps.setString(6,metododipagamento);
            ps.setString(7,cap);
            ps.setString(8,cellulare);
            ps.setString(9,nomecognome);
            ps.setString(10,provincia);
            ps.setString(11,citta);

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setOrderId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating product failed, no ID obtained.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
    }

    /*Metodo per modificare un ordine sul db*/
    @Override
    public void update(Order order) {
        try {
            String sql = "UPDATE `order` SET costo = ?, stato = ?, indirizzo = ?, dataordine = ?, metododipagamento = ?, cap = ?, cellulare = ?, nomecognome = ?, provincia = ?, citta = ?, deleted = 'N' WHERE orderid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, order.getCosto());
            ps.setString(2, order.getStato());
            ps.setString(3, order.getIndirizzo());
            ps.setString(4, order.getDataordine());
            ps.setString(5,order.getMetododipagamento());
            ps.setString(6,order.getCap());
            ps.setString(7,order.getCellulare());
            ps.setString(8,order.getNomecognome());
            ps.setString(9,order.getProvincia());
            ps.setString(10,order.getCitta());
            ps.setLong(11, order.getOrderId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*Metodo per eliminare un ordine dal db*/
    @Override
    public void delete(Order order) {
        PreparedStatement ps;

        try {
            String sql = "UPDATE `order` SET deleted = 'S' WHERE orderid = ?";

            ps = conn.prepareStatement(sql);
            Long prova = order.getOrderId();
            ps.setLong(1, order.getOrderId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*Metodo per cercare un ordine dal suo id*/
    @Override
    public Order findByOrderId(Long orderid) {
        Order order = null;
        try {
            String sql = "SELECT * FROM `order` WHERE orderid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, order.getOrderId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                order = read(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    /*Metodo per vedere gli ordini di un utente*/
    @Override
    public List<Order> findByUserid(Long userid) {
        List<Order> orders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM `order` WHERE userid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(read(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /*Metodo per modificare lo stato dell'ordine*/
    @Override
    public void modStatobyOrderid(Long orderid, String stato) {
        try {
            String sql = "UPDATE `order` SET stato = ? WHERE orderid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, stato);
            ps.setLong(2, orderid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List <Order> findAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM `order` WHERE deleted ='N'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(read(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    Order read(ResultSet rs) {

        Order order = new Order();
        try {
            order.setOrderId(rs.getLong("orderid"));
        } catch (SQLException sqle) {
        }
        try {
            order.setUserId(rs.getLong("userid"));
        } catch (SQLException sqle) {
        }
        try {
            order.setCosto(rs.getString("costo"));
        } catch (SQLException sqle) {
        }
        try {
            order.setStato(rs.getString("stato"));
        } catch (SQLException sqle) {
        }
        try {
            order.setIndirizzo(rs.getString("indirizzo"));
        } catch (SQLException sqle) {
        }
        try {
            order.setDataordine(rs.getString("dataordine"));
        } catch (SQLException sqle) {
        }
        try {
            order.setMetododipagamento(rs.getString("metododipagamento"));
        } catch (SQLException sqle) {
        }
        try {
            order.setCap(rs.getString("cap"));
        } catch (SQLException sqle) {
        }
        try {
            order.setCellulare(rs.getString("cellulare"));
        } catch (SQLException sqle) {
        }
        try {
            order.setNomecognome(rs.getString("nomecognome"));
        } catch (SQLException sqle) {
        }
        try {
            order.setProvincia(rs.getString("provincia"));
        } catch (SQLException sqle) {
        }
        try {
            order.setCitta(rs.getString("citta"));
        } catch (SQLException sqle) {
        }
        try {
            order.setDeleted(rs.getString("deleted").equals("S"));
        } catch (SQLException sqle) {
        }
        return order;
    }

}
