package DAL;

import DTO.DichVuDTO;
import DTO.LoaiDichVu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DichVuDAL {
    public void addDichVu(DichVuDTO dichVu){
        String sql = "INSERT INTO DichVuFB (TenDV, Loai, Gia, TonKho, NhapHang) VALUES (?, ?, ?, ?, ?)";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, dichVu.getTen());
            ps.setString(2, dichVu.getLoai().name());
            ps.setDouble(3, dichVu.getGia());
            ps.setInt(4, dichVu.getTonKho());
            ps.setInt(5, dichVu.getNhapHang());

            ps.executeUpdate();
        }catch(Exception e){
            System.out.println("Lỗi: " + e);
        }
    }

    public DichVuDTO getDichVuById(int id){
        DichVuDTO dichVu = null;
        String sql = "SELECT * FROM DichVuFB WHERE MaDV = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    dichVu = new DichVuDTO();
                    dichVu.setMa(id);
                    dichVu.setTen(rs.getString("TenDV"));
                    dichVu.setLoai(LoaiDichVu.valueOf(rs.getString("Loai")));
                    dichVu.setGia(rs.getDouble("Gia"));
                    dichVu.setTonKho(rs.getInt("TonKho"));
                    dichVu.setNhapHang(rs.getInt("NhapHang"));
                }
            }catch(Exception e){
                System.out.println("Lỗi: " + e);
            }
        }catch(Exception e){
            System.out.println("Lỗi: " + e);
        }

        return dichVu;
    }

    public List<DichVuDTO> getAllDichVu(){
        List<DichVuDTO> dsDichVu = new ArrayList<>();
        String sql = "SELECT * FROM DichVuFB";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                DichVuDTO dichVu = new DichVuDTO();

                dichVu.setMa(rs.getInt("MaDV"));
                dichVu.setTen(rs.getString("TenDV"));
                dichVu.setLoai(LoaiDichVu.valueOf(rs.getString("Loai")));
                dichVu.setGia(rs.getDouble("Gia"));
                dichVu.setTonKho(rs.getInt("TonKho"));
                dichVu.setNhapHang(rs.getInt("NhapHang"));

                dsDichVu.add(dichVu);
            }
        }catch(Exception e){
            System.out.println("Lỗi: " + e);
        }

        return dsDichVu;
    }

    public void updDichVu(DichVuDTO dichVu){
        String sql = "UPDATE DichVuFB SET TenDV = ?, Loai = ?, Gia = ?, TonKho = ?, NhapHang = ? WHERE MaDV = ?";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, dichVu.getTen());
            ps.setString(2, dichVu.getLoai().name());
            ps.setDouble(3, dichVu.getGia());
            ps.setInt(4, dichVu.getTonKho());
            ps.setInt(5, dichVu.getNhapHang());
            ps.setInt(6, dichVu.getMa());

            ps.executeUpdate();
        }catch(Exception e){
            System.out.println("Lỗi: " + e);
        }
    }

    public void delDichVu(int id){
        String sql = "DELETE FROM DichVuFB WHERE MaDV = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            ps.executeUpdate();
        }catch(Exception e){
            System.out.println("Lỗi: " + e);
        }
    }
}
