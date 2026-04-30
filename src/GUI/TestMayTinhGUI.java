package GUI;

import BLL.MayTinhBLL;
import DTO.MayTinhDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TestMayTinhGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnLoadData;
    private MayTinhBLL mayTinhBLL;

    public TestMayTinhGUI() {
        mayTinhBLL = new MayTinhBLL();
        
        // Cài đặt khung JFrame
        setTitle("Test Kết Nối Database - Quản Lý Máy Tính");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Hiển thị ra giữa màn hình
        setLayout(new BorderLayout());

        // 1. Tạo nút bấm ở trên cùng
        btnLoadData = new JButton("Tải danh sách máy tính từ MySQL");
        add(btnLoadData, BorderLayout.NORTH);

        // 2. Tạo bảng JTable ở giữa
        String[] columns = {"Mã Máy", "Tên Máy", "Trạng Thái", "Giá / Giờ (VNĐ)"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Bắt sự kiện khi bấm nút
        btnLoadData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hienThiDuLieu();
            }
        });
    }

    private void hienThiDuLieu() {
        // Xóa dữ liệu cũ trên bảng
        tableModel.setRowCount(0);

        // Lấy dữ liệu từ tầng BLL
        List<MayTinhDTO> dsMayTinh = mayTinhBLL.layDanhSachMayTinh();

        // Đổ từng dòng dữ liệu lên bảng
        for (MayTinhDTO mt : dsMayTinh) {
            Object[] row = {
                mt.getMaMay(),
                mt.getTenMay(),
                mt.getTrangThai(),
                mt.getGiaTheoGio()
            };
            tableModel.addRow(row);
        }
        JOptionPane.showMessageDialog(this, "Tải dữ liệu thành công!");
    }

    // Hàm Main để chạy trực tiếp giao diện này
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TestMayTinhGUI().setVisible(true);
            }
        });
    }
}
