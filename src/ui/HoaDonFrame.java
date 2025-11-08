package ui;

import dao.HoaDonDAO;
import dao.VeDAO;
import entity.HoaDon;
import entity.Ve;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HoaDonFrame extends JFrame {
    private JTable tableHoaDon, tableChiTiet;
    private DefaultTableModel tableModelHoaDon, tableModelChiTiet;
    private HoaDonDAO hoaDonDAO;
    private VeDAO veDAO;

    private JComboBox<String> cboTrangThai;
    private JButton btnLoc, btnThanhToan, btnHuy, btnLamMoi;
    private JLabel lblTongTien;

    public HoaDonFrame() {
        hoaDonDAO = HoaDonDAO.getInstance();
        veDAO = VeDAO.getInstance();

        setTitle("Quản Lý Hóa Đơn");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Top Panel - Lọc theo trạng thái
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createTitledBorder("Lọc Hóa Đơn"));

        topPanel.add(new JLabel("Trạng thái:"));
        cboTrangThai = new JComboBox<>(new String[]{"Tất cả", "Chua thanh toan", "Da thanh toan", "Huy"});
        topPanel.add(cboTrangThai);

        btnLoc = new JButton("Lọc");
        btnLoc.addActionListener(e -> locHoaDon());
        topPanel.add(btnLoc);

        btnLamMoi = new JButton("Làm Mới");
        btnLamMoi.addActionListener(e -> loadData());
        topPanel.add(btnLamMoi);

        add(topPanel, BorderLayout.NORTH);

        // Center - Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        // Top - Danh sách hóa đơn
        JPanel panelHoaDon = new JPanel(new BorderLayout());
        panelHoaDon.setBorder(BorderFactory.createTitledBorder("Danh Sách Hóa Đơn"));

        String[] columnNamesHoaDon = {"Mã HĐ", "Khách Hàng", "SĐT", "Ngày Lập", "Tổng Tiền", "Trạng Thái"};
        tableModelHoaDon = new DefaultTableModel(columnNamesHoaDon, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableHoaDon = new JTable(tableModelHoaDon);
        tableHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadChiTietHoaDon();
            }
        });

        JScrollPane scrollHoaDon = new JScrollPane(tableHoaDon);
        panelHoaDon.add(scrollHoaDon, BorderLayout.CENTER);

        // Bottom - Chi tiết hóa đơn
        JPanel panelChiTiet = new JPanel(new BorderLayout());
        panelChiTiet.setBorder(BorderFactory.createTitledBorder("Chi Tiết Hóa Đơn"));

        String[] columnNamesChiTiet = {"Mã Vé", "Phim", "Phòng", "Ngày Chiếu", "Giờ Chiếu", "Số Ghế", "Loại Ghế", "Giá Vé"};
        tableModelChiTiet = new DefaultTableModel(columnNamesChiTiet, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableChiTiet = new JTable(tableModelChiTiet);
        JScrollPane scrollChiTiet = new JScrollPane(tableChiTiet);
        panelChiTiet.add(scrollChiTiet, BorderLayout.CENTER);

        // Bottom panel với tổng tiền và nút
        JPanel bottomControlPanel = new JPanel(new BorderLayout());

        lblTongTien = new JLabel("Tổng Tiền: 0 VND", SwingConstants.CENTER);
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongTien.setForeground(new Color(220, 53, 69));
        bottomControlPanel.add(lblTongTien, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnThanhToan = new JButton("Thanh Toán");
        btnHuy = new JButton("Hủy Hóa Đơn");

        btnThanhToan.addActionListener(e -> thanhToanHoaDon());
        btnHuy.addActionListener(e -> huyHoaDon());

        buttonPanel.add(btnThanhToan);
        buttonPanel.add(btnHuy);

        bottomControlPanel.add(buttonPanel, BorderLayout.SOUTH);

        panelChiTiet.add(bottomControlPanel, BorderLayout.SOUTH);

        splitPane.setTopComponent(panelHoaDon);
        splitPane.setBottomComponent(panelChiTiet);
        splitPane.setDividerLocation(300);

        add(splitPane, BorderLayout.CENTER);
    }

    private void loadData() {
        tableModelHoaDon.setRowCount(0);
        List<HoaDon> danhSachHoaDon = hoaDonDAO.layDanhSachHoaDon();
        for (HoaDon hd : danhSachHoaDon) {
            Object[] row = {
                hd.getMaHoaDon(),
                hd.getKhachHang().getTenKhachHang(),
                hd.getKhachHang().getSoDienThoai(),
                hd.getNgayLap(),
                String.format("%,.0f", hd.getTongTien()),
                hd.getTrangThaiThanhToan()
            };
            tableModelHoaDon.addRow(row);
        }
    }

    private void loadChiTietHoaDon() {
        tableModelChiTiet.setRowCount(0);
        lblTongTien.setText("Tổng Tiền: 0 VND");

        int selectedRow = tableHoaDon.getSelectedRow();
        if (selectedRow < 0) return;

        int maHoaDon = Integer.parseInt(tableModelHoaDon.getValueAt(selectedRow, 0).toString());
        List<Ve> danhSachVe = veDAO.layVeTheoHoaDon(maHoaDon);

        double tongTien = 0;
        for (Ve ve : danhSachVe) {
            Object[] row = {
                ve.getMaVe(),
                ve.getLichChieu().getPhim().getTenPhim(),
                ve.getLichChieu().getPhongChieu().getTenPhong(),
                ve.getLichChieu().getNgayChieu(),
                ve.getLichChieu().getGioChieu(),
                ve.getGhe().getSoGhe(),
                ve.getGhe().getLoaiGhe(),
                String.format("%,.0f", ve.getGiaVe())
            };
            tableModelChiTiet.addRow(row);
            tongTien += ve.getGiaVe();
        }

        lblTongTien.setText("Tổng Tiền: " + String.format("%,.0f", tongTien) + " VND");
    }

    private void locHoaDon() {
        String trangThai = cboTrangThai.getSelectedItem().toString();

        tableModelHoaDon.setRowCount(0);
        List<HoaDon> danhSachHoaDon;

        if (trangThai.equals("Tất cả")) {
            danhSachHoaDon = hoaDonDAO.layDanhSachHoaDon();
        } else {
            danhSachHoaDon = hoaDonDAO.layHoaDonTheoTrangThai(trangThai);
        }

        for (HoaDon hd : danhSachHoaDon) {
            Object[] row = {
                hd.getMaHoaDon(),
                hd.getKhachHang().getTenKhachHang(),
                hd.getKhachHang().getSoDienThoai(),
                hd.getNgayLap(),
                String.format("%,.0f", hd.getTongTien()),
                hd.getTrangThaiThanhToan()
            };
            tableModelHoaDon.addRow(row);
        }
    }

    private void thanhToanHoaDon() {
        int selectedRow = tableHoaDon.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maHoaDon = Integer.parseInt(tableModelHoaDon.getValueAt(selectedRow, 0).toString());
        String trangThai = tableModelHoaDon.getValueAt(selectedRow, 5).toString();

        if (trangThai.equals("Da thanh toan")) {
            JOptionPane.showMessageDialog(this, "Hóa đơn đã được thanh toán!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (trangThai.equals("Huy")) {
            JOptionPane.showMessageDialog(this, "Hóa đơn đã bị hủy!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán hóa đơn?", "Thanh Toán", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (hoaDonDAO.thanhToanHoaDon(maHoaDon)) {
                JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
                loadData();
                tableModelChiTiet.setRowCount(0);
                lblTongTien.setText("Tổng Tiền: 0 VND");
            } else {
                JOptionPane.showMessageDialog(this, "Thanh toán thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void huyHoaDon() {
        int selectedRow = tableHoaDon.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maHoaDon = Integer.parseInt(tableModelHoaDon.getValueAt(selectedRow, 0).toString());
        String trangThai = tableModelHoaDon.getValueAt(selectedRow, 5).toString();

        if (trangThai.equals("Da thanh toan")) {
            JOptionPane.showMessageDialog(this, "Không thể hủy hóa đơn đã thanh toán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (trangThai.equals("Huy")) {
            JOptionPane.showMessageDialog(this, "Hóa đơn đã bị hủy!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn hủy hóa đơn này?", "Xác Nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (hoaDonDAO.huyHoaDon(maHoaDon)) {
                JOptionPane.showMessageDialog(this, "Hủy hóa đơn thành công!");
                loadData();
                tableModelChiTiet.setRowCount(0);
                lblTongTien.setText("Tổng Tiền: 0 VND");
            } else {
                JOptionPane.showMessageDialog(this, "Hủy hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
