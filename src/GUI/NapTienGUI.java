package GUI;

import BLL.TaiKhoanBLL;
import DTO.TaiKhoanDTO;
import javax.swing.*;
import java.awt.*;

public class NapTienGUI extends JDialog {
    private TaiKhoanBLL tkBLL = new TaiKhoanBLL();
    private TaiKhoanDTO user;

    public NapTienGUI(JFrame parent, TaiKhoanDTO user) {
        super(parent, "Nạp Tiền Tài Khoản", true);
        this.user = user;
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel pnMenhGia = new JPanel(new GridLayout(2, 3, 10, 10));
        int[] menhGia = {10000, 20000, 50000, 100000, 200000, 500000};
        
        for (int m : menhGia) {
            JButton btn = new JButton(m / 1000 + "k");
            btn.addActionListener(e -> xuLyNap(m));
            pnMenhGia.add(btn);
        }

        JPanel pnTuChon = new JPanel(new FlowLayout());
        JTextField txtTien = new JTextField(10);
        JButton btnNap = new JButton("Nạp");
        btnNap.addActionListener(e -> {
            try {
                xuLyNap(Double.parseDouble(txtTien.getText()));
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ"); }
        });
        
        pnTuChon.add(new JLabel("Số khác:"));
        pnTuChon.add(txtTien);
        pnTuChon.add(btnNap);

        add(new JLabel("Chọn mệnh giá muốn nạp", SwingConstants.CENTER), BorderLayout.NORTH);
        add(pnMenhGia, BorderLayout.CENTER);
        add(pnTuChon, BorderLayout.SOUTH);
    }

    private void xuLyNap(double soTien) {
        String res = tkBLL.napTien(user.getTenDangNhap(), String.valueOf(soTien), user.getSoDu());
        if (res.equals("success")) {
            user.setSoDu(user.getSoDu() + soTien);
            JOptionPane.showMessageDialog(this, "Nạp thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, res);
        }
    }
}