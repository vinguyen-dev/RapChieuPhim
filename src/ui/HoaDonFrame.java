package ui;

import dao.HoaDonDAO;
import dao.VeDAO;
import entity.HoaDon;
import entity.Ve;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.*;
import java.util.List;

public class HoaDonFrame extends JFrame {
    private JTable tableHoaDon, tableChiTiet;
    private DefaultTableModel tableModelHoaDon, tableModelChiTiet;
    private HoaDonDAO hoaDonDAO;
    private VeDAO veDAO;

    private JComboBox<String> cboTrangThai;
    private JButton btnLoc, btnThanhToan, btnHuy, btnLamMoi, btnInHoaDon;
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
        btnInHoaDon = new JButton("In Hóa Đơn");

        btnThanhToan.addActionListener(e -> thanhToanHoaDon());
        btnHuy.addActionListener(e -> huyHoaDon());
        btnInHoaDon.addActionListener(e -> inHoaDon());

        buttonPanel.add(btnThanhToan);
        buttonPanel.add(btnHuy);
        buttonPanel.add(btnInHoaDon);

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

    /**
     * In hóa đơn được chọn
     */
    private void inHoaDon() {
        int selectedRow = tableHoaDon.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần in!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tableModelChiTiet.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Hóa đơn không có chi tiết!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy thông tin hóa đơn
        int maHoaDon = Integer.parseInt(tableModelHoaDon.getValueAt(selectedRow, 0).toString());
        String tenKhachHang = tableModelHoaDon.getValueAt(selectedRow, 1).toString();
        String sdt = tableModelHoaDon.getValueAt(selectedRow, 2).toString();
        String ngayLap = tableModelHoaDon.getValueAt(selectedRow, 3).toString();
        String tongTien = tableModelHoaDon.getValueAt(selectedRow, 4).toString();
        String trangThai = tableModelHoaDon.getValueAt(selectedRow, 5).toString();

        // Tạo nội dung in
        StringBuilder content = new StringBuilder();
        content.append("═══════════════════════════════════════════════════════════\n");
        content.append("                    HÓA ĐƠN BÁN VÉ                        \n");
        content.append("              HỆ THỐNG RẠP CHIẾU PHIM                     \n");
        content.append("═══════════════════════════════════════════════════════════\n\n");
        content.append(String.format("Mã Hóa Đơn: %s%n", maHoaDon));
        content.append(String.format("Ngày Lập: %s%n", ngayLap));
        content.append(String.format("Khách Hàng: %s%n", tenKhachHang));
        content.append(String.format("Số Điện Thoại: %s%n", sdt));
        content.append(String.format("Trạng Thái: %s%n%n", trangThai));
        content.append("───────────────────────────────────────────────────────────\n");
        content.append("                     CHI TIẾT VÉ                          \n");
        content.append("───────────────────────────────────────────────────────────\n\n");

        // Thêm chi tiết vé
        for (int i = 0; i < tableModelChiTiet.getRowCount(); i++) {
            String phim = tableModelChiTiet.getValueAt(i, 1).toString();
            String phong = tableModelChiTiet.getValueAt(i, 2).toString();
            String ngayChieu = tableModelChiTiet.getValueAt(i, 3).toString();
            String gioChieu = tableModelChiTiet.getValueAt(i, 4).toString();
            String soGhe = tableModelChiTiet.getValueAt(i, 5).toString();
            String loaiGhe = tableModelChiTiet.getValueAt(i, 6).toString();
            String giaVe = tableModelChiTiet.getValueAt(i, 7).toString();

            content.append(String.format("Vé %d:%n", i + 1));
            content.append(String.format("  Phim: %s%n", phim));
            content.append(String.format("  Phòng: %s | Ghế: %s (%s)%n", phong, soGhe, loaiGhe));
            content.append(String.format("  Ngày: %s | Giờ: %s%n", ngayChieu, gioChieu));
            content.append(String.format("  Giá: %s VNĐ%n%n", giaVe));
        }

        content.append("═══════════════════════════════════════════════════════════\n");
        content.append(String.format("TỔNG TIỀN: %s VNĐ%n", tongTien));
        content.append("═══════════════════════════════════════════════════════════\n\n");
        content.append("            Cảm ơn quý khách! Hẹn gặp lại!              \n");
        content.append("═══════════════════════════════════════════════════════════\n");

        // Hiển thị hộp thoại xác nhận in
        JTextArea textArea = new JTextArea(content.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500));

        int option = JOptionPane.showConfirmDialog(
            this,
            scrollPane,
            "Xem Trước Hóa Đơn - Xác Nhận In",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            // In hóa đơn
            try {
                boolean complete = textArea.print();
                if (complete) {
                    JOptionPane.showMessageDialog(this, "In hóa đơn thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "In hóa đơn bị hủy!", "Thông Báo", JOptionPane.WARNING_MESSAGE);
                }
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi in: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
