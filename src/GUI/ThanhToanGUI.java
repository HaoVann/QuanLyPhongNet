package GUI;

import BLL.QuanLyVanhHanhBLL;
import DAL.ChiTietGoiDichVuDAL;
import DAL.DBConnection;
import DAL.DichVuDAL;
import DAL.NhatKyThueDAL;
import DAL.TaiKhoanDAL;
import DTO.ChiTietGoiDichVuDTO;
import DTO.DichVuDTO;
import DTO.NhatKyThueDTO;
import DTO.TaiKhoanDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ThanhToanGUI extends JDialog {
    private String maMay;
    private DashboardGUI parentDashboard;
    private QuanLyVanhHanhBLL vanHanhBLL = new QuanLyVanhHanhBLL();
    private NhatKyThueDAL nkDAL = new NhatKyThueDAL();
    private ChiTietGoiDichVuDAL ctDAL = new ChiTietGoiDichVuDAL();
    private DichVuDAL dvDAL = new DichVuDAL();
    private TaiKhoanDAL tkDAL = new TaiKhoanDAL();

    public ThanhToanGUI(DashboardGUI parent, String maMay) {
        super(parent, "Chi Tiết Máy Đang Chạy - " + maMay, true);
        this.maMay = maMay;
        this.parentDashboard = parent;

        setSize(500, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        initComponents();
    }

    private void initComponents() {
        NhatKyThueDTO phien = nkDAL.getPhienDangThue(maMay);
        TaiKhoanDTO tk = (phien != null) ? tkDAL.getAccountById(phien.getMaTK()) : null;

        // --- PHẦN 1: THÔNG TIN KHÁCH HÀNG & TRẠNG THÁI ---
        JPanel pnInfo = new JPanel(new GridLayout(4, 1, 5, 5));
        pnInfo.setBorder(BorderFactory.createTitledBorder("Thông tin phiên chơi"));
        
        pnInfo.add(new JLabel(" Tên khách hàng: " + (tk != null ? tk.getTenDangNhap() : "Khách vãng lai")));
        pnInfo.add(new JLabel(" Giờ bắt đầu: " + (phien != null ? new SimpleDateFormat("HH:mm:ss").format(phien.getThoiGianBatDau()) : "N/A")));
        
        JLabel lbSoDu = new JLabel(" Số dư còn lại: " + (tk != null ? Math.round(tk.getSoDu()) : 0) + " VNĐ");
        lbSoDu.setFont(new Font("Arial", Font.BOLD, 14));
        lbSoDu.setForeground(new Color(0, 128, 0));
        pnInfo.add(lbSoDu);

        add(pnInfo, BorderLayout.NORTH);

        // --- PHẦN 2: DANH SÁCH ORDER (Yêu cầu dịch vụ) ---
        String[] columns = {"Tên món", "Số lượng", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable tbOrder = new JTable(model);
        
        if (phien != null) {
            List<ChiTietGoiDichVuDTO> list = ctDAL.getDichVuTheoPhien(phien.getMaThue());
            for (ChiTietGoiDichVuDTO ct : list) {
                DichVuDTO dv = dvDAL.getDichVuById(ct.getMaDV());
                model.addRow(new Object[]{
                    (dv != null ? dv.getTen() : "Không rõ"),
                    ct.getSoLuong(),
                    Math.round(ct.getThanhTien()) + "đ"
                });
            }
        }
        
        JScrollPane sp = new JScrollPane(tbOrder);
        sp.setBorder(BorderFactory.createTitledBorder("Danh sách món đã gọi từ máy trạm"));
        add(sp, BorderLayout.CENTER);

        // --- PHẦN 3: TỔNG KẾT & NÚT BẤM ---
        JPanel pnBottom = new JPanel(new BorderLayout());
        pnBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        double tienDV = (phien != null) ? vanHanhBLL.tinhTienDichVu(maMay) : 0;
        JLabel lbTongTienDV = new JLabel("Tổng tiền dịch vụ chưa thu: " + Math.round(tienDV) + " VNĐ");
        lbTongTienDV.setFont(new Font("Arial", Font.BOLD, 15));
        lbTongTienDV.setForeground(Color.RED);
        pnBottom.add(lbTongTienDV, BorderLayout.NORTH);

        JPanel pnBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnKetThuc = new JButton("Thanh toán & Tắt máy");
        btnKetThuc.setBackground(new Color(231, 76, 60));
        btnKetThuc.setForeground(Color.WHITE);
        
        JButton btnDong = new JButton("Chỉ xem (Đóng)");

        pnBtn.add(btnKetThuc);
        pnBtn.add(btnDong);
        pnBottom.add(pnBtn, BorderLayout.SOUTH);

        add(pnBottom, BorderLayout.SOUTH);

        // Sự kiện
        btnDong.addActionListener(e -> dispose());
        
        btnKetThuc.addActionListener(e -> {
            int check = JOptionPane.showConfirmDialog(this, "Xác nhận khách đã trả tiền dịch vụ và muốn tắt máy?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (check == JOptionPane.YES_OPTION) {
                String res = vanHanhBLL.thanhToan(maMay);
                JOptionPane.showMessageDialog(this, res);
                dispose();
            }
        });
    }
    
}