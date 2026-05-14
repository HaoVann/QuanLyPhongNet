package GUI;

import DTO.TaiKhoanDTO;
import javax.swing.*;
import java.awt.*;

public class AdminMenuGUI extends JFrame {
    public AdminMenuGUI(TaiKhoanDTO admin) {
        setTitle("Bảng Điều Khiển Trung Tâm - Admin: " + admin.getTenDangNhap());
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel pnMain = new JPanel(new GridLayout(3, 1, 10, 10));
        pnMain.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btn1 = new JButton("1. Sơ đồ phòng máy (Hào)");
        JButton btn2 = new JButton("2. Quản lý Dịch vụ (Duy)");
        JButton btn3 = new JButton("3. Quản lý Hội viên (Hùng)");

        // Liên kết đến Dashboard của bạn
        btn1.addActionListener(e -> new DashboardGUI().setVisible(true));
        
        // Liên kết đến Menu của Duy
        btn2.addActionListener(e -> new DichVuMainGUI().setVisible(true));
        
        // Liên kết đến Giao diện của Hùng (đã đổi tên)
        btn3.addActionListener(e -> new QuanLyTaiKhoanGUI(admin).setVisible(true));

        pnMain.add(btn1); pnMain.add(btn2); pnMain.add(btn3);
        add(pnMain);
    }
}