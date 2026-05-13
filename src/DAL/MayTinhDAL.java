package DAL;

import DTO.MayTinhDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MayTinhDAL {
    // Lấy toàn bộ danh sách máy
    public List<MayTinhDTO> layDanhSachMayTinh() {
        List<MayTinhDTO> dsMayTinh = new ArrayList<>();
        String sql = "SELECT * FROM MayTinh";
        // Dùng try-with-resources để tự động đóng kết nối
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                dsMayTinh.add(new MayTinhDTO(
                    rs.getString("MaMay"),
                    rs.getString("TenMay"),
                    rs.getString("TrangThai"),
                    rs.getDouble("GiaTheoGio")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsMayTinh;
    }

    // Cập nhật trạng thái máy (Quan trọng cho phần Vận hành của Hào)
    public boolean capNhatTrangThai(String maMay, String trangThaiMoi) {
        String sql = "UPDATE MayTinh SET TrangThai = ? WHERE MaMay = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThaiMoi);
            ps.setString(2, maMay);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
