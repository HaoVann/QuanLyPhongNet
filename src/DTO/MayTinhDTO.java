package DTO; // Đổi thành viết hoa để đồng bộ với Duy và Hùng

public class MayTinhDTO {
    private String maMay;
    private String tenMay;
    private String trangThai; // TRONG, DANG_CHOI, BAO_TRI
    private double giaTheoGio;

    public MayTinhDTO() {} // Constructor mặc định

    public MayTinhDTO(String maMay, String tenMay, String trangThai, double giaTheoGio) {
        this.maMay = maMay;
        this.tenMay = tenMay;
        this.trangThai = trangThai;
        this.giaTheoGio = giaTheoGio;
    }

    // Getters và Setters hoàn chỉnh
    public String getMaMay() { return maMay; }
    public void setMaMay(String maMay) { this.maMay = maMay; }
    public String getTenMay() { return tenMay; }
    public void setTenMay(String tenMay) { this.tenMay = tenMay; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public double getGiaTheoGio() { return giaTheoGio; }
    public void setGiaTheoGio(double giaTheoGio) { this.giaTheoGio = giaTheoGio; }
}
