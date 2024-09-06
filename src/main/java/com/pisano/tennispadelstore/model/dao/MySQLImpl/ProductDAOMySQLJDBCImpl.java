package com.pisano.tennispadelstore.model.dao.MySQLImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Blob;

import com.pisano.tennispadelstore.model.dao.ProductDAO;
import com.pisano.tennispadelstore.model.mo.Product;

public class ProductDAOMySQLJDBCImpl implements ProductDAO {

    Connection conn;
    public ProductDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    /*Metodo per salvare un nuovo prodotto sul db*/
    @Override
    public Product create (
            Long productid,
            String nome,
            String descrizione,
            String prezzo,
            String categoria,
            String brand,
            String disponibilita,
            boolean vetrina,
            Blob image ) {

        Product product = new Product();
        product.setProductid(productid);
        product.setNome(nome);
        product.setDescrizione(descrizione);
        product.setPrezzo(prezzo);
        product.setCategoria(categoria);
        product.setBrand(brand);
        product.setDisponibilita(disponibilita);
        product.setVetrina(vetrina);
        product.setDeleted(false);
        product.setImage(image);

        PreparedStatement ps;

        try {
            String sql = "INSERT INTO product (productid, nome, descrizione, prezzo, categoria, brand, disponibilita, vetrina, deleted, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'N', ?)";

            ps = conn.prepareStatement(sql);

            ps.setLong(1,product.getProductid());
            ps.setString(2,product.getNome());
            ps.setString(3,product.getDescrizione());
            ps.setString(4,product.getPrezzo());
            ps.setString(5,product.getCategoria());
            ps.setString(6,product.getBrand());
            ps.setString(7,product.getDisponibilita());
            ps.setBoolean(8,product.getVetrina());
            ps.setBlob(9,product.getImage());

            ps.executeUpdate();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    /*Metodo per modificare un prodotto sul db*/
    @Override
    public void update (Product product) {
        PreparedStatement ps;

        try {
            String sql = "UPDATE product SET nome = ?, descrizione = ?, prezzo = ?, categoria = ?, brand = ?, disponibilita = ?, vetrina = ?, deleted = 'N', image = ? WHERE productid = ?";

            ps = conn.prepareStatement(sql);

            ps.setString(1,product.getNome());
            ps.setString(2,product.getDescrizione());
            ps.setString(3,product.getPrezzo());
            ps.setString(4,product.getCategoria());
            ps.setString(5,product.getBrand());
            ps.setString(6,product.getDisponibilita());
            ps.setBoolean(7,product.getVetrina());
            ps.setBlob(8,product.getImage());
            ps.setLong(9,product.getProductid());

            ps.executeUpdate();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*Metodo per eliminare un prodotto dal db*/
    @Override
    public void delete(Product product) {
        PreparedStatement ps;

        try {
            String sql = "UPDATE product SET deleted = 'S' WHERE productid = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, product.getProductid());

            ps.executeUpdate();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*Metodo per trovare le informazioni su un prodotto sul db a partire dal suo productid*/
    @Override
    public Product findByProductId(Long productId) {
        Product product = null;
        String sql = "SELECT * FROM product WHERE productid = ? AND deleted = 'N'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = read(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
    /*
    @Override
    public Product findByProductId(Long productid) {
        PreparedStatement ps;

        try {
            String sql = "SELECT * FROM product WHERE productid = ? AND deleted = 'N'";
            ps = conn.prepareStatement(sql);
            ps.setLong(1,productid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setProductid(rs.getLong("productid"));
                    product.setNome(rs.getString("nome"));
                    product.setDescrizione(rs.getString("descrizione"));
                    product.setPrezzo(rs.getString("prezzo"));
                    product.setCategoria(rs.getString("categoria"));
                    product.setBrand(rs.getString("brand"));
                    product.setDisponibilita(rs.getString("disponibilita"));
                    product.setVetrina(rs.getString("vetrina").equals("S"));
                    product.setDeleted(rs.getString("deleted").equals("S"));
                    product.setImage(rs.getBlob("image"));
                    return product;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
        }
        */

    /*Metodo per filtrare i prodotti in base alla categoria */
    @Override
    public List<Product> findbyCategory(String category) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE categoria = ? AND deleted = 'N'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(read(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /*Metodo per filtrare i prodotti in base al brand */
    @Override
    public List<Product> findbyBrand(String brand) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE brand = ? AND deleted = 'N'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brand);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(read(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /*Metodo per cercare i prodotti tramite il nome*/
    @Override
    public List<Product> findbyName(String name) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE nome LIKE ? AND deleted = 'N'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(read(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /*Metodo che permette agli amministratori di settare la disponibilità di un prodotto*/
    @Override
    public void modDispbyProductid(Long productId, String nuovaDisponibilita) {
        String query = "UPDATE product SET disponibilita = ? WHERE productid = ? AND deleted = 'N'";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nuovaDisponibilita);
            ps.setLong(2, productId);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Puoi aggiungere un'eccezione custom o un log per gestire l'errore in modo più specifico
        }
    }

    Product read(ResultSet rs) {

        Product product = new Product();
        try {
            product.setProductid(rs.getLong("productid"));
        } catch (SQLException sqle) {
        }
        try {
            product.setNome(rs.getString("nome"));
        } catch (SQLException sqle) {
        }
        try {
            product.setDescrizione(rs.getString("descrizione"));
        } catch (SQLException sqle) {
        }
        try {
            product.setPrezzo(rs.getString("prezzo"));
        } catch (SQLException sqle) {
        }
        try {
            product.setCategoria(rs.getString("categoria"));
        } catch (SQLException sqle) {
        }
        try {
            product.setBrand(rs.getString("brand"));
        } catch (SQLException sqle) {
        }
        try {
            product.setDisponibilita(rs.getString("disponibilita"));
        } catch (SQLException sqle)  {
        }
        try {
            product.setVetrina(rs.getString("vetrina").equals("S"));
        } catch (SQLException sqle) {
        }
        try {
            product.setDeleted(rs.getString("deleted").equals("S"));
        } catch (SQLException sqle) {
        }
        try {
            product.setImage(rs.getBlob("image"));
        } catch (SQLException sqle) {
        }
        return product;
    }
}

