package DTO;

public class DichVuDTO {
    private int maDV;
    private String tenDV;
    private LoaiDichVu loai;
    private double gia;
    private int tonKho;
    private int nhapHang;

    public int getMa(){
        return this.maDV;
    }

    public void setMa(int ma){
        this.maDV = ma;
    }

    public String getTen(){
        return this.tenDV;
    }

    public void setTen(String ten){
        this.tenDV = ten;
    }

    public LoaiDichVu getLoai(){
        return this.loai;
    }

    public void setLoai(LoaiDichVu loai){
        this.loai = loai;
    }
    
    public double getGia(){
        return this.gia;
    }

    public void setGia(double gia){
        this.gia = gia;
    }

    public int getTonKho(){
        return this.tonKho;
    }

    public void setTonKho(int tonKho){
        this.tonKho = tonKho;
    }

    public int getNhapHang(){
        return this.nhapHang;
    }

    public void setNhapHang(int nhapHang){
        this.nhapHang = nhapHang;
    }
}
