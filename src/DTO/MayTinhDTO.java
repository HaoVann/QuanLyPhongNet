package DTO;

public class MayTinhDTO {
    private String maMay;
    private String tenMay;
    private String trangThai;
    private double giaTheoGio;

    public MayTinhDTO(String maMay, String tenMay, String trangThai, double giaTheoGio) {
        this.maMay = maMay;
        this.tenMay = tenMay;
        this.trangThai = trangThai;
        this.giaTheoGio = giaTheoGio;
    }

    // Getters
    public String getMaMay() { return maMay; }
    public String getTenMay() { return tenMay; }
    public String getTrangThai() { return trangThai; }
    public double getGiaTheoGio() { return giaTheoGio; }
}
