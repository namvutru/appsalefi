package com.example.crudfirebase.model;

public class DonHang {

    String idDonhang;
    String idUser;
    String idSanpham;

    int Soluong;
    String thoigian;
    String trangthai;

    public DonHang() {
    }

    public DonHang(String idDonhang, String idUser, String idSanpham, int soluong, String thoigian, String trangthai) {
        this.idDonhang = idDonhang;
        this.idUser = idUser;
        this.idSanpham = idSanpham;
        Soluong = soluong;
        this.thoigian = thoigian;
        this.trangthai = trangthai;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public String getIdDonhang() {
        return idDonhang;
    }

    public void setIdDonhang(String idDonhang) {
        this.idDonhang = idDonhang;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdSanpham() {
        return idSanpham;
    }

    public void setIdSanpham(String idSanpham) {
        this.idSanpham = idSanpham;
    }

    public int getSoluong() {
        return Soluong;
    }

    public void setSoluong(int soluong) {
        Soluong = soluong;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }
}
