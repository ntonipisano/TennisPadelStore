package com.pisano.tennispadelstore.model.dao;

import com.pisano.tennispadelstore.model.mo.Product;

public interface ProductDAO {
    public Product create(
            Long productid,
            String nome,
            String descrizione,
            String prezzo,
            String categoria,
            String brand,
            String disponibilita,
            boolean vetrina);

    public void update(Product product);

    public void delete(Product product);

    public Product findByProductId(Product product);
}
