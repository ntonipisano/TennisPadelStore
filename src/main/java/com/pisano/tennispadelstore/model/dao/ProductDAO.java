package com.pisano.tennispadelstore.model.dao;

import com.pisano.tennispadelstore.model.mo.Product;

import java.sql.Blob;
import java.util.List;

public interface ProductDAO {
    public Product create(
            String nome,
            String descrizione,
            String prezzo,
            String categoria,
            String brand,
            String disponibilita,
            boolean vetrina,
            Blob image);

    public void update(Product product);
    public void delete(Product product);
    public Product findByProductId(Long productid);
    public List <Product> findbyCategory (String categoria);
    public List <Product> findbyBrand (String brand);
    public List <Product> findbyName (String nome);
    public List <Product> findFeaturedProducts();
    public List <Product> findAllProducts();
    public void modDispbyProductid (Long productid, String disponibilita);

}
