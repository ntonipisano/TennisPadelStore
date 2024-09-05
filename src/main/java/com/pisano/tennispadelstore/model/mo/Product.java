package com.pisano.tennispadelstore.model.mo;

import java.sql.Blob;
public class Product {

    private Long productid;
    private String nome;
    private String descrizione;
    private String prezzo;
    private String categoria;
    private String brand;
    private String disponibilita;
    private boolean vetrina;
    private boolean deleted;
    private Blob image;

    public Long getProductid() {
        return productid;
    }
    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getPrezzo() {
        return prezzo;
    }
    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDisponibilita() {
        return disponibilita;
    }
    public void setDisponibilita(String disponibilita) {
        this.disponibilita = disponibilita;
    }

    public boolean getVetrina() {
        return vetrina;
    }
    public void setVetrina(boolean vetrina) {
        this.vetrina = vetrina;
    }

    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Blob getImage () { return image; }
    public void setImage (Blob image) { this.image=image; }
}
