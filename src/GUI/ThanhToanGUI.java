package GUI;

import BLL.QuanLyVanhHanhBLL;
import DAL.ChiTietGoiDichVuDAL;
import DAL.DichVuDAL;
import DAL.NhatKyThueDAL;
import DTO.ChiTietGoiDichVuDTO;
import DTO.DichVuDTO;
import DTO.NhatKyThueDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class ThanhToanGUI extends JDialog {
    private String maMay;
    private DashboardGUI parentDashboard; // Để gọi hàm refresh sơ đồ máy sau khi thanh toán
    private QuanLyVanhHanhBLL vanHanhBLL = new QuanLyVanhHanhBLL();
    
    // Khai báo các đối tượng để lấy dữ liệu in Bill
    private NhatKyThueDAL nkDAL = new NhatKyThueDAL();
    private ChiTietGoiDichVuDAL ctDAL = new ChiTietGoiDichVuDAL();
    private DichVuDAL dvDAL = new DichVuDAL(); // Dùng code của Duy để lấy tên món ăn

    public ThanhToanGUI(DashboardGUI parent, String maMay) {
        super(parent, "Hóa Đơn Thanh Toán", true); // true = Modal (bắt buộc thao tác xong mới quay lại được)
        this.maMay = maMay;
        this.parentDashboard = parent;

        setSize(450, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadDataLenBill();
    }

    private void initComponents() {
        // --- PHẦN HEADER: THÔNG TIN PHIÊN CHƠI ---
        JPanel pnTop = new JPanel(new GridLayout(4, 1, 5, 5));
        pnTop.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblTitle = new JLabel("HÓA ĐƠN MÁY " + maMay, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        pnTop.add(lblTitle);

        pnTop.add(new JLabel("Tài khoản: " + layTenTaiKhoan()));
        pnTop.add(new JLabel("Thời gian bắt đầu: " + layGioBatDau()));
        
        add(pnTop, BorderLayout.NORTH);

        // --- PHẦN GIỮA: BẢNG DANH SÁCH ĐỒ ĂN/THỨC UỐNG ---
        String[] columns = {"Tên món", "SL", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable tbDichVu = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tbDichVu);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết dịch vụ đã gọi"));
        
        // Đổ dữ liệu vào bảng
        NhatKyThueDTO phienHienTai = nkDAL.getPhienDangThue(maMay);
        if (phienHienTai != null) {
            List<ChiTietGoiDichVuDTO> listCT = ctDAL.getDichVuTheoPhien(phienHienTai.getMaThue());
            for (ChiTietGoiDichVuDTO ct : listCT) {
                DichVuDTO dv = dvDAL.getDichVuById(ct.getMaDV()); // Lấy tên món từ module của Duy
                String tenMon = (dv != null) ? dv.getTen() : "Dịch vụ không rõ";
                model.addRow(new Object[]{tenMon, ct.getSoLuong(), Math.round(ct.getThanhTien()) + "đ"});
            }
        }
        add(scrollPane, BorderLayout.CENTER);

        // --- PHẦN BOTTOM: TỔNG TIỀN VÀ NÚT BẤM ---
        JPanel pnBottom = new JPanel(new BorderLayout());
        pnBottom.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Tính toán tiền
        double tienMay = vanHanhBLL.tinhTienMay(maMay);
        double tienDV = vanHanhBLL.tinhTienDichVu(maMay);
        double tongTien = tienMay + tienDV;

        JPanel pnTien = new JPanel(new GridLayout(3, 1));
        pnTien.add(new JLabel("Tiền giờ chơi: " + Math.round(tienMay) + " VNĐ"));
        pnTien.add(new JLabel("Tiền dịch vụ: " + Math.round(tienDV) + " VNĐ"));
        
        JLabel lblTongTien = new JLabel("TỔNG CỘNG: " + Math.round(tongTien) + " VNĐ");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongTien.setForeground(Color.RED);
        pnTien.add(lblTongTien);
        
        pnBottom.add(pnTien, BorderLayout.CENTER);

        // Nút hành động
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnThanhToan = new JButton("Xác nhận Thu tiền");
        btnThanhToan.setBackground(new Color(46, 204, 113));
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnHuy = new JButton("Đóng");

        pnButtons.add(btnThanhToan);
        pnButtons.add(btnHuy);
        pnBottom.add(pnButtons, BorderLayout.EAST);

        add(pnBottom, BorderLayout.SOUTH);

        // --- SỰ KIỆN NÚT BẤM ---
        btnHuy.addActionListener(e -> dispose()); // Đóng cửa sổ

        btnThanhToan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Gọi BLL để lưu Database và cập nhật trạng thái máy
                String ketQua = vanHanhBLL.thanhToan(maMay);
                JOptionPane.showMessageDialog(ThanhToanGUI.this, ketQua);
                
                // Refresh lại sơ đồ bên Dashboard (để máy đổi từ Đỏ sang Xanh)
                // Phải viết thêm hàm refreshSoDo() bên DashboardGUI
                dispose(); 
            }
        });
    }

    private void loadDataLenBill() {} // Đã gộp logic vào initComponents cho gọn

    // Hàm phụ trợ lấy thông tin
    private String layGioBatDau() {
        NhatKyThueDTO phien = nkDAL.getPhienDangThue(maMay);
        if (phien != null && phien.getThoiGianBatDau() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return sdf.format(phien.getThoiGianBatDau());
        }
        return "N/A";
    }

    private String layTenTaiKhoan() {
        NhatKyThueDTO phien = nkDAL.getPhienDangThue(maMay);
        // Ở đây nếu có thời gian, bạn gọi TaiKhoanDAL để lấy Tên thay vì hiển thị ID
        return (phien != null) ? "Khách hàng ID-" + phien.getMaTK() : "Khách vãng lai";
    }
}
