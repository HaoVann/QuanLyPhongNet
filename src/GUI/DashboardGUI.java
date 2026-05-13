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

        // 1. Khu vực Tiêu đề
        JLabel lblTitle = new JLabel("SƠ ĐỒ PHÒNG MÁY", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // 2. Khu vực Sơ đồ máy tính (Dùng FlowLayout để các máy tự xếp nối tiếp nhau)
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
        pn_SoDo.removeAll(); // Xóa sạch sơ đồ cũ để vẽ lại (quan trọng khi refresh)
        
        List<MayTinhDTO> dsMay = mtBLL.layDanhSachMayTinh();

        for (MayTinhDTO mt : dsMay) {
            // Tạo 1 nút bấm đại diện cho 1 máy
            JButton btnMay = new JButton("<html><center>" + mt.getMaMay() + "<br/>" + mt.getTenMay() + "</center></html>");
            btnMay.setPreferredSize(new Dimension(140, 100)); // Kích thước nút
            btnMay.setFont(new Font("Arial", Font.BOLD, 16));
            btnMay.setFocusPainted(false);
            btnMay.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Đổi màu tùy theo trạng thái máy
            switch (mt.getTrangThai()) {
                case "TRONG":
                    btnMay.setBackground(new Color(46, 204, 113)); // Xanh lá
                    btnMay.setForeground(Color.WHITE);
                    break;
                case "DANG_CHOI":
                    btnMay.setBackground(new Color(231, 76, 60)); // Đỏ
                    btnMay.setForeground(Color.WHITE);
                    break;
                default: // BAO_TRI
                    btnMay.setBackground(Color.GRAY); // Xám
                    btnMay.setForeground(Color.WHITE);
                    break;
            }

            // Bắt sự kiện khi thu ngân click vào 1 máy bất kỳ
            btnMay.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    xuLyClickMayTinh(mt);
                }
            });

            pn_SoDo.add(btnMay); // Thêm nút vào Panel
        }
        
        // Cập nhật lại giao diện sau khi vẽ xong
        pn_SoDo.revalidate();
        pn_SoDo.repaint();
    }

    // Hàm xử lý logic khi click vào máy
    private void xuLyClickMayTinh(MayTinhDTO mt) {
        if (mt.getTrangThai().equals("TRONG")) {
            // 1. NẾU MÁY TRỐNG -> Yêu cầu mở máy
            String tenTK = JOptionPane.showInputDialog(this, 
                "Nhập Tên đăng nhập hội viên\n(Để trống nếu là Khách vãng lai):", 
                "Mở Máy " + mt.getMaMay(), 
                JOptionPane.QUESTION_MESSAGE);
                
            // Nếu người dùng không bấm Cancel
            if (tenTK != null) { 
                String ketQua = vanHanhBLL.moMayChoKhach(mt.getMaMay(), tenTK.trim());
                JOptionPane.showMessageDialog(this, ketQua);
                veSoDoMayTinh(); // Reload lại sơ đồ để máy chuyển sang màu Đỏ
            }
            
        } else if (mt.getTrangThai().equals("DANG_CHOI")) {
            // Mở Giao diện Thanh toán chuẩn
            new ThanhToanGUI(this, mt.getMaMay()).setVisible(true);
            
            // Sau khi thanh toán xong (cửa sổ Dialog đóng lại), tiến hành vẽ lại sơ đồ
            veSoDoMayTinh();
            
        } else {
            // 3. NẾU MÁY BẢO TRÌ
            JOptionPane.showMessageDialog(this, "Máy đang bảo trì, không thể thao tác!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardGUI().setVisible(true));
    }
}
