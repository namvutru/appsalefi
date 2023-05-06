package com.example.crudfirebase.model;

public class User {
    String gmail;
    String nameuser;
    String sdt;
    String diachi;

    public User() {
    }

    public User(String gmail, String nameuser, String sdt, String diachi) {
        this.gmail = gmail;
        this.nameuser = nameuser;
        this.sdt = sdt;
        this.diachi = diachi;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }
}
