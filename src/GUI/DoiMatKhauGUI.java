package GUI;

import BLL.TaiKhoanBLL;
import DTO.TaiKhoanDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DoiMatKhauGUI extends JDialog implements ActionListener {

    private TaiKhoanBLL tkBLL = new TaiKhoanBLL();
    private TaiKhoanDTO account;

    private JPasswordField txt_cu, txt_moi, txt_xacnhan;
    private JButton btn_ok, btn_cancel;

    public DoiMatKhauGUI(JFrame parent, TaiKhoanDTO account) {
        super(parent, true);
        this.account = account;
        setTitle("Đổi mật khẩu");
        setSize(300, 240);
        setLocationRelativeTo(parent);
        initComponents();
        setVisible(true);
    }

    public void initComponents() {
        JPanel pn_center = new JPanel(new GridLayout(3, 2, 5, 10));
        pn_center.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        pn_center.add(new JLabel("Mật khẩu cũ:"));
        txt_cu = new JPasswordField();
        pn_center.add(txt_cu);

        pn_center.add(new JLabel("Mật khẩu mới:"));
        txt_moi = new JPasswordField();
        pn_center.add(txt_moi);

        pn_center.add(new JLabel("Xác nhận mới:"));
        txt_xacnhan = new JPasswordField();
        pn_center.add(txt_xacnhan);

        JPanel pn_bottom = new JPanel();
        btn_ok     = new JButton("Xác nhận");
        btn_cancel = new JButton("Hủy");
        btn_ok.addActionListener(this);
        btn_cancel.addActionListener(this);
        pn_bottom.add(btn_ok);
        pn_bottom.add(btn_cancel);

        add(pn_center, BorderLayout.CENTER);
        add(pn_bottom, BorderLayout.SOUTH);
    }

    private void doiMatKhau() {
        String cu      = new String(txt_cu.getPassword()).trim();
        String moi     = new String(txt_moi.getPassword()).trim();
        String xacnhan = new String(txt_xacnhan.getPassword()).trim();

        if (cu.isEmpty() || moi.isEmpty() || xacnhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        if (!cu.equals(account.getMatKhau())) {
            JOptionPane.showMessageDialog(this, "Mật khẩu cũ không đúng!");
            return;
        }
        if (!moi.equals(xacnhan)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới không khớp!");
            return;
        }
        if (moi.equals(cu)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới phải khác mật khẩu cũ!");
            return;
        }

        account.setMatKhau(moi);
        String result = tkBLL.editAccount(account);
        JOptionPane.showMessageDialog(this, result.contains("thành công") ? "Đổi mật khẩu thành công!" : result);
        if (result.contains("thành công")) dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_cancel) dispose();
        if (e.getSource() == btn_ok) doiMatKhau();
    }
}