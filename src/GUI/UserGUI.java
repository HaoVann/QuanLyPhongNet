package GUI;

import BLL.MayTinhBLL;
import BLL.QuanLyVanhHanhBLL;
import BLL.TaiKhoanBLL;
import DTO.TaiKhoanDTO;
import DAL.NhatKyThueDAL; // Cần để lấy mã phiên chơi để gọi món
import DTO.MayTinhDTO;
import DTO.NhatKyThueDTO;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class UserGUI extends JFrame implements ActionListener {

    private Timer playTimer;
    private double giaMoiPhut = 100.0; // Mặc định 6000đ/giờ -> 100đ/phút
    private JLabel lb_timeRemaining, lb_du;
    private TaiKhoanDTO account;
    private TaiKhoanBLL tkBLL = new TaiKhoanBLL(); // Dùng để cập nhật số dư
    private NhatKyThueDAL nkDAL = new NhatKyThueDAL();
    private JButton btn_doiMK, btn_logout, btn_order, btn_recharge;
    private MayTinhBLL mtBLL = new MayTinhBLL();
    private QuanLyVanhHanhBLL vhBLL = new QuanLyVanhHanhBLL();
    private String maMayHienTai; // LƯU LẠI MÁY ĐỂ LÚC ĐĂNG XUẤT BIẾT ĐƯỜNG TRẢ

   public UserGUI(TaiKhoanDTO account) {
        this.account = account;

        // 1. DỌN DẸP BÓNG MA PHIÊN CŨ (Nếu khách từng tắt ngang app)
        NhatKyThueDTO nk = nkDAL.getPhienDangThueCuaTaiKhoan(account.getId()); 
        if (nk != null) {
            // Ép kết thúc phiên cũ đang bị treo, trả máy đó về màu Xanh
            vhBLL.thanhToan(nk.getMaMay()); 
        }

        // 2. 100% BẮT BUỘC CHỌN MÁY MỚI KHI ĐĂNG NHẬP
        java.util.List<MayTinhDTO> dsMay = mtBLL.layDanhSachMayTinh();
        JComboBox<String> cbMay = new JComboBox<>();
        for (MayTinhDTO m : dsMay) {
            if ("TRONG".equals(m.getTrangThai())) {
                cbMay.addItem(m.getMaMay() + " - " + m.getTenMay());
            }
        }

        if (cbMay.getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, "Hiện tại không còn máy nào trống!");
            System.exit(0);
        }

        int result = JOptionPane.showConfirmDialog(null, cbMay, "Vui lòng chọn máy để ngồi", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String selected = (String) cbMay.getSelectedItem();
            maMayHienTai = selected.split(" - ")[0]; // Lưu lại máy khách vừa chọn
            
            vhBLL.moMayChoKhach(maMayHienTai, account.getTenDangNhap());
        } else {
            System.exit(0);
        }

        setTitle("Máy Trạm - Người dùng: " + account.getTenDangNhap());
        setSize(550, 450);
        setLocationRelativeTo(null);
        initComponents();
        startTimer();
    }

    public void initComponents() {
        // ── Bảng thông tin ────────────────────────────────
        JPanel pn_info = new JPanel(new GridLayout(6, 2, 8, 10));
        pn_info.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Thông tin phiên chơi"));

        pn_info.add(new JLabel("Mã tài khoản:"));
        pn_info.add(makeValueLabel(String.valueOf(account.getId())));

        pn_info.add(new JLabel("Tên khách hàng:"));
        pn_info.add(makeValueLabel(account.getTenDangNhap()));

        pn_info.add(new JLabel("Số dư hiện tại:"));
        lb_du = makeValueLabel(Math.round(account.getSoDu()) + " VNĐ");
        pn_info.add(lb_du);

        // Label hiển thị thời gian còn lại (Phần mới)
        pn_info.add(new JLabel("Thời gian còn lại:"));
        lb_timeRemaining = new JLabel("Đang tính toán...", SwingConstants.RIGHT);
        lb_timeRemaining.setFont(new Font("Arial", Font.BOLD, 14));
        lb_timeRemaining.setForeground(Color.RED);
        pn_info.add(lb_timeRemaining);

        // ── Nút chức năng phía dưới ───────────────────────
        JPanel pn_bottom = new JPanel(new GridLayout(1, 4, 10, 10));
        pn_bottom.setBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        btn_recharge = new JButton("Nạp Tiền");
        btn_order    = new JButton("Gọi Đồ Ăn");
        btn_doiMK    = new JButton("Đổi mật khẩu");
        btn_logout   = new JButton("Đăng xuất");

        btn_recharge.addActionListener(this);
        btn_order.addActionListener(this);
        btn_doiMK.addActionListener(this);
        btn_logout.addActionListener(this);

        pn_bottom.add(btn_recharge);
        pn_bottom.add(btn_order);
        pn_bottom.add(btn_doiMK);
        pn_bottom.add(btn_logout);

        JPanel pn_main = new JPanel(new BorderLayout(8, 8));
        pn_main.setBorder(new EmptyBorder(10, 12, 4, 12));
        pn_main.add(pn_info,   BorderLayout.CENTER);
        pn_main.add(pn_bottom, BorderLayout.SOUTH);
        add(pn_main);
    }

    private JLabel makeValueLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.RIGHT);
        l.setFont(new Font("Arial", Font.PLAIN, 14));
        return l;
    }

    private void startTimer() {
        // Cập nhật giao diện lần đầu
        updateStatus();

        // Chạy Timer mỗi 60 giây (1 phút)
        playTimer = new Timer(60000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (account.getSoDu() >= giaMoiPhut) {
                    // Trừ tiền trong đối tượng và DB
                    account.setSoDu(account.getSoDu() - giaMoiPhut);
                    tkBLL.editAccount(account); // Cập nhật xuống Database
                    
                    updateStatus();
                } else {
                    playTimer.stop();
                    JOptionPane.showMessageDialog(null, "Tài khoản của bạn đã hết tiền. Máy sẽ tự động đăng xuất!");
                    dangXuat();
                }
            }
        });
        playTimer.start();
    }

    private void updateStatus() {
        // Cập nhật số dư trên UI
        lb_du.setText(Math.round(account.getSoDu()) + " VNĐ");

        // Tính thời gian còn lại (phút)
        int phutConLai = (int) (account.getSoDu() / giaMoiPhut);
        int hours = phutConLai / 60;
        int mins = phutConLai % 60;
        lb_timeRemaining.setText(String.format("%02d:%02d", hours, mins));
    }

    private void dangXuat() {
        // 1. Dừng đồng hồ trừ tiền
        if (playTimer != null) playTimer.stop();
        
        // 2. Báo cho Admin & Database là khách đã trả máy
        if (maMayHienTai != null && !maMayHienTai.isEmpty()) {
            vhBLL.thanhToan(maMayHienTai); 
        }

        // 3. Đóng cửa sổ User và quay lại màn hình Đăng Nhập
        dispose();
        new DangNhapGUI().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_recharge) {
            // Mở form nạp tiền
            new NapTienGUI(this, account).setVisible(true);
            updateStatus(); // Cập nhật lại UI sau khi nạp
        }

        if (e.getSource() == btn_order) {
            // Lấy mã phiên thuê hiện tại để gọi món
            // Lưu ý: Dashboard cần mở máy trước thì mới có mã thuê
            NhatKyThueDTO nk = nkDAL.getPhienDangThueCuaTaiKhoan(account.getId());
            if (nk != null) {
                new OrderDichVuGUI(this, nk.getMaThue()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy phiên chơi hợp lệ để gọi món!");
            }
        }

        if (e.getSource() == btn_doiMK) {
            new DoiMatKhauGUI(this, account).setVisible(true);
        }

        if (e.getSource() == btn_logout) {
            int c = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                dangXuat();
            }
        }
    }
}