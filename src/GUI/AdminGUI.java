package GUI;

import BLL.TaiKhoanBLL;
import DTO.TaiKhoanDTO;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AdminGUI extends JFrame implements ActionListener {

    private TaiKhoanBLL tkBLL = new TaiKhoanBLL();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txt_find;
    private JComboBox<String> cbb_role;
    private JButton btn_add, btn_upd, btn_del, btn_nap, btn_find, btn_refresh, btn_logout;

    public AdminGUI(TaiKhoanDTO admin) {
        setTitle("Quản Lý Tài Khoản – Admin: " + admin.getTenDangNhap());
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadTable(tkBLL.getAllAccounts());
    }

    public void initComponents() {
        // ── Bảng ──────────────────────────────────────────
        String[] cols = {"Mã", "Tên đăng nhập", "Mật khẩu", "Vai trò", "Số dư"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);

        // ── Top: tìm kiếm ─────────────────────────────────
        JPanel pn_top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        txt_find = new JTextField(16);
        cbb_role = new JComboBox<>(new String[]{"Tất cả", "admin", "user"});
        btn_find    = new JButton("Tìm");
        btn_refresh = new JButton("Làm mới");
        pn_top.add(new JLabel("Tên:")); pn_top.add(txt_find);
        pn_top.add(new JLabel("Vai trò:")); pn_top.add(cbb_role);
        pn_top.add(btn_find); pn_top.add(btn_refresh);

        // ── Right: nút chức năng ──────────────────────────
        JPanel pn_right = new JPanel(new GridLayout(5, 1, 5, 8));
        pn_right.setBorder(new EmptyBorder(10, 6, 10, 8));
        btn_add = new JButton("Thêm mới");
        btn_upd = new JButton("Sửa chi tiết");
        btn_del = new JButton("Xóa");
        btn_nap = new JButton("Nạp tiền");
        pn_right.add(new JLabel("CHỨC NĂNG", SwingConstants.CENTER));
        pn_right.add(btn_add); pn_right.add(btn_upd);
        pn_right.add(btn_del); pn_right.add(btn_nap);

        // ── Bottom: đăng xuất ─────────────────────────────
        JPanel pn_bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        pn_bottom.setBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        btn_logout = new JButton("Đăng xuất");
        pn_bottom.add(btn_logout);

        // ── Layout chính ──────────────────────────────────
        JPanel pn_main = new JPanel(new BorderLayout(5, 5));
        pn_main.add(pn_top,     BorderLayout.NORTH);
        pn_main.add(scrollPane, BorderLayout.CENTER);
        pn_main.add(pn_right,   BorderLayout.EAST);
        pn_main.add(pn_bottom,  BorderLayout.SOUTH);
        add(pn_main);

        // ── Gắn sự kiện ───────────────────────────────────
        btn_add.addActionListener(this);
        btn_upd.addActionListener(this);
        btn_del.addActionListener(this);
        btn_nap.addActionListener(this);
        btn_find.addActionListener(this);
        btn_refresh.addActionListener(this);
        btn_logout.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_add) {
            new TaiKhoanDetailGUI(this, null);
            loadTable(tkBLL.getAllAccounts());
        }

        if (e.getSource() == btn_upd) {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Hãy chọn 1 dòng để sửa!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            TaiKhoanDTO tk = tkBLL.getAllAccounts().stream()
                    .filter(t -> t.getId() == id).findFirst().orElse(null);
            new TaiKhoanDetailGUI(this, tk);
            loadTable(tkBLL.getAllAccounts());
        }

        if (e.getSource() == btn_del) {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Hãy chọn 1 dòng để xóa!"); return; }
            String user = tableModel.getValueAt(row, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa \"" + user + "\"?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, tkBLL.deleteAccount(user));
                loadTable(tkBLL.getAllAccounts());
            }
        }

        if (e.getSource() == btn_nap) {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Hãy chọn 1 dòng để nạp tiền!"); return; }
            String user    = tableModel.getValueAt(row, 1).toString();
            String pass    = tableModel.getValueAt(row, 2).toString();
            String role    = tableModel.getValueAt(row, 3).toString();
            double current = Double.parseDouble(tableModel.getValueAt(row, 4).toString());
            String input   = JOptionPane.showInputDialog(this, "Nhập số tiền nạp cho \"" + user + "\":");
            if (input == null || input.trim().isEmpty()) return;
            try {
                double amount = Double.parseDouble(input.trim());
                if (amount <= 0) { JOptionPane.showMessageDialog(this, "Tiền nạp phải > 0!"); return; }
                TaiKhoanDTO tk = new TaiKhoanDTO(0, user, pass, role, current + amount);
                String msg = tkBLL.editAccount(tk);
                JOptionPane.showMessageDialog(this, msg.contains("thành công") ? "Nạp tiền thành công!" : msg);
                loadTable(tkBLL.getAllAccounts());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ!");
            }
        }

        if (e.getSource() == btn_find) {
            String keyword    = txt_find.getText().trim().toLowerCase();
            String roleFilter = cbb_role.getSelectedItem().toString();
            ArrayList<TaiKhoanDTO> result = new ArrayList<>();
            for (TaiKhoanDTO tk : tkBLL.getAllAccounts()) {
                boolean matchName = keyword.isEmpty() || tk.getTenDangNhap().toLowerCase().contains(keyword);
                boolean matchRole = roleFilter.equals("Tất cả") || tk.getVaiTro().equalsIgnoreCase(roleFilter);
                if (matchName && matchRole) result.add(tk);
            }
            loadTable(result);
        }

        if (e.getSource() == btn_refresh) {
            txt_find.setText("");
            cbb_role.setSelectedIndex(0);
            loadTable(tkBLL.getAllAccounts());
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

    public void loadTable(ArrayList<TaiKhoanDTO> list) {
        tableModel.setRowCount(0);
        for (TaiKhoanDTO tk : list) {
            tableModel.addRow(new Object[]{
                tk.getId(), tk.getTenDangNhap(),
                tk.getMatKhau(), tk.getVaiTro(), tk.getSoDu()
            });
        }
    }
}