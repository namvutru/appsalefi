package com.example.crudfirebase.model;

public class ItemCart {
    String idcart;

    String username;
    String idproduct;
    int soluong;
    float giatien;

    public ItemCart() {
    }

    public ItemCart(String idcart, String username, String idproduct, int soluong, float giatien) {
        this.idcart = idcart;
        this.username = username;
        this.idproduct = idproduct;
        this.soluong = soluong;
        this.giatien = giatien;
    }

    public String getIdcart() {
        return idcart;
    }

    public void setIdcart(String idcart) {
        this.idcart = idcart;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(String idproduct) {
        this.idproduct = idproduct;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public float getGiatien() {
        return giatien;
    }

    public void setGiatien(float giatien) {
        this.giatien = giatien;
    }

    @Override
    public String toString() {
        return "ItemCart{" +
                "idcart='" + idcart + '\'' +
                ", username='" + username + '\'' +
                ", idproduct='" + idproduct + '\'' +
                ", soluong=" + soluong +
                ", giatien=" + giatien +
                '}';
    }
}
