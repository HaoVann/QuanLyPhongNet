package GUI;


import DTO.TaiKhoanDTO;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class UserGUI extends JFrame implements ActionListener {

    
    private TaiKhoanDTO account;
    private JButton btn_doiMK, btn_logout;

    public UserGUI(TaiKhoanDTO account) {
        this.account = account;
        setTitle("Thông Tin Tài Khoản – " + account.getTenDangNhap());
        setSize(420, 360);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    public void initComponents() {
        // ── Bảng thông tin ────────────────────────────────
        JPanel pn_info = new JPanel(new GridLayout(5, 2, 8, 10));
        pn_info.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Thông tin tài khoản"));

        pn_info.add(new JLabel("Mã tài khoản:"));
        pn_info.add(makeValueLabel(String.valueOf(account.getId())));

        pn_info.add(new JLabel("Tên đăng nhập:"));
        pn_info.add(makeValueLabel(account.getTenDangNhap()));

        pn_info.add(new JLabel("Mật khẩu:"));
        pn_info.add(makeValueLabel("••••••••"));

        pn_info.add(new JLabel("Vai trò:"));
        pn_info.add(makeValueLabel(account.getVaiTro()));

        pn_info.add(new JLabel("Số dư:"));
        JLabel lb_sodu = makeValueLabel(String.format("%,.0f VNĐ", account.getSoDu()));
        lb_sodu.setForeground(new Color(0, 128, 0));
        pn_info.add(lb_sodu);

        // ── Bottom: nút ───────────────────────────────────
        JPanel pn_bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        pn_bottom.setBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        btn_doiMK  = new JButton("Đổi mật khẩu");
        btn_logout = new JButton("Đăng xuất");
        btn_doiMK.addActionListener(this);
        btn_logout.addActionListener(this);
        pn_bottom.add(btn_doiMK);
        pn_bottom.add(btn_logout);

        JPanel pn_main = new JPanel(new BorderLayout(8, 8));
        pn_main.setBorder(new EmptyBorder(10, 12, 4, 12));
        pn_main.add(pn_info,   BorderLayout.CENTER);
        pn_main.add(pn_bottom, BorderLayout.SOUTH);
        add(pn_main);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_doiMK) {
            new DoiMatKhauGUI(this, account);
        }

        if (e.getSource() == btn_logout) {
            int c = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                dispose();
                new DangNhapGUI().setVisible(true);
            }
        }
    }

    private JLabel makeValueLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        return lbl;
    }
}