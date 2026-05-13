package DTO;

public class TaiKhoanDTO {
    private int id;
    private String tenDangNhap, matKhau, vaiTro;
    private double soDu;

    public TaiKhoanDTO() {}
    public TaiKhoanDTO(int id, String tenDangNhap, String matKhau, String vaiTro, double soDu) {
        this.id = id; this.tenDangNhap = tenDangNhap; this.matKhau = matKhau;
        this.vaiTro = vaiTro; this.soDu = soDu;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    public double getSoDu() { return soDu; }
    public void setSoDu(double soDu) { this.soDu = soDu; }
}