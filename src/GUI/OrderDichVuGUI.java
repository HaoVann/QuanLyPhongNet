package GUI;

import DAL.DichVuDAL; // Của Duy
import DTO.DichVuDTO;
import DTO.ChiTietGoiDichVuDTO;
import DAL.ChiTietGoiDichVuDAL;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderDichVuGUI extends JDialog {
    private DichVuDAL dvDAL = new DichVuDAL();
    private ChiTietGoiDichVuDAL ctDAL = new ChiTietGoiDichVuDAL();
    private int maThue; // Truyền từ UserGUI qua

    public OrderDichVuGUI(JFrame parent, int maThue) {
        super(parent, "Menu Gọi Món", true);
        this.maThue = maThue;
        setSize(500, 400);
        setLocationRelativeTo(parent);
        
        JPanel pnMain = new JPanel(new GridLayout(0, 2, 10, 10)); // 2 cột
        List<DichVuDTO> dsDV = dvDAL.getAllDichVu();

        for (DichVuDTO dv : dsDV) {
            JPanel item = new JPanel(new BorderLayout());
            item.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            item.add(new JLabel(dv.getTen() + " - " + Math.round(dv.getGia()) + "đ"), BorderLayout.CENTER);
            
            JButton btnAdd = new JButton("Thêm");
            btnAdd.addActionListener(e -> {
                ChiTietGoiDichVuDTO ct = new ChiTietGoiDichVuDTO();
                ct.setMaThue(maThue);
                ct.setMaDV(dv.getMa());
                ct.setSoLuong(1);
                ct.setThanhTien(dv.getGia());
                
                if(ctDAL.goiMon(ct)) {
                    JOptionPane.showMessageDialog(this, "Đã thêm " + dv.getTen());
                }
            });
            item.add(btnAdd, BorderLayout.EAST);
            pnMain.add(item);
        }
        
        add(new JScrollPane(pnMain));
    }
}
