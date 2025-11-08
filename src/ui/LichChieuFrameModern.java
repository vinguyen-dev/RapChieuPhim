package ui;

import dao.*;
import entity.*;
import util.Constants;
import util.ExcelExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Modern screening schedule management with optional JCalendar support
 */
public class LichChieuFrameModern extends JFrame {
    private LichChieuDAO lichChieuDAO;
    private PhimDAO phimDAO;
    private PhongChieuDAO phongChieuDAO;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtMaLichChieu, txtGiaVe;
    private JComboBox<String> cboPhim, cboPhongChieu, cboGioChieu;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnExport;

    // JCalendar component (will be initialized if available)
    private Object dateChooser; // JDateChooser from com.toedter.calendar
    private JTextField txtNgayChieuFallback; // Fallback if JCalendar not available

    public LichChieuFrameModern() {
        lichChieuDAO = LichChieuDAO.getInstance();
        phimDAO = PhimDAO.getInstance();
        phongChieuDAO = PhongChieuDAO.getInstance();

        setTitle("Quản Lý Lịch Chiếu - Screening Schedule");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadData();

        // Fade in animation
        SwingUtilities.invokeLater(() -> AnimationUtils.fadeIn(getContentPane(), 300));
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(UIStyles.BG_PRIMARY);

        // Top panel
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Center - Table
        JPanel centerPanel = createTablePanel();
        add(centerPanel, BorderLayout.CENTER);

        // Bottom - Form
        JPanel bottomPanel = createFormPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        UIStyles.styleCard(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JLabel lblTitle = new JLabel("Lịch Chiếu Phim");
        lblTitle.setIcon(IconFactory.createCalendarIcon(32, UIStyles.PRIMARY_COLOR));
        lblTitle.setFont(UIStyles.FONT_TITLE);
        lblTitle.setForeground(UIStyles.TEXT_PRIMARY);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        btnExport = new JButton("Xuất Excel");
        btnExport.setIcon(IconFactory.createInvoiceIcon(16, Color.WHITE));
        UIStyles.styleSuccessButton(btnExport);
        btnExport.addActionListener(e -> exportToExcel());

        buttonPanel.add(btnExport);

        panel.add(lblTitle, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        UIStyles.styleCard(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columns = {"Mã", "Phim", "Phòng", "Ngày Chiếu", "Giờ Chiếu", "Giá Vé"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        UIStyles.styleTable(table);

        // Apply modern renderers
        table.setDefaultRenderer(Object.class, new ModernTableCellRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new CurrencyCellRenderer());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        UIStyles.styleCard(panel);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 15, 15, 15),
            BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, UIStyles.PRIMARY_COLOR),
                "Thông Tin Lịch Chiếu",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                UIStyles.FONT_HEADER,
                UIStyles.PRIMARY_DARK
            )
        ));

        // Use GridBagLayout for better compatibility
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1
        JLabel lblMa = new JLabel("Mã Lịch Chiếu:");
        lblMa.setFont(UIStyles.FONT_NORMAL);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(lblMa, gbc);

        txtMaLichChieu = new JTextField();
        UIStyles.styleTextField(txtMaLichChieu);
        txtMaLichChieu.setEditable(false);
        txtMaLichChieu.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        panel.add(txtMaLichChieu, gbc);

        JLabel lblPhim = new JLabel("Phim:");
        lblPhim.setFont(UIStyles.FONT_NORMAL);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(lblPhim, gbc);

        cboPhim = new JComboBox<>();
        UIStyles.styleComboBox(cboPhim);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        panel.add(cboPhim, gbc);

        // Row 2
        JLabel lblPhong = new JLabel("Phòng Chiếu:");
        lblPhong.setFont(UIStyles.FONT_NORMAL);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(lblPhong, gbc);

        cboPhongChieu = new JComboBox<>();
        UIStyles.styleComboBox(cboPhongChieu);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        panel.add(cboPhongChieu, gbc);

        JLabel lblNgay = new JLabel("Ngày Chiếu:");
        lblNgay.setFont(UIStyles.FONT_NORMAL);
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(lblNgay, gbc);

        // Try to use JCalendar if available
        try {
            Class<?> jDateChooserClass = Class.forName("com.toedter.calendar.JDateChooser");
            dateChooser = jDateChooserClass.getDeclaredConstructor().newInstance();

            // Set date format
            jDateChooserClass.getMethod("setDateFormatString", String.class)
                .invoke(dateChooser, "dd/MM/yyyy");

            // Style
            if (dateChooser instanceof JComponent) {
                JComponent jc = (JComponent) dateChooser;
                jc.setFont(UIStyles.FONT_NORMAL);
                jc.setPreferredSize(new Dimension(200, 36));
                gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
                panel.add(jc, gbc);
            }

            System.out.println("✓ JCalendar date picker initialized");

        } catch (ClassNotFoundException e) {
            // JCalendar not available, use text field
            txtNgayChieuFallback = new JTextField();
            UIStyles.styleTextField(txtNgayChieuFallback);
            txtNgayChieuFallback.setToolTipText("Format: dd/MM/yyyy");
            gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
            panel.add(txtNgayChieuFallback, gbc);

            System.out.println("ℹ JCalendar not found. Using text field for date input.");
        } catch (Exception e) {
            e.printStackTrace();
            txtNgayChieuFallback = new JTextField();
            UIStyles.styleTextField(txtNgayChieuFallback);
            gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
            panel.add(txtNgayChieuFallback, gbc);
        }

        // Row 3
        JLabel lblGio = new JLabel("Giờ Chiếu:");
        lblGio.setFont(UIStyles.FONT_NORMAL);
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(lblGio, gbc);

        cboGioChieu = new JComboBox<>(Constants.GIO_CHIEU);
        UIStyles.styleComboBox(cboGioChieu);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        panel.add(cboGioChieu, gbc);

        JLabel lblGia = new JLabel("Giá Vé:");
        lblGia.setFont(UIStyles.FONT_NORMAL);
        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(lblGia, gbc);

        txtGiaVe = new JTextField();
        UIStyles.styleTextField(txtGiaVe);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1.0;
        panel.add(txtGiaVe, gbc);

        // Row 4 - Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);

        btnThem = new JButton("Thêm");
        btnThem.setIcon(IconFactory.createAddIcon(16, Color.WHITE));
        UIStyles.styleSuccessButton(btnThem);
        btnThem.addActionListener(e -> themLichChieu());

        btnSua = new JButton("Sửa");
        btnSua.setIcon(IconFactory.createEditIcon(16, Color.WHITE));
        UIStyles.stylePrimaryButton(btnSua);
        btnSua.addActionListener(e -> suaLichChieu());

        btnXoa = new JButton("Xóa");
        btnXoa.setIcon(IconFactory.createDeleteIcon(16, Color.WHITE));
        UIStyles.styleErrorButton(btnXoa);
        btnXoa.addActionListener(e -> xoaLichChieu());

        btnLamMoi = new JButton("Làm Mới");
        btnLamMoi.setIcon(IconFactory.createRefreshIcon(16, UIStyles.PRIMARY_COLOR));
        UIStyles.styleSecondaryButton(btnLamMoi);
        btnLamMoi.addActionListener(e -> lamMoi());

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);

        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 3; gbc.weightx = 1.0;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private void loadData() {
        // Load movies
        cboPhim.removeAllItems();
        List<Phim> phims = phimDAO.layDanhSachPhim();
        for (Phim p : phims) {
            cboPhim.addItem(p.getMaPhim() + " - " + p.getTenPhim());
        }

        // Load theaters
        cboPhongChieu.removeAllItems();
        List<PhongChieu> phongs = phongChieuDAO.layDanhSachPhongChieu();
        for (PhongChieu pc : phongs) {
            cboPhongChieu.addItem(pc.getMaPhong() + " - " + pc.getTenPhong());
        }

        // Load screenings
        tableModel.setRowCount(0);
        List<LichChieu> lichChieus = lichChieuDAO.layDanhSachLichChieu();
        for (LichChieu lc : lichChieus) {
            Object[] row = {
                lc.getMaLichChieu(),
                lc.getPhim().getTenPhim(),
                lc.getPhongChieu().getTenPhong(),
                new SimpleDateFormat("dd/MM/yyyy").format(lc.getNgayChieu()),
                lc.getGioChieu().toString(),
                lc.getGiaVe()
            };
            tableModel.addRow(row);
        }
    }

    private void displaySelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            txtMaLichChieu.setText(tableModel.getValueAt(selectedRow, 0).toString());

            String tenPhim = tableModel.getValueAt(selectedRow, 1).toString();
            for (int i = 0; i < cboPhim.getItemCount(); i++) {
                if (cboPhim.getItemAt(i).contains(tenPhim)) {
                    cboPhim.setSelectedIndex(i);
                    break;
                }
            }

            String tenPhong = tableModel.getValueAt(selectedRow, 2).toString();
            for (int i = 0; i < cboPhongChieu.getItemCount(); i++) {
                if (cboPhongChieu.getItemAt(i).contains(tenPhong)) {
                    cboPhongChieu.setSelectedIndex(i);
                    break;
                }
            }

            String ngayStr = tableModel.getValueAt(selectedRow, 3).toString();
            if (dateChooser != null) {
                try {
                    java.util.Date date = new SimpleDateFormat("dd/MM/yyyy").parse(ngayStr);
                    dateChooser.getClass().getMethod("setDate", java.util.Date.class)
                        .invoke(dateChooser, date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (txtNgayChieuFallback != null) {
                txtNgayChieuFallback.setText(ngayStr);
            }

            String gio = tableModel.getValueAt(selectedRow, 4).toString();
            cboGioChieu.setSelectedItem(gio.substring(0, 5)); // Extract HH:mm

            txtGiaVe.setText(tableModel.getValueAt(selectedRow, 5).toString());
        }
    }

    private void themLichChieu() {
        try {
            if (cboPhim.getSelectedItem() == null || cboPhongChieu.getSelectedItem() == null) {
                UIStyles.showErrorMessage(this, "Vui lòng chọn phim và phòng chiếu!");
                AnimationUtils.shake(cboPhim);
                return;
            }

            int maPhim = extractId(cboPhim.getSelectedItem().toString());
            int maPhong = extractId(cboPhongChieu.getSelectedItem().toString());

            // Get date
            java.util.Date utilDate = null;
            if (dateChooser != null) {
                utilDate = (java.util.Date) dateChooser.getClass().getMethod("getDate").invoke(dateChooser);
            } else if (txtNgayChieuFallback != null) {
                utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(txtNgayChieuFallback.getText().trim());
            }

            if (utilDate == null) {
                UIStyles.showErrorMessage(this, "Vui lòng chọn ngày chiếu!");
                return;
            }

            Date ngayChieu = new Date(utilDate.getTime());
            Time gioChieu = Time.valueOf(cboGioChieu.getSelectedItem().toString() + ":00");
            double giaVe = Double.parseDouble(txtGiaVe.getText().trim());

            Phim phim = new Phim();
            phim.setMaPhim(maPhim);

            PhongChieu phongChieu = new PhongChieu();
            phongChieu.setMaPhong(maPhong);

            LichChieu lichChieu = new LichChieu();
            lichChieu.setPhim(phim);
            lichChieu.setPhongChieu(phongChieu);
            lichChieu.setNgayChieu(ngayChieu);
            lichChieu.setGioChieu(gioChieu);
            lichChieu.setGiaVe(giaVe);

            if (lichChieuDAO.themLichChieu(lichChieu)) {
                UIStyles.showSuccessMessage(this, "Thêm lịch chiếu thành công!");
                loadData();
                lamMoi();
            } else {
                UIStyles.showErrorMessage(this, "Thêm lịch chiếu thất bại!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            UIStyles.showErrorMessage(this, "Dữ liệu không hợp lệ!");
            AnimationUtils.shake(txtGiaVe);
        }
    }

    private void suaLichChieu() {
        try {
            if (txtMaLichChieu.getText().trim().isEmpty()) {
                UIStyles.showErrorMessage(this, "Vui lòng chọn lịch chiếu cần sửa!");
                AnimationUtils.shake(table);
                return;
            }

            int maLichChieu = Integer.parseInt(txtMaLichChieu.getText().trim());
            int maPhim = extractId(cboPhim.getSelectedItem().toString());
            int maPhong = extractId(cboPhongChieu.getSelectedItem().toString());

            java.util.Date utilDate = null;
            if (dateChooser != null) {
                utilDate = (java.util.Date) dateChooser.getClass().getMethod("getDate").invoke(dateChooser);
            } else if (txtNgayChieuFallback != null) {
                utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(txtNgayChieuFallback.getText().trim());
            }

            Date ngayChieu = new Date(utilDate.getTime());
            Time gioChieu = Time.valueOf(cboGioChieu.getSelectedItem().toString() + ":00");
            double giaVe = Double.parseDouble(txtGiaVe.getText().trim());

            Phim phim = new Phim();
            phim.setMaPhim(maPhim);

            PhongChieu phongChieu = new PhongChieu();
            phongChieu.setMaPhong(maPhong);

            LichChieu lichChieu = new LichChieu();
            lichChieu.setMaLichChieu(maLichChieu);
            lichChieu.setPhim(phim);
            lichChieu.setPhongChieu(phongChieu);
            lichChieu.setNgayChieu(ngayChieu);
            lichChieu.setGioChieu(gioChieu);
            lichChieu.setGiaVe(giaVe);

            if (lichChieuDAO.capNhatLichChieu(lichChieu)) {
                UIStyles.showSuccessMessage(this, "Cập nhật lịch chiếu thành công!");
                loadData();
                lamMoi();
            } else {
                UIStyles.showErrorMessage(this, "Cập nhật lịch chiếu thất bại!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            UIStyles.showErrorMessage(this, "Dữ liệu không hợp lệ!");
        }
    }

    private void xoaLichChieu() {
        if (txtMaLichChieu.getText().trim().isEmpty()) {
            UIStyles.showErrorMessage(this, "Vui lòng chọn lịch chiếu cần xóa!");
            return;
        }

        if (UIStyles.showConfirmDialog(this, "Bạn có chắc muốn xóa lịch chiếu này?")) {
            int maLichChieu = Integer.parseInt(txtMaLichChieu.getText().trim());

            if (lichChieuDAO.xoaLichChieu(maLichChieu)) {
                UIStyles.showSuccessMessage(this, "Xóa lịch chiếu thành công!");
                loadData();
                lamMoi();
            } else {
                UIStyles.showErrorMessage(this, "Xóa lịch chiếu thất bại!");
            }
        }
    }

    private void lamMoi() {
        txtMaLichChieu.setText("");
        txtGiaVe.setText("");
        cboPhim.setSelectedIndex(0);
        cboPhongChieu.setSelectedIndex(0);
        cboGioChieu.setSelectedIndex(0);

        if (dateChooser != null) {
            try {
                dateChooser.getClass().getMethod("setDate", java.util.Date.class)
                    .invoke(dateChooser, (java.util.Date) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (txtNgayChieuFallback != null) {
            txtNgayChieuFallback.setText("");
        }

        table.clearSelection();
    }

    private void exportToExcel() {
        ExcelExporter.exportTableWithDialog(table, this, "LichChieu");
    }

    private int extractId(String comboBoxItem) {
        return Integer.parseInt(comboBoxItem.split(" - ")[0]);
    }
}
