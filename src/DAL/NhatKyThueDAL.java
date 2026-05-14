package DAL;

import DTO.NhatKyThueDTO;
import java.sql.*;

public class NhatKyThueDAL {
    
    // 1. Mở máy cho khách (Tạo một phiên thuê mới)
    public boolean moMay(NhatKyThueDTO nk) {
        String sql = "INSERT INTO NhatKyThue (MaTK, MaMay, ThoiGianBatDau, TrangThai) VALUES (?, ?, ?, 'DANG_THUE')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nk.getMaTK());
            ps.setString(2, nk.getMaMay());
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // Lấy giờ hệ thống hiện tại
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    // 2. Lấy thông tin phiên thuê ĐANG CHẠY của một máy tính (để phục vụ việc tính tiền)
    public NhatKyThueDTO getPhienDangThue(String maMay) {
        String sql = "SELECT * FROM NhatKyThue WHERE MaMay = ? AND TrangThai = 'DANG_THUE'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maMay);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NhatKyThueDTO nk = new NhatKyThueDTO();
                nk.setMaThue(rs.getInt("MaThue"));
                nk.setMaTK(rs.getInt("MaTK"));
                nk.setMaMay(rs.getString("MaMay"));
                nk.setThoiGianBatDau(rs.getTimestamp("ThoiGianBatDau"));
                return nk;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null; // Trả về null nếu máy đang trống
    }

    // 3. Kết thúc phiên thuê (Cập nhật giờ ra và tổng tiền)
    public boolean ketThucPhien(int maThue, double tongTien) {
        String sql = "UPDATE NhatKyThue SET ThoiGianKetThuc = ?, TongTien = ?, TrangThai = 'DA_THANH_TOAN' WHERE MaThue = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setDouble(2, tongTien);
            ps.setInt(3, maThue);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    
    // Lấy thông tin phiên thuê ĐANG CHẠY dựa vào Mã Tài Khoản của khách
    public NhatKyThueDTO getPhienDangThueCuaTaiKhoan(int maTK) {
        String sql = "SELECT * FROM NhatKyThue WHERE MaTK = ? AND TrangThai = 'DANG_THUE'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maTK);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NhatKyThueDTO nk = new NhatKyThueDTO();
                nk.setMaThue(rs.getInt("MaThue"));
                nk.setMaTK(rs.getInt("MaTK"));
                nk.setMaMay(rs.getString("MaMay"));
                nk.setThoiGianBatDau(rs.getTimestamp("ThoiGianBatDau"));
                return nk;
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return null; // Không tìm thấy máy khách đang ngồi
    }
}