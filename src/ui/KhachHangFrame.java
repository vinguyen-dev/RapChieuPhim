package ui;

import dao.KhachHangDAO;
import entity.KhachHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KhachHangFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private KhachHangDAO khachHangDAO;

    private JTextField txtMaKhachHang, txtTenKhachHang, txtSoDienThoai, txtEmail, txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;

    public KhachHangFrame() {
        khachHangDAO = KhachHangDAO.getInstance();

        setTitle("Quản Lý Khách Hàng");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel trên - Tìm kiếm
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createTitledBorder("Tìm Kiếm"));

        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("Tìm Kiếm");
        btnTimKiem.addActionListener(e -> timKiemKhachHang());

        topPanel.add(new JLabel("Tên/SĐT:"));
        topPanel.add(txtTimKiem);
        topPanel.add(btnTimKiem);

        add(topPanel, BorderLayout.NORTH);

        // Panel giữa - Bảng dữ liệu
        String[] columnNames = {"Mã KH", "Tên Khách Hàng", "Số Điện Thoại", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel dưới - Form nhập liệu
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Khách Hàng"));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã KH:"), gbc);
        gbc.gridx = 1;
        txtMaKhachHang = new JTextField(15);
        txtMaKhachHang.setEditable(false);
        formPanel.add(txtMaKhachHang, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Tên Khách Hàng:"), gbc);
        gbc.gridx = 3;
        txtTenKhachHang = new JTextField(15);
        formPanel.add(txtTenKhachHang, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Số Điện Thoại:"), gbc);
        gbc.gridx = 1;
        txtSoDienThoai = new JTextField(15);
        formPanel.add(txtSoDienThoai, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        txtEmail = new JTextField(15);
        formPanel.add(txtEmail, gbc);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm Mới");

        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnLamMoi.addActionListener(e -> lamMoi());

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<KhachHang> danhSachKhachHang = khachHangDAO.layDanhSachKhachHang();
        for (KhachHang kh : danhSachKhachHang) {
            Object[] row = {
                kh.getMaKhachHang(),
                kh.getTenKhachHang(),
                kh.getSoDienThoai(),
                kh.getEmail()
            };
            tableModel.addRow(row);
        }
    }

    private void displaySelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            txtMaKhachHang.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenKhachHang.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtSoDienThoai.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtEmail.setText(tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : "");
        }
    }

    private void themKhachHang() {
        String tenKhachHang = txtTenKhachHang.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
        String email = txtEmail.getText().trim();

        if (tenKhachHang.isEmpty() || soDienThoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        KhachHang kh = new KhachHang();
        kh.setTenKhachHang(tenKhachHang);
        kh.setSoDienThoai(soDienThoai);
        kh.setEmail(email);

        if (khachHangDAO.themKhachHang(kh)) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            loadData();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaKhachHang() {
        String maKhachHangStr = txtMaKhachHang.getText().trim();
        if (maKhachHangStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maKhachHang = Integer.parseInt(maKhachHangStr);
        String tenKhachHang = txtTenKhachHang.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
        String email = txtEmail.getText().trim();

        if (tenKhachHang.isEmpty() || soDienThoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        KhachHang kh = new KhachHang();
        kh.setMaKhachHang(maKhachHang);
        kh.setTenKhachHang(tenKhachHang);
        kh.setSoDienThoai(soDienThoai);
        kh.setEmail(email);

        if (khachHangDAO.capNhatKhachHang(kh)) {
            JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!");
            loadData();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaKhachHang() {
        String maKhachHangStr = txtMaKhachHang.getText().trim();
        if (maKhachHangStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khách hàng này?", "Xác Nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            int maKhachHang = Integer.parseInt(maKhachHangStr);
            if (khachHangDAO.xoaKhachHang(maKhachHang)) {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                loadData();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void timKiemKhachHang() {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);

        // Tìm theo tên
        List<KhachHang> danhSachKhachHang = khachHangDAO.timKhachHangTheoTen(keyword);

        // Nếu không tìm thấy theo tên, thử tìm theo SĐT
        if (danhSachKhachHang.isEmpty()) {
            KhachHang kh = khachHangDAO.timKhachHangTheoSDT(keyword);
            if (kh != null) {
                danhSachKhachHang.add(kh);
            }
        }

        for (KhachHang kh : danhSachKhachHang) {
            Object[] row = {
                kh.getMaKhachHang(),
                kh.getTenKhachHang(),
                kh.getSoDienThoai(),
                kh.getEmail()
            };
            tableModel.addRow(row);
        }
    }

    private void lamMoi() {
        txtMaKhachHang.setText("");
        txtTenKhachHang.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        txtTimKiem.setText("");
        table.clearSelection();
        loadData();
    }
}
