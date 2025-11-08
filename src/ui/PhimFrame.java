package ui;

import dao.PhimDAO;
import entity.Phim;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PhimFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private PhimDAO phimDAO;

    private JTextField txtMaPhim, txtTenPhim, txtTheLoai, txtThoiLuong, txtHinhAnh, txtTimKiem;
    private JTextArea txtMoTa;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;

    public PhimFrame() {
        phimDAO = PhimDAO.getInstance();

        setTitle("Quản Lý Phim");
        setSize(1000, 600);
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
        btnTimKiem.addActionListener(e -> timKiemPhim());

        topPanel.add(new JLabel("Tên phim:"));
        topPanel.add(txtTimKiem);
        topPanel.add(btnTimKiem);

        add(topPanel, BorderLayout.NORTH);

        // Panel giữa - Bảng dữ liệu
        String[] columnNames = {"Mã Phim", "Tên Phim", "Thể Loại", "Thời Lượng (phút)", "Mô Tả", "Hình Ảnh"};
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
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Phim"));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã Phim:"), gbc);
        gbc.gridx = 1;
        txtMaPhim = new JTextField(15);
        txtMaPhim.setEditable(false);
        formPanel.add(txtMaPhim, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Tên Phim:"), gbc);
        gbc.gridx = 3;
        txtTenPhim = new JTextField(15);
        formPanel.add(txtTenPhim, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Thể Loại:"), gbc);
        gbc.gridx = 1;
        txtTheLoai = new JTextField(15);
        formPanel.add(txtTheLoai, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Thời Lượng (phút):"), gbc);
        gbc.gridx = 3;
        txtThoiLuong = new JTextField(15);
        formPanel.add(txtThoiLuong, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Hình Ảnh:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtHinhAnh = new JTextField();
        formPanel.add(txtHinhAnh, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Mô Tả:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtMoTa = new JTextArea(3, 20);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        formPanel.add(scrollMoTa, gbc);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm Mới");

        btnThem.addActionListener(e -> themPhim());
        btnSua.addActionListener(e -> suaPhim());
        btnXoa.addActionListener(e -> xoaPhim());
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
        List<Phim> danhSachPhim = phimDAO.layDanhSachPhim();
        for (Phim phim : danhSachPhim) {
            Object[] row = {
                phim.getMaPhim(),
                phim.getTenPhim(),
                phim.getTheLoai(),
                phim.getThoiLuong(),
                phim.getMoTa(),
                phim.getHinhAnh()
            };
            tableModel.addRow(row);
        }
    }

    private void displaySelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            txtMaPhim.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenPhim.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtTheLoai.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtThoiLuong.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtMoTa.setText(tableModel.getValueAt(selectedRow, 4) != null ? tableModel.getValueAt(selectedRow, 4).toString() : "");
            txtHinhAnh.setText(tableModel.getValueAt(selectedRow, 5) != null ? tableModel.getValueAt(selectedRow, 5).toString() : "");
        }
    }

    private void themPhim() {
        try {
            String tenPhim = txtTenPhim.getText().trim();
            String theLoai = txtTheLoai.getText().trim();
            String thoiLuongStr = txtThoiLuong.getText().trim();
            String moTa = txtMoTa.getText().trim();
            String hinhAnh = txtHinhAnh.getText().trim();

            if (tenPhim.isEmpty() || thoiLuongStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên phim và thời lượng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int thoiLuong = Integer.parseInt(thoiLuongStr);

            Phim phim = new Phim();
            phim.setTenPhim(tenPhim);
            phim.setTheLoai(theLoai);
            phim.setThoiLuong(thoiLuong);
            phim.setMoTa(moTa);
            phim.setHinhAnh(hinhAnh);

            if (phimDAO.themPhim(phim)) {
                JOptionPane.showMessageDialog(this, "Thêm phim thành công!");
                loadData();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm phim thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Thời lượng phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaPhim() {
        try {
            String maPhimStr = txtMaPhim.getText().trim();
            if (maPhimStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phim cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int maPhim = Integer.parseInt(maPhimStr);
            String tenPhim = txtTenPhim.getText().trim();
            String theLoai = txtTheLoai.getText().trim();
            String thoiLuongStr = txtThoiLuong.getText().trim();
            String moTa = txtMoTa.getText().trim();
            String hinhAnh = txtHinhAnh.getText().trim();

            if (tenPhim.isEmpty() || thoiLuongStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên phim và thời lượng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int thoiLuong = Integer.parseInt(thoiLuongStr);

            Phim phim = new Phim();
            phim.setMaPhim(maPhim);
            phim.setTenPhim(tenPhim);
            phim.setTheLoai(theLoai);
            phim.setThoiLuong(thoiLuong);
            phim.setMoTa(moTa);
            phim.setHinhAnh(hinhAnh);

            if (phimDAO.capNhatPhim(phim)) {
                JOptionPane.showMessageDialog(this, "Cập nhật phim thành công!");
                loadData();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật phim thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaPhim() {
        String maPhimStr = txtMaPhim.getText().trim();
        if (maPhimStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phim này?", "Xác Nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            int maPhim = Integer.parseInt(maPhimStr);
            if (phimDAO.xoaPhim(maPhim)) {
                JOptionPane.showMessageDialog(this, "Xóa phim thành công!");
                loadData();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa phim thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void timKiemPhim() {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        List<Phim> danhSachPhim = phimDAO.timPhimTheoTen(keyword);
        for (Phim phim : danhSachPhim) {
            Object[] row = {
                phim.getMaPhim(),
                phim.getTenPhim(),
                phim.getTheLoai(),
                phim.getThoiLuong(),
                phim.getMoTa(),
                phim.getHinhAnh()
            };
            tableModel.addRow(row);
        }
    }

    private void lamMoi() {
        txtMaPhim.setText("");
        txtTenPhim.setText("");
        txtTheLoai.setText("");
        txtThoiLuong.setText("");
        txtMoTa.setText("");
        txtHinhAnh.setText("");
        txtTimKiem.setText("");
        table.clearSelection();
        loadData();
    }
}
