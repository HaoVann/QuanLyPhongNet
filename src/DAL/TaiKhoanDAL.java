package DAL;

import DTO.TaiKhoanDTO;
import java.sql.*;
import java.util.ArrayList;

public class TaiKhoanDAL {
    public TaiKhoanDTO checkLogin(String user, String pass) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ?";
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, user); pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return new TaiKhoanDTO(rs.getInt("id"), rs.getString("tenDangNhap"), rs.getString("matKhau"), rs.getString("vaiTro"), rs.getDouble("soDu"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public ArrayList<TaiKhoanDTO> getAllAccounts() {
        ArrayList<TaiKhoanDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan";
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new TaiKhoanDTO(rs.getInt("id"), rs.getString("tenDangNhap"), rs.getString("matKhau"), rs.getString("vaiTro"), rs.getDouble("soDu")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(TaiKhoanDTO tk) {
        String sql = "INSERT INTO TaiKhoan (tenDangNhap, matKhau, vaiTro, soDu) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, tk.getTenDangNhap()); pst.setString(2, tk.getMatKhau());
            pst.setString(3, tk.getVaiTro()); pst.setDouble(4, tk.getSoDu());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean update(TaiKhoanDTO tk) {
        String sql = "UPDATE TaiKhoan SET matKhau = ?, vaiTro = ?, soDu = ? WHERE tenDangNhap = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, tk.getMatKhau()); pst.setString(2, tk.getVaiTro());
            pst.setDouble(3, tk.getSoDu()); pst.setString(4, tk.getTenDangNhap());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean delete(String user) {
        String sql = "DELETE FROM TaiKhoan WHERE tenDangNhap = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, user); return pst.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}