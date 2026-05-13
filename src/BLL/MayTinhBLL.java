package BLL;

import DAL.MayTinhDAL;
import DTO.MayTinhDTO;
import java.util.List;

public class MayTinhBLL {
    private MayTinhDAL mtDAL = new MayTinhDAL();

    public List<MayTinhDTO> layDanhSachMayTinh() {
        return mtDAL.layDanhSachMayTinh(); //
    }

    public boolean doiTrangThai(String maMay, String trangThaiMoi) {
        return mtDAL.capNhatTrangThai(maMay, trangThaiMoi);
    }
}
