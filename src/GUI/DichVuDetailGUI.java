package GUI;

import javax.swing.*;

import BLL.DichVuBLL;

import DTO.LoaiDichVu;
import DTO.DichVuDTO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DichVuDetailGUI extends JDialog implements ActionListener{
    private DichVuBLL bll = new DichVuBLL();
    private DichVuDTO currentDV;
    private JTextField txt_ten, txt_gia, txt_ton, txt_nhap;
    private JComboBox<LoaiDichVu> cbb_type;
    private JLabel lb_ma_val; 
    private JButton btn_ok, btn_can;

    public DichVuDetailGUI(JFrame parent, DichVuDTO dv){
        super(parent, true); 
        this.currentDV = dv;
        setTitle(dv == null ? "Thêm dịch vụ mới" : "Chỉnh sửa dịch vụ");
        setSize(350, 450);
        setLocationRelativeTo(parent);
        initComponents();

        if (dv != null) {
            lb_ma_val.setText(String.valueOf(dv.getMa()));
            txt_ten.setText(dv.getTen());
            cbb_type.setSelectedItem(dv.getLoai());
            txt_gia.setText(String.valueOf(dv.getGia()));
            txt_ton.setText(String.valueOf(dv.getTonKho()));
            txt_nhap.setText(String.valueOf(dv.getNhapHang()));
        }

        setVisible(true);
    }

    public void initComponents(){
        JPanel pn_main = new JPanel(new BorderLayout(10, 10));
        JPanel pn_center = new JPanel(new GridLayout(6, 2, 5, 5));

        pn_center.add(new JLabel("Mã:"));
        lb_ma_val = new JLabel("Tự động");
        pn_center.add(lb_ma_val);

        pn_center.add(new JLabel("Tên:"));
        txt_ten = new JTextField();
        pn_center.add(txt_ten);

        pn_center.add(new JLabel("Loại:"));
        cbb_type = new JComboBox<>(LoaiDichVu.values());
        pn_center.add(cbb_type);

        pn_center.add(new JLabel("Giá:"));
        txt_gia = new JTextField();
        pn_center.add(txt_gia);

        pn_center.add(new JLabel("Tồn kho:"));
        txt_ton = new JTextField();
        pn_center.add(txt_ton);

        pn_center.add(new JLabel("Lần nhập này:"));
        txt_nhap = new JTextField();
        pn_center.add(txt_nhap);

        JPanel pn_bottom = new JPanel();
        btn_ok = new JButton("Lưu");
        btn_can = new JButton("Hủy");
        btn_ok.addActionListener(this);
        btn_can.addActionListener(this);
        pn_bottom.add(btn_ok); pn_bottom.add(btn_can);

        pn_main.add(pn_center, BorderLayout.CENTER);
        pn_main.add(pn_bottom, BorderLayout.SOUTH);
        add(pn_main);
    }

    private void saveData() {
        try {
            double gia = Double.parseDouble(txt_gia.getText().trim());
            int ton = Integer.parseInt(txt_ton.getText().trim());
            int nhap = Integer.parseInt(txt_nhap.getText().trim());

            DichVuDTO dv = (currentDV == null) ? new DichVuDTO() : currentDV;
            dv.setTen(txt_ten.getText());
            dv.setLoai((LoaiDichVu) cbb_type.getSelectedItem());
            dv.setGia(Double.parseDouble(txt_gia.getText()));
            dv.setTonKho(Integer.parseInt(txt_ton.getText()));
            dv.setNhapHang(Integer.parseInt(txt_nhap.getText()));

            String result = (currentDV == null) ? bll.addDichVu(dv) : bll.updDichVu(dv);
            JOptionPane.showMessageDialog(this, result);
            if (result.contains("thành công")) dispose();
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Giá, Tồn kho và Nhập hàng phải là số hợp lệ!");
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Lỗi" + e);
        }
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btn_can) dispose();
        if (e.getSource() == btn_ok) {
            saveData();
        }
    }
}
