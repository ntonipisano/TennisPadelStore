package com.pisano.tennispadelstore.model.dao.MySQLJDBCImpl;

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

    /*Metodo per salvare un ordine sul db*/
    @Override
    public Order create(Long orderid, Long userid, String costo, String stato, String indirizzo, String dataordine) {

        Order order = new Order();
        order.setOrderId(orderid);
        order.setUserId(userid);
        order.setCosto(costo);
        order.setStato(stato);
        order.setIndirizzo(indirizzo);
        order.setDataordine(dataordine);
        order.setDeleted(false);

        try {
            String sql = "INSERT INTO `order` (orderid, userid, costo, stato, indirizzo, dataordine, deleted) VALUES (?, ?, ?, ?, ?, ?, 'N')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, orderid);
            ps.setLong(2, userid);
            ps.setString(3, costo);
            ps.setString(4, stato);
            ps.setString(5, indirizzo);
            ps.setString(6, dataordine);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    /*Metodo per modificare un ordine sul db*/
    @Override
    public void update(Order order) {
        try {
            String sql = "UPDATE `order` SET costo = ?, stato = ?, indirizzo = ?, dataordine = ?, deleted = 'N' WHERE orderid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, order.getCosto());
            ps.setString(2, order.getStato());
            ps.setString(3, order.getIndirizzo());
            ps.setString(4, order.getDataordine());
            ps.setLong(5, order.getOrderId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*Metodo per eliminare un ordine dal db*/
    @Override
    public void delete(Order order) {
        try {
            String sql = "UPDATE `order` SET deleted = 'S' WHERE orderid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, order.getOrderId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*Metodo per cercare un ordine dal suo id*/
    @Override
    public Order findByOrderId(Order orderid) {
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
            order.setDeleted(rs.getString("deleted").equals("S"));
        } catch (SQLException sqle) {
        }
        return order;
    }

}
