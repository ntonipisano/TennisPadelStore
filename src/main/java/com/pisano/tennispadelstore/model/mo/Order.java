package com.pisano.tennispadelstore.model.mo;

public class Order {

    private Long orderid;
    private Long userid;
    private String costo;
    private String stato;
    private String indirizzo;
    private String dataordine;
    private String metododipagamento;
    private String cap;
    private String cellulare;
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

    public String getMetododipagamento() {return metododipagamento;}
    public void setMetododipagamento (String metododipagamento) {this.metododipagamento = metododipagamento;}

    public String getCap() { return cap;}
    public void setCap (String cap) {this.cap=cap;}

    public String getCellulare() {return cellulare;}
    public void setCellulare(String cellulare) {this.cellulare=cellulare;}

    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
