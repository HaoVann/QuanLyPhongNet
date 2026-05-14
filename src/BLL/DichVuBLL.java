package BLL;

import DTO.DichVuDTO;
import DTO.LoaiDichVu;

import DAL.DichVuDAL;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DichVuBLL {
    private DichVuDAL dal = new DichVuDAL();

    public List<DichVuDTO> getAllDichVu(){
        return dal.getAllDichVu();
    }

    public DichVuDTO getDichVuById(int id){
        return dal.getDichVuById(id);
    }

    public void dichVuCheck(DichVuDTO dichVu) throws Exception{
        StringBuilder err = new StringBuilder("Lỗi: \n");
        boolean hasError = false;

        if(dichVu.getTen() == null || dichVu.getTen().trim().isEmpty()){
            hasError = true;
            err.append("- Tên không được bỏ trống \n");
        }

        if(dichVu.getGia() < 0){
            hasError = true;
            err.append("- Mệnh giá không được âm \n");
        }

        if(dichVu.getTonKho() < 0){
            hasError = true;
            err.append("- Hàng trong kho không được âm \n");
        }

        if(dichVu.getNhapHang() < 0){
            hasError = true;
            err.append("- Hàng đang nhập không được âm \n");
        }
        
        if(hasError == true){
            throw new Exception(err.toString());
        }
    }

    public String addDichVu(DichVuDTO dichVu){
        try {
            dichVuCheck(dichVu);
            dal.addDichVu(dichVu);
            return "Thêm dịch vụ mới thành công";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String updDichVu(DichVuDTO dichVu){
        try {
            dichVuCheck(dichVu);
            dal.updDichVu(dichVu);
            return "Sửa dịch vụ thành công";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String delDichVu(DichVuDTO dichVu){
        try {
            dal.delDichVu(dichVu.getMa());
            return "Xóa dịch vụ thành công";
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    public List<DichVuDTO> sortGanHet(List<DichVuDTO> list){
        return list.stream().sorted(Comparator.comparingInt(DichVuDTO::getTonKho).thenComparingInt(DichVuDTO::getNhapHang)).collect(Collectors.toList());
    }

    public List<DichVuDTO> findByNameAndType(String name, LoaiDichVu loai, List<DichVuDTO> list){
        return list.stream().filter(dv -> dv.getTen().toLowerCase().contains(name.toLowerCase()))
                            .filter(dv -> loai == null || dv.getLoai() == loai)
                            .collect(Collectors.toList());
    }
}
