package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import BLL.DichVuBLL;

import DTO.LoaiDichVu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DichVuMainGUI extends JFrame implements ActionListener{
    //private DichVuBLL bll = new DichVuBLL();
    private JTable table;
    private DefaultTableModel tableModel;

    public DichVuMainGUI(){
        setTitle("Quản lý Dịch vụ F&B");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    public void initComponents(){
        String[] columns = {"Mã", "Tên dịch vụ", "Loại", "Giá", "Tồn kho", "Nhập về"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel pn_top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txt_find = new JTextField(15);
        JComboBox<LoaiDichVu> cbb_type = new JComboBox<>(LoaiDichVu.values());
        JButton btn_find = new JButton("Tìm");
        pn_top.add(new JLabel("Tên:")); pn_top.add(txt_find);
        pn_top.add(new JLabel("Loại:")); pn_top.add(cbb_type);
        pn_top.add(btn_find);

        JPanel pn_left = new JPanel(new GridLayout(4, 1, 5, 5)); 
        JButton btn_add = new JButton("Thêm mới");
        JButton btn_upd = new JButton("Sửa chi tiết");
        JButton btn_del = new JButton("Xóa");
        pn_left.add(new JLabel("CHỨC NĂNG"));
        pn_left.add(btn_add); pn_left.add(btn_upd); pn_left.add(btn_del);

        JPanel pn_main = new JPanel(new BorderLayout(10, 10));
        pn_main.add(pn_top, BorderLayout.NORTH); 
        pn_main.add(scrollPane, BorderLayout.CENTER); 
        pn_main.add(pn_left, BorderLayout.EAST); 

        add(pn_main);
    }


    public void actionPerformed(ActionEvent e){

    }

    public static void main(String[] args){
        new DichVuMainGUI();
    }
}
