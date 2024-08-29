package com.pisano.tennispadelstore.model.mo;

public class User {

    private Long userid;
    private String username;
    private String password;
    private String nome;
    private String cognome;
    private boolean admin;
    private boolean deleted;

    private Order[] orders;

    public Long getUserId() {
        return userid;
    }
    public void setUserId(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public boolean isAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Order[] getOrders() {
        return orders;
    }
    public void setOrders(Order[] orders) {
        this.orders = orders;
    }

    /*
    public Contact getContacts(int index) {
        return this.contacts[index];
    }

    public void setContacts(int index, Contact contacts) {
        this.contacts[index] = contacts;
    }
    */
}


