package ui;

import dao.*;
import entity.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

/**
 * Modern redesigned ticket booking frame with visual seat selection
 */
public class DatVeFrameModern extends JFrame {
    private JComboBox<String> cboLichChieu, cboKhachHang;
    private JPanel seatPanel;
    private JTable tableVeDaDat;
    private DefaultTableModel tableModelVeDaDat;
    private JButton btnThanhToan, btnReset;
    private JLabel lblTongTien, lblMovieInfo, lblScreenInfo;

    private LichChieuDAO lichChieuDAO;
    private KhachHangDAO khachHangDAO;
    private GheDAO gheDAO;
    private VeDAO veDAO;
    private HoaDonDAO hoaDonDAO;

    private HoaDon hoaDonHienTai;
    private LichChieu lichChieuHienTai;
    private Map<Integer, SeatButton> seatButtons = new HashMap<>();
    private List<Integer> selectedSeats = new ArrayList<>();

    public DatVeFrameModern() {
        lichChieuDAO = LichChieuDAO.getInstance();
        khachHangDAO = KhachHangDAO.getInstance();
        gheDAO = GheDAO.getInstance();
        veDAO = VeDAO.getInstance();
        hoaDonDAO = HoaDonDAO.getInstance();

        setTitle("🎟️ Đặt Vé Xem Phim - Ticket Booking");
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadComboBoxes();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(UIStyles.BG_PRIMARY);

        // Top Panel - Movie and Customer Selection
        JPanel topPanel = createSelectionPanel();
        add(topPanel, BorderLayout.NORTH);

        // Center - Main content with seat selection and booking summary
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(850);
        splitPane.setBorder(null);
        splitPane.setOpaque(false);

        // Left - Seat Selection
        JPanel leftPanel = createSeatSelectionPanel();
        splitPane.setLeftComponent(leftPanel);

        // Right - Booking Summary
        JPanel rightPanel = createBookingSummaryPanel();
        splitPane.setRightComponent(rightPanel);

        add(splitPane, BorderLayout.CENTER);

        // Bottom - Legend and Actions
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        UIStyles.styleCard(panel);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 15, 10, 15),
            BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, UIStyles.PRIMARY_COLOR),
                "📋 Thông Tin Đặt Vé",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                UIStyles.FONT_HEADER,
                UIStyles.PRIMARY_DARK
            )
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Lịch chiếu
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblLichChieu = new JLabel("🎬 Lịch Chiếu:");
        lblLichChieu.setFont(UIStyles.FONT_SUBHEADER);
        panel.add(lblLichChieu, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        cboLichChieu = new JComboBox<>();
        UIStyles.styleComboBox(cboLichChieu);
        cboLichChieu.setPreferredSize(new Dimension(500, 36));
        cboLichChieu.addActionListener(e -> onLichChieuSelected());
        panel.add(cboLichChieu, gbc);

        // Khách hàng
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lblKhachHang = new JLabel("👤 Khách Hàng:");
        lblKhachHang.setFont(UIStyles.FONT_SUBHEADER);
        panel.add(lblKhachHang, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        cboKhachHang = new JComboBox<>();
        UIStyles.styleComboBox(cboKhachHang);
        cboKhachHang.setPreferredSize(new Dimension(500, 36));
        panel.add(cboKhachHang, gbc);

        // Movie info display
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        lblMovieInfo = new JLabel(" ");
        lblMovieInfo.setFont(UIStyles.FONT_NORMAL);
        lblMovieInfo.setForeground(UIStyles.TEXT_SECONDARY);
        panel.add(lblMovieInfo, gbc);

        return panel;
    }

    private JPanel createSeatSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        UIStyles.styleCard(panel);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 10),
            BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, UIStyles.PRIMARY_COLOR),
                "🎭 Chọn Ghế Ngồi",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                UIStyles.FONT_HEADER,
                UIStyles.PRIMARY_DARK
            )
        ));

        // Screen indicator
        JPanel screenPanel = new JPanel(new BorderLayout());
        screenPanel.setOpaque(false);
        screenPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JLabel lblScreen = new JLabel("🎬 MÀN HÌNH CHIẾU", SwingConstants.CENTER);
        lblScreen.setFont(UIStyles.FONT_HEADER);
        lblScreen.setForeground(UIStyles.TEXT_WHITE);
        lblScreen.setOpaque(true);
        lblScreen.setBackground(UIStyles.PRIMARY_DARK);
        lblScreen.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyles.PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        lblScreenInfo = new JLabel("Vui lòng chọn lịch chiếu", SwingConstants.CENTER);
        lblScreenInfo.setFont(UIStyles.FONT_SMALL);
        lblScreenInfo.setForeground(UIStyles.TEXT_SECONDARY);

        screenPanel.add(lblScreen, BorderLayout.NORTH);
        screenPanel.add(lblScreenInfo, BorderLayout.CENTER);

        // Seat grid
        seatPanel = new JPanel(new GridBagLayout());
        seatPanel.setBackground(UIStyles.BG_SECONDARY);
        JScrollPane scrollPane = new JScrollPane(seatPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UIStyles.BG_SECONDARY);

        panel.add(screenPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBookingSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        UIStyles.styleCard(panel);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 10, 15, 15),
            BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, UIStyles.ACCENT_COLOR),
                "🎫 Vé Đã Chọn",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                UIStyles.FONT_HEADER,
                UIStyles.ACCENT_COLOR
            )
        ));

        // Table for selected tickets
        String[] columns = {"Ghế", "Hàng", "Loại", "Giá"};
        tableModelVeDaDat = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableVeDaDat = new JTable(tableModelVeDaDat);
        UIStyles.styleTable(tableVeDaDat);

        JScrollPane scrollPane = new JScrollPane(tableVeDaDat);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1));

        // Total panel
        JPanel totalPanel = new JPanel(new BorderLayout(10, 10));
        totalPanel.setOpaque(false);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        lblTongTien = new JLabel("Tổng Tiền: 0 VNĐ", SwingConstants.CENTER);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongTien.setForeground(UIStyles.ACCENT_COLOR);
        lblTongTien.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyles.ACCENT_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        totalPanel.add(lblTongTien, BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        actionPanel.setOpaque(false);

        btnThanhToan = new JButton("💳 Thanh Toán");
        UIStyles.styleSuccessButton(btnThanhToan);
        btnThanhToan.setPreferredSize(UIStyles.LARGE_BUTTON_SIZE);
        btnThanhToan.setFont(UIStyles.FONT_SUBHEADER);
        btnThanhToan.addActionListener(e -> thanhToan());

        btnReset = new JButton("🔄 Làm Mới");
        UIStyles.styleSecondaryButton(btnReset);
        btnReset.setPreferredSize(UIStyles.LARGE_BUTTON_SIZE);
        btnReset.setFont(UIStyles.FONT_SUBHEADER);
        btnReset.addActionListener(e -> resetSelection());

        actionPanel.add(btnThanhToan);
        actionPanel.add(btnReset);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(totalPanel, BorderLayout.SOUTH);
        panel.add(actionPanel, BorderLayout.PAGE_END);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        legendPanel.setBackground(UIStyles.BG_SECONDARY);
        legendPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        legendPanel.add(createLegendItem("Trống", UIStyles.SEAT_AVAILABLE));
        legendPanel.add(createLegendItem("Đã chọn", UIStyles.SEAT_SELECTED));
        legendPanel.add(createLegendItem("Đã đặt", UIStyles.SEAT_OCCUPIED));
        legendPanel.add(createLegendItem("VIP (+20%)", UIStyles.SEAT_VIP));
        legendPanel.add(createLegendItem("💑 Couple (x2)", new Color(233, 30, 99)));

        panel.add(legendPanel, BorderLayout.CENTER);

        // Pricing info panel
        JPanel pricingPanel = new JPanel();
        pricingPanel.setLayout(new BoxLayout(pricingPanel, BoxLayout.Y_AXIS));
        pricingPanel.setBackground(new Color(255, 248, 225));
        pricingPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 193, 7), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblPricingTitle = new JLabel("💰 Bảng Giá");
        lblPricingTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPricingTitle.setForeground(new Color(230, 126, 34));
        lblPricingTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPricingInfo = new JLabel("<html>" +
            "• Ghế Thường: Giá cơ bản<br>" +
            "• Ghế VIP: Giá cơ bản + 20%<br>" +
            "• Ghế Couple: Giá cơ bản × 2" +
            "</html>");
        lblPricingInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPricingInfo.setForeground(new Color(51, 51, 51));
        lblPricingInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        pricingPanel.add(lblPricingTitle);
        pricingPanel.add(Box.createVerticalStrut(5));
        pricingPanel.add(lblPricingInfo);

        panel.add(pricingPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createLegendItem(String label, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setOpaque(false);

        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(30, 30));
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

        JLabel lblText = new JLabel(label);
        lblText.setFont(UIStyles.FONT_NORMAL);

        item.add(colorBox);
        item.add(lblText);

        return item;
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

    private void onLichChieuSelected() {
        if (cboLichChieu.getSelectedItem() == null) return;

        int maLichChieu = extractId(cboLichChieu.getSelectedItem().toString());
        lichChieuHienTai = lichChieuDAO.timLichChieuTheoMa(maLichChieu);

        if (lichChieuHienTai != null) {
            // Update movie info
            lblMovieInfo.setText(String.format("📽️ %s | ⏱️ %d phút | 🏷️ %s | 💰 %.0f VNĐ",
                lichChieuHienTai.getPhim().getTenPhim(),
                lichChieuHienTai.getPhim().getThoiLuong(),
                lichChieuHienTai.getPhim().getTheLoai(),
                lichChieuHienTai.getGiaVe()));

            lblScreenInfo.setText(String.format("Phòng: %s | Ngày: %s | Giờ: %s",
                lichChieuHienTai.getPhongChieu().getTenPhong(),
                lichChieuHienTai.getNgayChieu(),
                lichChieuHienTai.getGioChieu()));

            loadSeats();
            resetSelection();
        }
    }

    private void loadSeats() {
        seatPanel.removeAll();
        seatButtons.clear();

        if (lichChieuHienTai == null) return;

        int maPhong = lichChieuHienTai.getPhongChieu().getMaPhong();

        // Kiểm tra và tạo ghế tự động nếu phòng chưa có ghế
        gheDAO.kiemTraVaTaoGhe(maPhong);

        List<Ghe> danhSachGhe = gheDAO.layDanhSachGheTheoPhong(maPhong);

        // Group seats by row
        Map<String, List<Ghe>> seatsByRow = new TreeMap<>();
        for (Ghe ghe : danhSachGhe) {
            seatsByRow.computeIfAbsent(ghe.getHang(), k -> new ArrayList<>()).add(ghe);
        }

        // Sort seats by seat number (numeric)
        for (List<Ghe> seats : seatsByRow.values()) {
            seats.sort((g1, g2) -> {
                try {
                    // Extract numeric part from soGhe (e.g., "A1" -> 1, "A10" -> 10)
                    String num1 = g1.getSoGhe().replaceAll("[^0-9]", "");
                    String num2 = g2.getSoGhe().replaceAll("[^0-9]", "");
                    int n1 = num1.isEmpty() ? 0 : Integer.parseInt(num1);
                    int n2 = num2.isEmpty() ? 0 : Integer.parseInt(num2);
                    return Integer.compare(n1, n2);
                } catch (NumberFormatException e) {
                    return g1.getSoGhe().compareTo(g2.getSoGhe());
                }
            });
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;
        for (Map.Entry<String, List<Ghe>> entry : seatsByRow.entrySet()) {
            String rowName = entry.getKey();
            List<Ghe> seats = entry.getValue();

            // Row label
            gbc.gridx = 0;
            gbc.gridy = row;
            JLabel lblRow = new JLabel(rowName);
            lblRow.setFont(UIStyles.FONT_SUBHEADER);
            lblRow.setForeground(UIStyles.TEXT_PRIMARY);
            seatPanel.add(lblRow, gbc);

            // Seats in row
            int columnIndex = 1; // Start from column 1 (after row label)
            for (Ghe ghe : seats) {
                boolean isOccupied = veDAO.kiemTraGheDaDat(lichChieuHienTai.getMaLichChieu(), ghe.getMaGhe());

                SeatButton.SeatStatus status;
                if (isOccupied) {
                    status = SeatButton.SeatStatus.OCCUPIED;
                } else if ("VIP".equalsIgnoreCase(ghe.getLoaiGhe())) {
                    status = SeatButton.SeatStatus.VIP_AVAILABLE;
                } else if ("Couple".equalsIgnoreCase(ghe.getLoaiGhe())) {
                    status = SeatButton.SeatStatus.COUPLE_AVAILABLE;
                } else {
                    status = SeatButton.SeatStatus.AVAILABLE;
                }

                SeatButton seatBtn = new SeatButton(ghe, status);
                seatBtn.addActionListener(e -> toggleSeat(seatBtn));

                gbc.gridx = columnIndex;

                // Couple seats occupy 2 columns
                if ("Couple".equalsIgnoreCase(ghe.getLoaiGhe())) {
                    gbc.gridwidth = 2;
                    columnIndex += 2;
                } else {
                    gbc.gridwidth = 1;
                    columnIndex += 1;
                }

                seatPanel.add(seatBtn, gbc);
                seatButtons.put(ghe.getMaGhe(), seatBtn);
            }

            row++;
        }

        seatPanel.revalidate();
        seatPanel.repaint();
    }

    private void toggleSeat(SeatButton seatBtn) {
        if (seatBtn.getStatus() == SeatButton.SeatStatus.OCCUPIED) {
            return; // Cannot select occupied seat
        }

        int maGhe = seatBtn.getGhe().getMaGhe();

        if (selectedSeats.contains(maGhe)) {
            // Deselect
            selectedSeats.remove(Integer.valueOf(maGhe));
            if (seatBtn.getStatus() == SeatButton.SeatStatus.VIP_SELECTED) {
                seatBtn.setStatus(SeatButton.SeatStatus.VIP_AVAILABLE);
            } else if (seatBtn.getStatus() == SeatButton.SeatStatus.COUPLE_SELECTED) {
                seatBtn.setStatus(SeatButton.SeatStatus.COUPLE_AVAILABLE);
            } else {
                seatBtn.setStatus(SeatButton.SeatStatus.AVAILABLE);
            }
        } else {
            // Select
            selectedSeats.add(maGhe);
            if (seatBtn.getStatus() == SeatButton.SeatStatus.VIP_AVAILABLE) {
                seatBtn.setStatus(SeatButton.SeatStatus.VIP_SELECTED);
            } else if (seatBtn.getStatus() == SeatButton.SeatStatus.COUPLE_AVAILABLE) {
                seatBtn.setStatus(SeatButton.SeatStatus.COUPLE_SELECTED);
            } else {
                seatBtn.setStatus(SeatButton.SeatStatus.SELECTED);
            }
        }

        updateBookingSummary();
    }

    private void updateBookingSummary() {
        tableModelVeDaDat.setRowCount(0);
        double tongTien = 0;
        double giaCoban = lichChieuHienTai.getGiaVe();

        for (int maGhe : selectedSeats) {
            SeatButton btn = seatButtons.get(maGhe);
            if (btn != null) {
                Ghe ghe = btn.getGhe();
                double giaVe = tinhGiaVe(giaCoban, ghe.getLoaiGhe());

                Object[] row = {
                    ghe.getSoGhe(),
                    ghe.getHang(),
                    ghe.getLoaiGhe(),
                    String.format("%.0f VNĐ", giaVe)
                };
                tableModelVeDaDat.addRow(row);
                tongTien += giaVe;
            }
        }

        lblTongTien.setText(String.format("Tổng Tiền: %.0f VNĐ", tongTien));
    }

    /**
     * Tính giá vé dựa trên loại ghế
     * - Ghế Thường: giá cơ bản
     * - Ghế VIP: giá cơ bản + 20%
     * - Ghế Couple: giá cơ bản x 2
     *
     * @param giaCoBan Giá vé cơ bản từ lịch chiếu
     * @param loaiGhe Loại ghế (Thuong, VIP, Couple)
     * @return Giá vé đã được điều chỉnh
     */
    private double tinhGiaVe(double giaCoBan, String loaiGhe) {
        if (loaiGhe == null) return giaCoBan;

        switch (loaiGhe) {
            case "VIP":
                return giaCoBan * 1.2; // VIP: +20%
            case "Couple":
                return giaCoBan * 2.0; // Couple: x2
            case "Thuong":
            default:
                return giaCoBan; // Regular price
        }
    }

    private void resetSelection() {
        for (SeatButton btn : seatButtons.values()) {
            if (btn.getStatus() == SeatButton.SeatStatus.SELECTED) {
                btn.setStatus(SeatButton.SeatStatus.AVAILABLE);
            } else if (btn.getStatus() == SeatButton.SeatStatus.VIP_SELECTED) {
                btn.setStatus(SeatButton.SeatStatus.VIP_AVAILABLE);
            }
        }

        selectedSeats.clear();
        hoaDonHienTai = null;
        updateBookingSummary();
    }

    private void thanhToan() {
        if (selectedSeats.isEmpty()) {
            UIStyles.showErrorMessage(this, "Vui lòng chọn ít nhất một ghế!");
            return;
        }

        if (cboKhachHang.getSelectedItem() == null) {
            UIStyles.showErrorMessage(this, "Vui lòng chọn khách hàng!");
            return;
        }

        // Create invoice
        if (hoaDonHienTai == null) {
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
                UIStyles.showErrorMessage(this, "Không thể tạo hóa đơn!");
                return;
            }
        }

        // Book seats with dynamic pricing based on seat type
        boolean success = true;
        double giaCoban = lichChieuHienTai.getGiaVe(); // Base price

        for (int maGhe : selectedSeats) {
            // Get seat info to determine price
            SeatButton seatBtn = seatButtons.get(maGhe);
            if (seatBtn == null) continue;

            Ghe ghe = seatBtn.getGhe();
            double giaVe = tinhGiaVe(giaCoban, ghe.getLoaiGhe());

            if (!veDAO.datVe(lichChieuHienTai.getMaLichChieu(), maGhe, hoaDonHienTai.getMaHoaDon(), giaVe)) {
                success = false;
                break;
            }
        }

        if (success) {
            // Complete payment
            if (hoaDonDAO.thanhToanHoaDon(hoaDonHienTai.getMaHoaDon())) {
                UIStyles.showSuccessMessage(this, "Thanh toán thành công!\nMã hóa đơn: " + hoaDonHienTai.getMaHoaDon());

                // Ask if user wants to print invoice
                int option = JOptionPane.showConfirmDialog(
                    this,
                    "Thanh toán thành công!\nBạn có muốn in hóa đơn không?",
                    "In Hóa Đơn",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (option == JOptionPane.YES_OPTION) {
                    inHoaDon(hoaDonHienTai.getMaHoaDon());
                }

                resetSelection();
                loadSeats(); // Reload to show newly occupied seats
            } else {
                UIStyles.showErrorMessage(this, "Thanh toán thất bại!");
            }
        } else {
            UIStyles.showErrorMessage(this, "Đặt vé thất bại!");
        }
    }

    /**
     * In hóa đơn sau khi đặt vé thành công
     */
    private void inHoaDon(int maHoaDon) {
        // Get invoice details
        HoaDon hoaDon = hoaDonDAO.timHoaDonTheoMa(maHoaDon);
        if (hoaDon == null) {
            UIStyles.showErrorMessage(this, "Không tìm thấy hóa đơn!");
            return;
        }

        // Get ticket details
        List<Ve> danhSachVe = veDAO.layVeTheoHoaDon(maHoaDon);
        if (danhSachVe.isEmpty()) {
            UIStyles.showErrorMessage(this, "Hóa đơn không có chi tiết!");
            return;
        }

        // Create invoice content
        StringBuilder content = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        content.append("═══════════════════════════════════════════════════════════\n");
        content.append("                    HÓA ĐƠN BÁN VÉ                        \n");
        content.append("              HỆ THỐNG RẠP CHIẾU PHIM                     \n");
        content.append("═══════════════════════════════════════════════════════════\n\n");
        content.append(String.format("Mã Hóa Đơn: %d%n", hoaDon.getMaHoaDon()));
        content.append(String.format("Ngày Lập: %s%n", sdf.format(hoaDon.getNgayLap())));
        content.append(String.format("Khách Hàng: %s%n", hoaDon.getKhachHang().getTenKhachHang()));
        content.append(String.format("Số Điện Thoại: %s%n", hoaDon.getKhachHang().getSoDienThoai()));
        content.append(String.format("Trạng Thái: %s%n%n", hoaDon.getTrangThaiThanhToan()));
        content.append("───────────────────────────────────────────────────────────\n");
        content.append("                     CHI TIẾT VÉ                          \n");
        content.append("───────────────────────────────────────────────────────────\n\n");

        double tongTien = 0;
        SimpleDateFormat dateSdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < danhSachVe.size(); i++) {
            Ve ve = danhSachVe.get(i);
            content.append(String.format("Vé %d:%n", i + 1));
            content.append(String.format("  Phim: %s%n", ve.getLichChieu().getPhim().getTenPhim()));
            content.append(String.format("  Phòng: %s | Ghế: %s (%s)%n",
                ve.getLichChieu().getPhongChieu().getTenPhong(),
                ve.getGhe().getSoGhe(),
                ve.getGhe().getLoaiGhe()));
            content.append(String.format("  Ngày: %s | Giờ: %s%n",
                dateSdf.format(ve.getLichChieu().getNgayChieu()),
                ve.getLichChieu().getGioChieu()));
            content.append(String.format("  Giá: %,.0f VNĐ%n%n", ve.getGiaVe()));
            tongTien += ve.getGiaVe();
        }

        content.append("═══════════════════════════════════════════════════════════\n");
        content.append(String.format("TỔNG TIỀN: %,.0f VNĐ%n", tongTien));
        content.append("═══════════════════════════════════════════════════════════\n\n");
        content.append("            Cảm ơn quý khách! Hẹn gặp lại!              \n");
        content.append("═══════════════════════════════════════════════════════════\n");

        // Show preview and print dialog
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
            try {
                boolean complete = textArea.print();
                if (complete) {
                    UIStyles.showSuccessMessage(this, "In hóa đơn thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "In hóa đơn bị hủy!", "Thông Báo", JOptionPane.WARNING_MESSAGE);
                }
            } catch (PrinterException ex) {
                UIStyles.showErrorMessage(this, "Lỗi khi in: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
