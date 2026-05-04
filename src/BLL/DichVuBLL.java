package BLL;

import DTO.DichVuDTO;
import DTO.LoaiDichVu;

import DAL.DichVuDAL;

import java.util.ArrayList;
import java.util.List;

public class DichVuBLL {
    private DichVuDAL dal = new DichVuDAL();

    public List<DichVuDTO> getAllDichVu(){
        return dal.getAllDichVu();
    }

    public void addDichVu(DichVuDTO dichVu){
        
    }
}
