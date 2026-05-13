package BLL;

import DAL.TaiKhoanDAL;
import DTO.TaiKhoanDTO;
import java.util.ArrayList;

public class TaiKhoanBLL {
    private TaiKhoanDAL tkDAL = new TaiKhoanDAL();

    public Object checkLogin(String user, String pass) {
        if (user.isEmpty() || pass.isEmpty()) return "Vui lòng nhập đầy đủ!";
        TaiKhoanDTO tk = tkDAL.checkLogin(user, pass);
        return (tk != null) ? tk : "Sai tài khoản hoặc mật khẩu!";
    }

    public String addAccount(TaiKhoanDTO tk) {
        if (tk.getTenDangNhap().isEmpty()) return "Tên không được trống!";
        return tkDAL.insert(tk) ? "Thêm thành công!" : "Tên đăng nhập đã tồn tại!";
    }

    public String editAccount(TaiKhoanDTO tk) {
        return tkDAL.update(tk) ? "Sửa thành công!" : "Lỗi khi sửa!";
    }

    public String deleteAccount(String username) {
        return tkDAL.delete(username) ? "Xóa thành công!" : "Lỗi khi xóa!";
    }

    public String napTien(String user, String moneyStr, double current) {
        try {
            double nap = Double.parseDouble(moneyStr);
            if (nap <= 0) return "Tiền nạp phải > 0!";
            TaiKhoanDTO tk = new TaiKhoanDTO();
            tk.setTenDangNhap(user); tk.setSoDu(current + nap);
            // Cập nhật lại mật khẩu và vai trò nếu cần, ở đây ta giả định chỉ update số dư
            return tkDAL.capNhatSoDu(user, current + nap) ? "success" : "Lỗi nạp tiền!";
        } catch (Exception e) { return "Số tiền không hợp lệ!"; }
    }

    public ArrayList<TaiKhoanDTO> getAllAccounts() { return tkDAL.getAllAccounts(); }
}