package GUI;

import BLL.MayTinhBLL;
import BLL.QuanLyVanhHanhBLL;
import DTO.MayTinhDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DashboardGUI extends JFrame {
    private MayTinhBLL mtBLL = new MayTinhBLL();
    private QuanLyVanhHanhBLL vanHanhBLL = new QuanLyVanhHanhBLL();
    private JPanel pn_SoDo; // Panel chứa các nút máy tính

    public DashboardGUI() {
        setTitle("Dashboard Quản Lý Quán Net");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Khu vực Tiêu đề và Nút Làm mới (Đã tích hợp mã mới)
        JPanel pn_Top = new JPanel(new BorderLayout()); // Panel gom chung để đặt lên NORTH
        
        JLabel lblTitle = new JLabel("SƠ ĐỒ PHÒNG MÁY", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        pn_Top.add(lblTitle, BorderLayout.CENTER);

        // Tạo thanh công cụ chứa nút Refresh và đẩy sang góc phải
        JPanel pnTool = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton btnRefresh = new JButton("Làm mới (Refresh)");
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 13));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Sự kiện khi bấm nút Refresh
        btnRefresh.addActionListener(e -> {
            veSoDoMayTinh(); // Cập nhật lại giao diện
            JOptionPane.showMessageDialog(this, "Đã cập nhật tình trạng phòng máy mới nhất!");
        });

        pnTool.add(btnRefresh);
        pn_Top.add(pnTool, BorderLayout.EAST); // Gắn thanh công cụ vào bên phải của vùng Top

        add(pn_Top, BorderLayout.NORTH); // Đưa toàn bộ cụm Tiêu đề + Nút lên đầu JFrame

        // 2. Khu vực Sơ đồ máy tính
        pn_SoDo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        JScrollPane scrollPane = new JScrollPane(pn_SoDo);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách Máy Trạm"));
        add(scrollPane, BorderLayout.CENTER);

        // 3. Khu vực Chú thích màu sắc ở dưới cùng
        JPanel pn_ChuThich = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        JLabel lblXanh = new JLabel("■ Máy Trống (Xanh)");
        lblXanh.setForeground(new Color(46, 204, 113));
        lblXanh.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel lblDo = new JLabel("■ Đang Chơi (Đỏ)");
        lblDo.setForeground(new Color(231, 76, 60));
        lblDo.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel lblXam = new JLabel("■ Bảo Trì (Xám)");
        lblXam.setForeground(Color.GRAY);
        lblXam.setFont(new Font("Arial", Font.BOLD, 14));

        pn_ChuThich.add(lblXanh);
        pn_ChuThich.add(lblDo);
        pn_ChuThich.add(lblXam);
        add(pn_ChuThich, BorderLayout.SOUTH);

        // Gọi hàm vẽ các nút máy tính lên màn hình
        veSoDoMayTinh();
    }

    // Hàm load dữ liệu từ Database và vẽ ra giao diện
    private void veSoDoMayTinh() {
        pn_SoDo.removeAll(); 
        
        List<MayTinhDTO> dsMay = mtBLL.layDanhSachMayTinh();

        for (MayTinhDTO mt : dsMay) {
            JButton btnMay = new JButton("<html><center>" + mt.getMaMay() + "<br/>" + mt.getTenMay() + "</center></html>");
            btnMay.setPreferredSize(new Dimension(140, 100)); 
            btnMay.setFont(new Font("Arial", Font.BOLD, 16));
            btnMay.setFocusPainted(false);
            btnMay.setCursor(new Cursor(Cursor.HAND_CURSOR));

            switch (mt.getTrangThai()) {
                case "TRONG":
                    btnMay.setBackground(new Color(46, 204, 113)); 
                    btnMay.setForeground(Color.WHITE);
                    break;
                case "DANG_CHOI":
                    btnMay.setBackground(new Color(231, 76, 60)); 
                    btnMay.setForeground(Color.WHITE);
                    break;
                default: 
                    btnMay.setBackground(Color.GRAY); 
                    btnMay.setForeground(Color.WHITE);
                    break;
            }

            btnMay.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    xuLyClickMayTinh(mt);
                }
            });

            pn_SoDo.add(btnMay); 
        }
        
        pn_SoDo.revalidate();
        pn_SoDo.repaint();
    }

    private void xuLyClickMayTinh(MayTinhDTO mt) {
        if (mt.getTrangThai().equals("TRONG")) {
            String tenTK = JOptionPane.showInputDialog(this, 
                "Nhập Tên đăng nhập hội viên\n(Để trống nếu là Khách vãng lai):", 
                "Mở Máy " + mt.getMaMay(), 
                JOptionPane.QUESTION_MESSAGE);
                
            if (tenTK != null) { 
                String ketQua = vanHanhBLL.moMayChoKhach(mt.getMaMay(), tenTK.trim());
                JOptionPane.showMessageDialog(this, ketQua);
                veSoDoMayTinh(); 
            }
            
        } else if (mt.getTrangThai().equals("DANG_CHOI")) {
            new ThanhToanGUI(this, mt.getMaMay()).setVisible(true);
            veSoDoMayTinh();
            
        } else {
            JOptionPane.showMessageDialog(this, "Máy đang bảo trì, không thể thao tác!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardGUI().setVisible(true));
    }
}