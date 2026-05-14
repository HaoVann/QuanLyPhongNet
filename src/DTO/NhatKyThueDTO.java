package DTO;

import java.sql.Timestamp;

public class NhatKyThueDTO {
    private int maThue;
    private int maTK; // Khớp với module của Hùng
    private String maMay; // Khớp với module Máy Tính
    private Timestamp thoiGianBatDau;
    private Timestamp thoiGianKetThuc;
    private double tongTien;
    private String trangThai; // "DANG_THUE" hoặc "DA_THANH_TOAN"

    public NhatKyThueDTO() {}

    // Getters và Setters
    public int getMaThue() { return maThue; }
    public void setMaThue(int maThue) { this.maThue = maThue; }
    public int getMaTK() { return maTK; }
    public void setMaTK(int maTK) { this.maTK = maTK; }
    public String getMaMay() { return maMay; }
    public void setMaMay(String maMay) { this.maMay = maMay; }
    public Timestamp getThoiGianBatDau() { return thoiGianBatDau; }
    public void setThoiGianBatDau(Timestamp thoiGianBatDau) { this.thoiGianBatDau = thoiGianBatDau; }
    public Timestamp getThoiGianKetThuc() { return thoiGianKetThuc; }
    public void setThoiGianKetThuc(Timestamp thoiGianKetThuc) { this.thoiGianKetThuc = thoiGianKetThuc; }
    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}