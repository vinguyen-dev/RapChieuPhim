package ui;

import dao.*;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatVeFrame extends JFrame {
    private JComboBox<String> cboLichChieu, cboKhachHang;
    private JTable tableGhe, tableVeDaDat;
    private DefaultTableModel tableModelGhe, tableModelVeDaDat;
    private JButton btnDatVe, btnXoaVe, btnThanhToan;
    private JLabel lblTongTien;

    private LichChieuDAO lichChieuDAO;
    private KhachHangDAO khachHangDAO;
    private GheDAO gheDAO;
    private VeDAO veDAO;
    private HoaDonDAO hoaDonDAO;

    private HoaDon hoaDonHienTai;
    private LichChieu lichChieuHienTai;

    public DatVeFrame() {
        lichChieuDAO = LichChieuDAO.getInstance();
        khachHangDAO = KhachHangDAO.getInstance();
        gheDAO = GheDAO.getInstance();
        veDAO = VeDAO.getInstance();
        hoaDonDAO = HoaDonDAO.getInstance();

        setTitle("Đặt Vé Xem Phim");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadComboBoxes();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Top Panel - Chọn lịch chiếu và khách hàng
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Đặt Vé"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Lịch Chiếu:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        cboLichChieu = new JComboBox<>();
        cboLichChieu.addActionListener(e -> loadGheTrong());
        topPanel.add(cboLichChieu, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        topPanel.add(new JLabel("Khách Hàng:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        cboKhachHang = new JComboBox<>();
        topPanel.add(cboKhachHang, gbc);

        add(topPanel, BorderLayout.NORTH);

        // Center Panel - Split pane cho ghế trống và vé đã đặt
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Left - Danh sách ghế trống
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Ghế Trống"));

        String[] columnNamesGhe = {"Mã Ghế", "Số Ghế", "Hàng", "Loại Ghế"};
        tableModelGhe = new DefaultTableModel(columnNamesGhe, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableGhe = new JTable(tableModelGhe);
        tableGhe.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollGhe = new JScrollPane(tableGhe);
        leftPanel.add(scrollGhe, BorderLayout.CENTER);

        btnDatVe = new JButton("Đặt Vé");
        btnDatVe.addActionListener(e -> datVe());
        leftPanel.add(btnDatVe, BorderLayout.SOUTH);

        // Right - Danh sách vé đã đặt (hóa đơn tạm)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Vé Đã Đặt"));

        String[] columnNamesVe = {"Mã Vé", "Số Ghế", "Loại Ghế", "Giá Vé"};
        tableModelVeDaDat = new DefaultTableModel(columnNamesVe, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableVeDaDat = new JTable(tableModelVeDaDat);
        JScrollPane scrollVe = new JScrollPane(tableVeDaDat);
        rightPanel.add(scrollVe, BorderLayout.CENTER);

        JPanel rightBottomPanel = new JPanel(new BorderLayout());
        btnXoaVe = new JButton("Xóa Vé");
        btnXoaVe.addActionListener(e -> xoaVe());
        rightBottomPanel.add(btnXoaVe, BorderLayout.WEST);

        lblTongTien = new JLabel("Tổng Tiền: 0 VND", SwingConstants.CENTER);
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        rightBottomPanel.add(lblTongTien, BorderLayout.CENTER);

        btnThanhToan = new JButton("Thanh Toán");
        btnThanhToan.addActionListener(e -> thanhToan());
        rightBottomPanel.add(btnThanhToan, BorderLayout.EAST);

        rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(600);

        add(splitPane, BorderLayout.CENTER);
    }

    private void loadComboBoxes() {
        // Load lịch chiếu
        cboLichChieu.removeAllItems();
        List<LichChieu> danhSachLichChieu = lichChieuDAO.layDanhSachLichChieu();
        for (LichChieu lc : danhSachLichChieu) {
            String item = lc.getMaLichChieu() + " - " + lc.getPhim().getTenPhim() +
                         " - " + lc.getPhongChieu().getTenPhong() +
                         " - " + lc.getNgayChieu() + " " + lc.getGioChieu();
            cboLichChieu.addItem(item);
        }

        // Load khách hàng
        cboKhachHang.removeAllItems();
        List<KhachHang> danhSachKhachHang = khachHangDAO.layDanhSachKhachHang();
        for (KhachHang kh : danhSachKhachHang) {
            cboKhachHang.addItem(kh.getMaKhachHang() + " - " + kh.getTenKhachHang() + " - " + kh.getSoDienThoai());
        }
    }

    private int extractId(String comboBoxItem) {
        if (comboBoxItem == null || comboBoxItem.isEmpty()) return -1;
        String[] parts = comboBoxItem.split(" - ");
        return Integer.parseInt(parts[0]);
    }

    private void loadGheTrong() {
        tableModelGhe.setRowCount(0);

        if (cboLichChieu.getSelectedItem() == null) return;

        int maLichChieu = extractId(cboLichChieu.getSelectedItem().toString());
        lichChieuHienTai = lichChieuDAO.timLichChieuTheoMa(maLichChieu);

        if (lichChieuHienTai != null) {
            // Tạo hóa đơn mới nếu chưa có
            if (hoaDonHienTai == null && cboKhachHang.getSelectedItem() != null) {
                taoHoaDonMoi();
            }

            int maPhong = lichChieuHienTai.getPhongChieu().getMaPhong();
            List<Ghe> danhSachGhe = gheDAO.layDanhSachGheTheoPhong(maPhong);

            for (Ghe ghe : danhSachGhe) {
                // Kiểm tra ghế đã đặt chưa
                if (!veDAO.kiemTraGheDaDat(maLichChieu, ghe.getMaGhe())) {
                    Object[] row = {
                        ghe.getMaGhe(),
                        ghe.getSoGhe(),
                        ghe.getHang(),
                        ghe.getLoaiGhe()
                    };
                    tableModelGhe.addRow(row);
                }
            }
        }
    }

    private void taoHoaDonMoi() {
        if (cboKhachHang.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maKhachHang = extractId(cboKhachHang.getSelectedItem().toString());
        KhachHang kh = khachHangDAO.timKhachHangTheoMa(maKhachHang);

        hoaDonHienTai = new HoaDon();
        hoaDonHienTai.setKhachHang(kh);
        hoaDonHienTai.setNgayLap(Timestamp.valueOf(LocalDateTime.now()));
        hoaDonHienTai.setTongTien(0.0);
        hoaDonHienTai.setTrangThaiThanhToan("Chua thanh toan");

        int maHoaDon = hoaDonDAO.themHoaDon(hoaDonHienTai);
        if (maHoaDon > 0) {
            hoaDonHienTai.setMaHoaDon(maHoaDon);
        } else {
            JOptionPane.showMessageDialog(this, "Không thể tạo hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void datVe() {
        if (hoaDonHienTai == null) {
            taoHoaDonMoi();
        }

        if (lichChieuHienTai == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch chiếu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[] selectedRows = tableGhe.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ghế!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int row : selectedRows) {
            int maGhe = Integer.parseInt(tableModelGhe.getValueAt(row, 0).toString());
            double giaVe = lichChieuHienTai.getGiaVe();

            if (veDAO.datVe(lichChieuHienTai.getMaLichChieu(), maGhe, hoaDonHienTai.getMaHoaDon(), giaVe)) {
                // Success
            } else {
                JOptionPane.showMessageDialog(this, "Đặt vé thất bại cho ghế " + tableModelGhe.getValueAt(row, 1), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        loadGheTrong();
        loadVeDaDat();
    }

    private void loadVeDaDat() {
        tableModelVeDaDat.setRowCount(0);

        if (hoaDonHienTai == null) return;

        List<Ve> danhSachVe = veDAO.layVeTheoHoaDon(hoaDonHienTai.getMaHoaDon());
        double tongTien = 0;

        for (Ve ve : danhSachVe) {
            Object[] row = {
                ve.getMaVe(),
                ve.getGhe().getSoGhe(),
                ve.getGhe().getLoaiGhe(),
                ve.getGiaVe()
            };
            tableModelVeDaDat.addRow(row);
            tongTien += ve.getGiaVe();
        }

        lblTongTien.setText("Tổng Tiền: " + String.format("%,.0f", tongTien) + " VND");
    }

    private void xoaVe() {
        int selectedRow = tableVeDaDat.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vé cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maVe = Integer.parseInt(tableModelVeDaDat.getValueAt(selectedRow, 0).toString());

        int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa vé này?", "Xác Nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (veDAO.huyVe(maVe)) {
                JOptionPane.showMessageDialog(this, "Hủy vé thành công!");
                loadGheTrong();
                loadVeDaDat();
            } else {
                JOptionPane.showMessageDialog(this, "Hủy vé thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void thanhToan() {
        if (hoaDonHienTai == null) {
            JOptionPane.showMessageDialog(this, "Chưa có vé nào được đặt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán hóa đơn?", "Thanh Toán", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (hoaDonDAO.thanhToanHoaDon(hoaDonHienTai.getMaHoaDon())) {
                JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
                // Reset
                hoaDonHienTai = null;
                tableModelVeDaDat.setRowCount(0);
                lblTongTien.setText("Tổng Tiền: 0 VND");
                loadGheTrong();
            } else {
                JOptionPane.showMessageDialog(this, "Thanh toán thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
