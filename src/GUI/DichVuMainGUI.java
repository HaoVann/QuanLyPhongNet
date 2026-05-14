package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import BLL.DichVuBLL;
import DTO.DichVuDTO;
import DTO.LoaiDichVu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DichVuMainGUI extends JFrame implements ActionListener{
    private DichVuBLL bll = new DichVuBLL();
    public List<DichVuDTO> preparedDV = bll.getAllDichVu();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txt_find;
    private JButton btn_add, btn_upd, btn_del, btn_find, btn_need;
    JComboBox<Object> cbb_type;

    public DichVuMainGUI(){
        setTitle("Quản lý Dịch vụ F&B");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        loadNewPreparedDV();
        loadTableDataFromPrepared();
        setVisible(true);
    }

    public void initComponents(){
        String[] columns = {"Mã", "Tên dịch vụ", "Loại", "Giá", "Tồn kho", "Nhập về"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel pn_top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txt_find = new JTextField(15);
        cbb_type = new JComboBox<>();
        cbb_type.addItem("Tất cả");
        for (LoaiDichVu loai : LoaiDichVu.values()) {
            cbb_type.addItem(loai);
        }
        btn_find = new JButton("Tìm");
        btn_need = new JButton("Gần hết");
        pn_top.add(new JLabel("Tên:")); pn_top.add(txt_find);
        pn_top.add(new JLabel("Loại:")); pn_top.add(cbb_type);
        pn_top.add(btn_find);
        pn_top.add(btn_need);

        JPanel pn_left = new JPanel(new GridLayout(4, 1, 5, 5)); 
        btn_add = new JButton("Thêm mới");
        btn_upd = new JButton("Sửa chi tiết");
        btn_del = new JButton("Xóa");
        pn_left.add(new JLabel("CHỨC NĂNG"));
        pn_left.add(btn_add); pn_left.add(btn_upd); pn_left.add(btn_del);

        JPanel pn_main = new JPanel(new BorderLayout(10, 10));
        pn_main.add(pn_top, BorderLayout.NORTH); 
        pn_main.add(scrollPane, BorderLayout.CENTER); 
        pn_main.add(pn_left, BorderLayout.EAST); 

        add(pn_main);

        btn_add.addActionListener(this);
        btn_upd.addActionListener(this);
        btn_del.addActionListener(this);
        btn_find.addActionListener(this);
        btn_need.addActionListener(this);
    }

    public void loadNewPreparedDV(){
        this.preparedDV = bll.getAllDichVu();
    }

    public void loadTableDataFromVar(List<DichVuDTO> list) {
        tableModel.setRowCount(0); 
        for (DichVuDTO dv : list) {
            tableModel.addRow(new Object[]{
                dv.getMa(), dv.getTen(), dv.getLoai().getDisplayValue(), 
                dv.getGia(), dv.getTonKho(), dv.getNhapHang()
            });
        }
    }

    public void loadTableDataFromPrepared() {
        loadTableDataFromVar(this.preparedDV);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btn_add) {
            new DichVuDetailGUI(this, null);
            loadNewPreparedDV();
            loadTableDataFromPrepared(); 
        }
    
        if (e.getSource() == btn_upd) {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int)table.getValueAt(row, 0);
                DichVuDTO dvCanSua = bll.getDichVuById(id);
                new DichVuDetailGUI(this, dvCanSua); 
                loadNewPreparedDV();
                loadTableDataFromPrepared();
            }else{
                JOptionPane.showMessageDialog(this, "Hãy chọn 1 dòng để sửa!");
            }
        }

        if(e.getSource() == btn_del){
            int row = table.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION){
                    int id = (int)table.getValueAt(row, 0);
                    DichVuDTO dvCanXoa = bll.getDichVuById(id);
                    bll.delDichVu(dvCanXoa);
                    loadNewPreparedDV();
                    loadTableDataFromPrepared();
                }
            }else{
                JOptionPane.showMessageDialog(this, "Hãy chọn 1 dòng để xóa!");
            }
        }

        if(e.getSource() == btn_need){
            List<DichVuDTO> sorted = bll.sortGanHet(preparedDV);
            loadTableDataFromVar(sorted);
        }

        if(e.getSource() == btn_find){
            Object selected = cbb_type.getSelectedItem();
            LoaiDichVu loaiChon;
            if("Tất cả".equals(selected.toString())){
                loaiChon = null;
            }
            else{
                loaiChon = (LoaiDichVu)selected; 
            }
            List<DichVuDTO> filtered = bll.findByNameAndType(txt_find.getText(), loaiChon, preparedDV);
            loadTableDataFromVar(filtered);
        }
    }

    public static void main(String[] args){
        new DichVuMainGUI();
    }
}
