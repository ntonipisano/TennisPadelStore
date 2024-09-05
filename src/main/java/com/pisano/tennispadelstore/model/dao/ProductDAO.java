package com.pisano.tennispadelstore.model.dao;

import com.pisano.tennispadelstore.model.mo.Product;

import java.sql.Blob;

public interface ProductDAO {
    public Product create(
            Long productid,
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
}
