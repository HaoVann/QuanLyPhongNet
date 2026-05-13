package DAL;

import DTO.ChiTietGoiDichVuDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietGoiDichVuDAL {

    // 1. Khách order đồ ăn/thức uống
    public boolean goiMon(ChiTietGoiDichVuDTO chiTiet) {
        String sql = "INSERT INTO ChiTietGoiDichVu (MaThue, MaDV, SoLuong, ThanhTien) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, chiTiet.getMaThue());
            ps.setInt(2, chiTiet.getMaDV());
            ps.setInt(3, chiTiet.getSoLuong());
            ps.setDouble(4, chiTiet.getThanhTien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    // 2. Lấy danh sách đồ ăn khách đã gọi (để in lên Bill)
    public List<ChiTietGoiDichVuDTO> getDichVuTheoPhien(int maThue) {
        List<ChiTietGoiDichVuDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietGoiDichVu WHERE MaThue = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maThue);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietGoiDichVuDTO ct = new ChiTietGoiDichVuDTO();
                ct.setMaThue(rs.getInt("MaThue"));
                ct.setMaDV(rs.getInt("MaDV"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                list.add(ct);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
