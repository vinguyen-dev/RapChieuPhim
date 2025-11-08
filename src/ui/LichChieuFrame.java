package ui;

import dao.LichChieuDAO;
import dao.PhimDAO;
import dao.PhongChieuDAO;
import entity.LichChieu;
import entity.Phim;
import entity.PhongChieu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LichChieuFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private LichChieuDAO lichChieuDAO;
    private PhimDAO phimDAO;
    private PhongChieuDAO phongChieuDAO;

    private JTextField txtMaLichChieu, txtNgayChieu, txtGioChieu, txtGiaVe;
    private JComboBox<String> cboPhim, cboPhongChieu;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    public LichChieuFrame() {
        lichChieuDAO = LichChieuDAO.getInstance();
        phimDAO = PhimDAO.getInstance();
        phongChieuDAO = PhongChieuDAO.getInstance();

        setTitle("Quản Lý Lịch Chiếu");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadData();
        loadComboBoxes();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel giữa - Bảng dữ liệu
        String[] columnNames = {"Mã Lịch", "Phim", "Phòng", "Ngày Chiếu", "Giờ Chiếu", "Giá Vé"};
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
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Lịch Chiếu"));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã Lịch:"), gbc);
        gbc.gridx = 1;
        txtMaLichChieu = new JTextField(15);
        txtMaLichChieu.setEditable(false);
        formPanel.add(txtMaLichChieu, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Phim:"), gbc);
        gbc.gridx = 3;
        cboPhim = new JComboBox<>();
        formPanel.add(cboPhim, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Phòng Chiếu:"), gbc);
        gbc.gridx = 1;
        cboPhongChieu = new JComboBox<>();
        formPanel.add(cboPhongChieu, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Ngày Chiếu (yyyy-MM-dd):"), gbc);
        gbc.gridx = 3;
        txtNgayChieu = new JTextField(15);
        formPanel.add(txtNgayChieu, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Giờ Chiếu (HH:mm):"), gbc);
        gbc.gridx = 1;
        txtGioChieu = new JTextField(15);
        formPanel.add(txtGioChieu, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Giá Vé:"), gbc);
        gbc.gridx = 3;
        txtGiaVe = new JTextField(15);
        formPanel.add(txtGiaVe, gbc);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm Mới");

        btnThem.addActionListener(e -> themLichChieu());
        btnSua.addActionListener(e -> suaLichChieu());
        btnXoa.addActionListener(e -> xoaLichChieu());
        btnLamMoi.addActionListener(e -> lamMoi());

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadComboBoxes() {
        // Load Phim
        cboPhim.removeAllItems();
        List<Phim> danhSachPhim = phimDAO.layDanhSachPhim();
        for (Phim phim : danhSachPhim) {
            cboPhim.addItem(phim.getMaPhim() + " - " + phim.getTenPhim());
        }

        // Load Phòng Chiếu
        cboPhongChieu.removeAllItems();
        List<PhongChieu> danhSachPhongChieu = phongChieuDAO.layDanhSachPhongChieu();
        for (PhongChieu phongChieu : danhSachPhongChieu) {
            cboPhongChieu.addItem(phongChieu.getMaPhong() + " - " + phongChieu.getTenPhong());
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<LichChieu> danhSachLichChieu = lichChieuDAO.layDanhSachLichChieu();
        for (LichChieu lc : danhSachLichChieu) {
            Object[] row = {
                lc.getMaLichChieu(),
                lc.getPhim().getTenPhim(),
                lc.getPhongChieu().getTenPhong(),
                lc.getNgayChieu(),
                lc.getGioChieu(),
                lc.getGiaVe()
            };
            tableModel.addRow(row);
        }
    }

    private void displaySelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int maLichChieu = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            LichChieu lc = lichChieuDAO.timLichChieuTheoMa(maLichChieu);

            if (lc != null) {
                txtMaLichChieu.setText(String.valueOf(lc.getMaLichChieu()));
                txtNgayChieu.setText(lc.getNgayChieu().toString());
                txtGioChieu.setText(lc.getGioChieu().toString());
                txtGiaVe.setText(String.valueOf(lc.getGiaVe()));

                // Select phim
                for (int i = 0; i < cboPhim.getItemCount(); i++) {
                    String item = cboPhim.getItemAt(i);
                    if (item.startsWith(lc.getPhim().getMaPhim() + " - ")) {
                        cboPhim.setSelectedIndex(i);
                        break;
                    }
                }

                // Select phòng
                for (int i = 0; i < cboPhongChieu.getItemCount(); i++) {
                    String item = cboPhongChieu.getItemAt(i);
                    if (item.startsWith(lc.getPhongChieu().getMaPhong() + " - ")) {
                        cboPhongChieu.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    private int extractId(String comboBoxItem) {
        if (comboBoxItem == null || comboBoxItem.isEmpty()) return -1;
        String[] parts = comboBoxItem.split(" - ");
        return Integer.parseInt(parts[0]);
    }

    private void themLichChieu() {
        try {
            if (cboPhim.getSelectedItem() == null || cboPhongChieu.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phim và phòng chiếu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int maPhim = extractId(cboPhim.getSelectedItem().toString());
            int maPhong = extractId(cboPhongChieu.getSelectedItem().toString());
            LocalDate ngayChieu = LocalDate.parse(txtNgayChieu.getText().trim());
            LocalTime gioChieu = LocalTime.parse(txtGioChieu.getText().trim());
            double giaVe = Double.parseDouble(txtGiaVe.getText().trim());

            Phim phim = phimDAO.timPhimTheoMa(maPhim);
            PhongChieu phongChieu = phongChieuDAO.timPhongChieuTheoMa(maPhong);

            LichChieu lc = new LichChieu();
            lc.setPhim(phim);
            lc.setPhongChieu(phongChieu);
            lc.setNgayChieu(ngayChieu);
            lc.setGioChieu(gioChieu);
            lc.setGiaVe(giaVe);

            if (lichChieuDAO.themLichChieu(lc)) {
                JOptionPane.showMessageDialog(this, "Thêm lịch chiếu thành công!");
                loadData();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm lịch chiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!\n" + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaLichChieu() {
        try {
            String maLichChieuStr = txtMaLichChieu.getText().trim();
            if (maLichChieuStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch chiếu cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int maLichChieu = Integer.parseInt(maLichChieuStr);
            int maPhim = extractId(cboPhim.getSelectedItem().toString());
            int maPhong = extractId(cboPhongChieu.getSelectedItem().toString());
            LocalDate ngayChieu = LocalDate.parse(txtNgayChieu.getText().trim());
            LocalTime gioChieu = LocalTime.parse(txtGioChieu.getText().trim());
            double giaVe = Double.parseDouble(txtGiaVe.getText().trim());

            Phim phim = phimDAO.timPhimTheoMa(maPhim);
            PhongChieu phongChieu = phongChieuDAO.timPhongChieuTheoMa(maPhong);

            LichChieu lc = new LichChieu();
            lc.setMaLichChieu(maLichChieu);
            lc.setPhim(phim);
            lc.setPhongChieu(phongChieu);
            lc.setNgayChieu(ngayChieu);
            lc.setGioChieu(gioChieu);
            lc.setGiaVe(giaVe);

            if (lichChieuDAO.capNhatLichChieu(lc)) {
                JOptionPane.showMessageDialog(this, "Cập nhật lịch chiếu thành công!");
                loadData();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật lịch chiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!\n" + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaLichChieu() {
        String maLichChieuStr = txtMaLichChieu.getText().trim();
        if (maLichChieuStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch chiếu cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa lịch chiếu này?", "Xác Nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            int maLichChieu = Integer.parseInt(maLichChieuStr);
            if (lichChieuDAO.xoaLichChieu(maLichChieu)) {
                JOptionPane.showMessageDialog(this, "Xóa lịch chiếu thành công!");
                loadData();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa lịch chiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void lamMoi() {
        txtMaLichChieu.setText("");
        txtNgayChieu.setText("");
        txtGioChieu.setText("");
        txtGiaVe.setText("");
        if (cboPhim.getItemCount() > 0) cboPhim.setSelectedIndex(0);
        if (cboPhongChieu.getItemCount() > 0) cboPhongChieu.setSelectedIndex(0);
        table.clearSelection();
        loadData();
    }
}
