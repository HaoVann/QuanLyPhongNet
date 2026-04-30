package BLL;

import DAL.MayTinhDAL;
import DTO.MayTinhDTO;
import java.util.List;

public class MayTinhBLL {
    private MayTinhDAL mayTinhDAL = new MayTinhDAL();

    public List<MayTinhDTO> layDanhSachMayTinh() {
        // Có thể thêm logic kiểm tra ở đây trước khi return
        return mayTinhDAL.layDanhSachMayTinh();
    }
}
