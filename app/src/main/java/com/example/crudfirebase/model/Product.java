package com.example.crudfirebase.model;

import java.io.Serializable;

public class Product implements Serializable {
    String id;
    String ten;
    String anh;
    String des;
    float gia;
    String loai;
    int soluong;

    public Product() {
    }

    public Product(String id, String ten, String anh, String des, float gia, String loai, int soluong) {
        this.id = id;
        this.ten = ten;
        this.anh = anh;
        this.des = des;
        this.gia = gia;
        this.loai = loai;
        this.soluong = soluong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", ten='" + ten + '\'' +
                ", anh='" + anh + '\'' +
                ", des='" + des + '\'' +
                ", gia=" + gia +
                ", loai='" + loai + '\'' +
                ", soluong=" + soluong +
                '}';
    }

}
