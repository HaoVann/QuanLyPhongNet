package DAL;

import DTO.MayTinhDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MayTinhDAL {
    public List<MayTinhDTO> layDanhSachMayTinh() {
        List<MayTinhDTO> dsMayTinh = new ArrayList<>();
        String sql = "SELECT * FROM MayTinh";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MayTinhDTO mt = new MayTinhDTO(
                    rs.getString("MaMay"),
                    rs.getString("TenMay"),
                    rs.getString("TrangThai"),
                    rs.getDouble("GiaTheoGio")
                );
                dsMayTinh.add(mt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsMayTinh;
    }
}
