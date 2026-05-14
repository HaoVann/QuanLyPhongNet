package DTO;

public class ChiTietGoiDichVuDTO {
    private int maThue; // Thuộc về phiên thuê nào
    private int maDV;   // Khớp với mã Dịch vụ của Duy
    private int soLuong;
    private double thanhTien; // Bằng Số lượng * Giá dịch vụ

    public ChiTietGoiDichVuDTO() {}

    // Getters và Setters
    public int getMaThue() { return maThue; }
    public void setMaThue(int maThue) { this.maThue = maThue; }
    public int getMaDV() { return maDV; }
    public void setMaDV(int maDV) { this.maDV = maDV; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }
}
