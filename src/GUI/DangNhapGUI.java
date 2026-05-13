package GUI;

import BLL.TaiKhoanBLL;
import DTO.TaiKhoanDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DangNhapGUI extends JFrame implements ActionListener {

    private TaiKhoanBLL tkBLL = new TaiKhoanBLL();
    private JTextField txt_user;
    private JPasswordField txt_pass;
    private JButton btn_login;
    private JLabel lb_thongbao;

    public DangNhapGUI() {
        setTitle("Đăng Nhập Hệ Thống");
        setSize(380, 260);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    public void initComponents() {
        JPanel pn_main = new JPanel(new GridBagLayout());
        pn_main.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("QUẢN LÝ QUÁN NET", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        pn_main.add(lblTitle, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0;
        pn_main.add(new JLabel("Tên đăng nhập:"), gbc);
        txt_user = new JTextField(18);
        gbc.gridx = 1; gbc.weightx = 1;
        pn_main.add(txt_user, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0;
        pn_main.add(new JLabel("Mật khẩu:"), gbc);
        txt_pass = new JPasswordField(18);
        gbc.gridx = 1; gbc.weightx = 1;
        pn_main.add(txt_pass, gbc);

        lb_thongbao = new JLabel(" ", SwingConstants.CENTER);
        lb_thongbao.setForeground(Color.RED);
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        pn_main.add(lb_thongbao, gbc);

        btn_login = new JButton("Đăng Nhập");
        gbc.gridy = 4;
        pn_main.add(btn_login, gbc);

        btn_login.addActionListener(this);
        txt_pass.addActionListener(this);
        txt_user.addActionListener(this);

        add(pn_main);
    }

    public void actionPerformed(ActionEvent e) {
        String user = txt_user.getText().trim();
        String pass = new String(txt_pass.getPassword()).trim();
        Object result = tkBLL.checkLogin(user, pass);
        if (result instanceof TaiKhoanDTO) {
            TaiKhoanDTO tk = (TaiKhoanDTO) result;
            dispose();
            if ("admin".equalsIgnoreCase(tk.getVaiTro())) {
                new AdminGUI(tk).setVisible(true);
            } else {
                new UserGUI(tk).setVisible(true);
            }
        } else {
            lb_thongbao.setText(result.toString());
            txt_pass.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DangNhapGUI().setVisible(true));
    }
}