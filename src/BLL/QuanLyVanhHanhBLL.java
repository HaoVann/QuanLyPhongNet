package BLL;

import DAL.NhatKyThueDAL;
import DAL.MayTinhDAL;
import DAL.ChiTietGoiDichVuDAL;
import DAL.TaiKhoanDAL; // Dùng code của Hùng
import DTO.NhatKyThueDTO;
import DTO.MayTinhDTO;
import DTO.ChiTietGoiDichVuDTO;
import DTO.TaiKhoanDTO;

import java.util.List;

public class QuanLyVanhHanhBLL {
    private NhatKyThueDAL nkDAL = new NhatKyThueDAL();
    private MayTinhDAL mtDAL = new MayTinhDAL();
    private ChiTietGoiDichVuDAL ctDAL = new ChiTietGoiDichVuDAL();
    private TaiKhoanDAL tkDAL = new TaiKhoanDAL();

    // ==========================================
    // 1. LOGIC MỞ MÁY
    // ==========================================
    public String moMayChoKhach(String maMay, String tenDangNhap) {
        // Kiểm tra xem người dùng nhập tài khoản có tồn tại không
        TaiKhoanDTO tk = null;
        if (tenDangNhap != null && !tenDangNhap.isEmpty()) {
            // Tạm dùng một cách để lấy MaTK từ tên đăng nhập (Dựa vào hàm getAll của Hùng)
            List<TaiKhoanDTO> listTK = tkDAL.getAllAccounts();
            for (TaiKhoanDTO t : listTK) {
                if (t.getTenDangNhap().equals(tenDangNhap)) {
                    tk = t;
                    break;
                }
            }
            if (tk == null) return "Lỗi: Tài khoản không tồn tại!";
            if (tk.getSoDu() <= 0) return "Lỗi: Tài khoản đã hết tiền, vui lòng nạp thêm!";
        }

        // Mặc định nếu không có tài khoản (Khách vãng lai) thì dùng ID = 4 (như dữ liệu mẫu)
        int maTK = (tk != null) ? tk.getId() : 4; 

        NhatKyThueDTO nkMoi = new NhatKyThueDTO();
        nkMoi.setMaTK(maTK);
        nkMoi.setMaMay(maMay);

        if (nkDAL.moMay(nkMoi)) {
            return "Mở máy " + maMay + " thành công!";
        }
        return "Lỗi: Không thể mở máy trên Database!";
    }

    // ==========================================
    // 2. LOGIC TÍNH TIỀN (Dùng để hiển thị lên Dashboard khi click vào máy)
    // ==========================================
    
    // Tính tiền giờ chơi dựa trên (Giờ hiện tại - Giờ bắt đầu) * Giá máy
    public double tinhTienMay(String maMay) {
        NhatKyThueDTO phienHienTai = nkDAL.getPhienDangThue(maMay);
        if (phienHienTai == null) return 0;

        long gioBatDau = phienHienTai.getThoiGianBatDau().getTime();
        long gioHienTai = System.currentTimeMillis();
        
        // Đổi mili-giây sang số giờ (vd: 1.5 giờ)
        double soGioChoi = (double)(gioHienTai - gioBatDau) / (1000 * 60 * 60);

        // Lấy giá của máy tính đó
        double giaGio = 0;
        List<MayTinhDTO> dsMay = mtDAL.layDanhSachMayTinh();
        for (MayTinhDTO m : dsMay) {
            if (m.getMaMay().equals(maMay)) {
                giaGio = m.getGiaTheoGio();
                break;
            }
        }
        
        return soGioChoi * giaGio;
    }

    // Tính tổng tiền các món nước, mì tôm khách đã gọi
    public double tinhTienDichVu(String maMay) {
        NhatKyThueDTO phienHienTai = nkDAL.getPhienDangThue(maMay);
        if (phienHienTai == null) return 0;

        List<ChiTietGoiDichVuDTO> dsMon = ctDAL.getDichVuTheoPhien(phienHienTai.getMaThue());
        double tongTienDV = 0;
        for (ChiTietGoiDichVuDTO mon : dsMon) {
            tongTienDV += mon.getThanhTien();
        }
        return tongTienDV;
    }

    // ==========================================
    // 3. LOGIC THANH TOÁN & ĐÓNG MÁY
    // ==========================================
    public String thanhToan(String maMay) {
        NhatKyThueDTO phienHienTai = nkDAL.getPhienDangThue(maMay);
        if (phienHienTai == null) return "Lỗi: Máy này không có ai đang chơi!";

        double tienMay = tinhTienMay(maMay);
        double tienDichVu = tinhTienDichVu(maMay);
        double tongPhaiTra = tienMay + tienDichVu;

        // Lưu thông tin kết thúc vào Database
        if (nkDAL.ketThucPhien(phienHienTai.getMaThue(), tongPhaiTra)) {
            
            // Đổi trạng thái máy tính về TRONG
            // Gọi trực tiếp hàm cập nhật trạng thái đã viết ở DAL
        if (mtDAL.capNhatTrangThai(maMay, "TRONG")) {
            // Xóa phần thập phân cho đẹp
            long tienTron = Math.round(tongPhaiTra);
            return "Thanh toán thành công!\nTổng tiền: " + tienTron + " VNĐ";
        }
            
            // Xóa phần thập phân cho đẹp
            long tienTron = Math.round(tongPhaiTra);
            return "Thanh toán thành công!\nTổng tiền: " + tienTron + " VNĐ";
        }
        
        return "Lỗi: Không thể kết thúc phiên trên Database!";
    }
}
