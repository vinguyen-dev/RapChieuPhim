package ui;

import dao.PhongChieuDAO;
import entity.PhongChieu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PhongChieuFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private PhongChieuDAO phongChieuDAO;

    private JTextField txtMaPhong, txtTenPhong, txtSoGhe;
    private JComboBox<String> cboLoaiPhong;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    public PhongChieuFrame() {
        phongChieuDAO = PhongChieuDAO.getInstance();

        setTitle("Quản Lý Phòng Chiếu");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel giữa - Bảng dữ liệu
        String[] columnNames = {"Mã Phòng", "Tên Phòng", "Số Ghế", "Loại Phòng"};
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
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Phòng Chiếu"));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã Phòng:"), gbc);
        gbc.gridx = 1;
        txtMaPhong = new JTextField(15);
        txtMaPhong.setEditable(false);
        formPanel.add(txtMaPhong, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Tên Phòng:"), gbc);
        gbc.gridx = 3;
        txtTenPhong = new JTextField(15);
        formPanel.add(txtTenPhong, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Số Ghế:"), gbc);
        gbc.gridx = 1;
        txtSoGhe = new JTextField(15);
        formPanel.add(txtSoGhe, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Loại Phòng:"), gbc);
        gbc.gridx = 3;
        cboLoaiPhong = new JComboBox<>(new String[]{"2D", "3D", "IMAX", "4DX"});
        formPanel.add(cboLoaiPhong, gbc);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm Mới");

        btnThem.addActionListener(e -> themPhongChieu());
        btnSua.addActionListener(e -> suaPhongChieu());
        btnXoa.addActionListener(e -> xoaPhongChieu());
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
        List<PhongChieu> danhSachPhongChieu = phongChieuDAO.layDanhSachPhongChieu();
        for (PhongChieu phongChieu : danhSachPhongChieu) {
            Object[] row = {
                phongChieu.getMaPhong(),
                phongChieu.getTenPhong(),
                phongChieu.getSoGhe(),
                phongChieu.getLoaiPhong()
            };
            tableModel.addRow(row);
        }
    }

    private void displaySelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            txtMaPhong.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenPhong.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtSoGhe.setText(tableModel.getValueAt(selectedRow, 2).toString());
            cboLoaiPhong.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
        }
    }

    private void themPhongChieu() {
        try {
            String tenPhong = txtTenPhong.getText().trim();
            String soGheStr = txtSoGhe.getText().trim();
            String loaiPhong = cboLoaiPhong.getSelectedItem().toString();

            if (tenPhong.isEmpty() || soGheStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int soGhe = Integer.parseInt(soGheStr);

            PhongChieu phongChieu = new PhongChieu();
            phongChieu.setTenPhong(tenPhong);
            phongChieu.setSoGhe(soGhe);
            phongChieu.setLoaiPhong(loaiPhong);

            if (phongChieuDAO.themPhongChieu(phongChieu)) {
                JOptionPane.showMessageDialog(this, "Thêm phòng chiếu thành công!");
                loadData();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm phòng chiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số ghế phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaPhongChieu() {
        try {
            String maPhongStr = txtMaPhong.getText().trim();
            if (maPhongStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng chiếu cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int maPhong = Integer.parseInt(maPhongStr);
            String tenPhong = txtTenPhong.getText().trim();
            String soGheStr = txtSoGhe.getText().trim();
            String loaiPhong = cboLoaiPhong.getSelectedItem().toString();

            if (tenPhong.isEmpty() || soGheStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int soGhe = Integer.parseInt(soGheStr);

            PhongChieu phongChieu = new PhongChieu();
            phongChieu.setMaPhong(maPhong);
            phongChieu.setTenPhong(tenPhong);
            phongChieu.setSoGhe(soGhe);
            phongChieu.setLoaiPhong(loaiPhong);

            if (phongChieuDAO.capNhatPhongChieu(phongChieu)) {
                JOptionPane.showMessageDialog(this, "Cập nhật phòng chiếu thành công!");
                loadData();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật phòng chiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaPhongChieu() {
        String maPhongStr = txtMaPhong.getText().trim();
        if (maPhongStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng chiếu cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phòng chiếu này?", "Xác Nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            int maPhong = Integer.parseInt(maPhongStr);
            if (phongChieuDAO.xoaPhongChieu(maPhong)) {
                JOptionPane.showMessageDialog(this, "Xóa phòng chiếu thành công!");
                loadData();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa phòng chiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void lamMoi() {
        txtMaPhong.setText("");
        txtTenPhong.setText("");
        txtSoGhe.setText("");
        cboLoaiPhong.setSelectedIndex(0);
        table.clearSelection();
        loadData();
    }
}
