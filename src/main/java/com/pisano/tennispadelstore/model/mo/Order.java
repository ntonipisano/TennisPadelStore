package com.pisano.tennispadelstore.model.mo;

public class Order {

    private Long orderid;
    private Long userid;
    private String costo;
    private String stato;
    private String indirizzo;
    private String dataordine;
    private boolean deleted;

    public Long getOrderId() {
        return orderid;
    }
    public void setOrderId(Long orderid) {
        this.orderid = orderid;
    }

    public Long getUserId() {
        return userid;
    }
    public void setUserId(Long userid) {
        this.userid = userid;
    }

    public String getCosto() {
        return costo;
    }
    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getStato() {
        return stato;
    }
    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getIndirizzo() {
        return indirizzo;
    }
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getDataordine() {
        return dataordine;
    }
    public void setDataordine(String dataordine) {
        this.dataordine = dataordine;
    }

    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
