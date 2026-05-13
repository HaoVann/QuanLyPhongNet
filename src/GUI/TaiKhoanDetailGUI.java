package GUI;

import BLL.TaiKhoanBLL;
import DTO.TaiKhoanDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TaiKhoanDetailGUI extends JDialog implements ActionListener {

    private TaiKhoanBLL tkBLL = new TaiKhoanBLL();
    private TaiKhoanDTO currentTK;

    private JLabel lb_ma_val;
    private JTextField txt_user, txt_pass, txt_balance;
    private JComboBox<String> cbb_role;
    private JButton btn_ok, btn_cancel;

    public TaiKhoanDetailGUI(JFrame parent, TaiKhoanDTO tk) {
        super(parent, true);
        this.currentTK = tk;
        setTitle(tk == null ? "Thêm tài khoản mới" : "Chỉnh sửa tài khoản");
        setSize(320, 300);
        setLocationRelativeTo(parent);
        initComponents();
        if (tk != null) {
            lb_ma_val.setText(String.valueOf(tk.getId()));
            txt_user.setText(tk.getTenDangNhap());
            txt_pass.setText(tk.getMatKhau());
            cbb_role.setSelectedItem(tk.getVaiTro());
            txt_balance.setText(String.valueOf(tk.getSoDu()));
        }
        setVisible(true);
    }

    public void initComponents() {
        JPanel pn_center = new JPanel(new GridLayout(5, 2, 5, 8));
        pn_center.setBorder(BorderFactory.createEmptyBorder(12, 16, 8, 16));

        pn_center.add(new JLabel("Mã:"));
        lb_ma_val = new JLabel("Tự động");
        pn_center.add(lb_ma_val);

        pn_center.add(new JLabel("Tên đăng nhập:"));
        txt_user = new JTextField();
        pn_center.add(txt_user);

        pn_center.add(new JLabel("Mật khẩu:"));
        txt_pass = new JTextField();
        pn_center.add(txt_pass);

        pn_center.add(new JLabel("Vai trò:"));
        cbb_role = new JComboBox<>(new String[]{"user", "admin"});
        pn_center.add(cbb_role);

        pn_center.add(new JLabel("Số dư:"));
        txt_balance = new JTextField("0");
        pn_center.add(txt_balance);

        JPanel pn_bottom = new JPanel();
        btn_ok     = new JButton("Lưu");
        btn_cancel = new JButton("Hủy");
        btn_ok.addActionListener(this);
        btn_cancel.addActionListener(this);
        pn_bottom.add(btn_ok);
        pn_bottom.add(btn_cancel);

        add(pn_center, BorderLayout.CENTER);
        add(pn_bottom, BorderLayout.SOUTH);
    }

    private void saveData() {
        String user = txt_user.getText().trim();
        String pass = txt_pass.getText().trim();
        String role = cbb_role.getSelectedItem().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        double balance;
        try {
            balance = Double.parseDouble(txt_balance.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số dư phải là số hợp lệ!");
            return;
        }

        TaiKhoanDTO tk = (currentTK == null) ? new TaiKhoanDTO() : currentTK;
        tk.setTenDangNhap(user);
        tk.setMatKhau(pass);
        tk.setVaiTro(role);
        tk.setSoDu(balance);

        String result = (currentTK == null) ? tkBLL.addAccount(tk) : tkBLL.editAccount(tk);
        JOptionPane.showMessageDialog(this, result);
        if (result.contains("thành công")) dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_cancel) dispose();
        if (e.getSource() == btn_ok) saveData();
    }
}