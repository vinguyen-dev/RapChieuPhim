package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("Hệ Thống Quản Lý Rạp Chiếu Phim - Cinema Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(UIStyles.BG_PRIMARY);

        initComponents();
    }

    private void initComponents() {
        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(UIStyles.BG_SECONDARY);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIStyles.PRIMARY_COLOR));
        menuBar.setFont(UIStyles.FONT_NORMAL);

        // Menu Quản lý
        JMenu menuQuanLy = new JMenu("Quản Lý");
        menuQuanLy.setFont(UIStyles.FONT_SUBHEADER);

        JMenuItem itemPhim = new JMenuItem("Quản Lý Phim");
        JMenuItem itemPhongChieu = new JMenuItem("Quản Lý Phòng Chiếu");
        JMenuItem itemLichChieu = new JMenuItem("Quản Lý Lịch Chiếu");
        JMenuItem itemKhachHang = new JMenuItem("Quản Lý Khách Hàng");

        itemPhim.setFont(UIStyles.FONT_NORMAL);
        itemPhongChieu.setFont(UIStyles.FONT_NORMAL);
        itemLichChieu.setFont(UIStyles.FONT_NORMAL);
        itemKhachHang.setFont(UIStyles.FONT_NORMAL);

        itemPhim.addActionListener(e -> openPhimFrame());
        itemPhongChieu.addActionListener(e -> openPhongChieuFrame());
        itemLichChieu.addActionListener(e -> openLichChieuFrame());
        itemKhachHang.addActionListener(e -> openKhachHangFrame());

        menuQuanLy.add(itemPhim);
        menuQuanLy.add(itemPhongChieu);
        menuQuanLy.add(itemLichChieu);
        menuQuanLy.add(itemKhachHang);

        // Menu Bán vé
        JMenu menuBanVe = new JMenu("Bán Vé");
        menuBanVe.setFont(UIStyles.FONT_SUBHEADER);

        JMenuItem itemDatVe = new JMenuItem("Đặt Vé");
        JMenuItem itemHoaDon = new JMenuItem("Quản Lý Hóa Đơn");

        itemDatVe.setFont(UIStyles.FONT_NORMAL);
        itemHoaDon.setFont(UIStyles.FONT_NORMAL);

        itemDatVe.addActionListener(e -> openDatVeFrame());
        itemHoaDon.addActionListener(e -> openHoaDonFrame());

        menuBanVe.add(itemDatVe);
        menuBanVe.add(itemHoaDon);

        // Menu Hệ thống
        JMenu menuHeThong = new JMenu("Hệ Thống");
        menuHeThong.setFont(UIStyles.FONT_SUBHEADER);

        JMenuItem itemDashboard = new JMenuItem("Dashboard & Thống Kê");
        itemDashboard.setFont(UIStyles.FONT_NORMAL);
        itemDashboard.addActionListener(e -> openDashboard());

        JMenuItem itemThoat = new JMenuItem("Thoát");
        itemThoat.setFont(UIStyles.FONT_NORMAL);
        itemThoat.addActionListener(e -> {
            if (UIStyles.showConfirmDialog(this, "Bạn có chắc muốn thoát?")) {
                System.exit(0);
            }
        });

        menuHeThong.add(itemDashboard);
        menuHeThong.addSeparator();
        menuHeThong.add(itemThoat);

        menuBar.add(menuQuanLy);
        menuBar.add(menuBanVe);
        menuBar.add(menuHeThong);
        setJMenuBar(menuBar);

        // Content Panel with gradient background
        contentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, UIStyles.BG_PRIMARY, 0, getHeight(), new Color(240, 248, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setOpaque(false);

        // Welcome Panel with modern header
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));

        JLabel lblWelcome = new JLabel("HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM", SwingConstants.CENTER);
        UIStyles.styleTitleLabel(lblWelcome);
        lblWelcome.setForeground(UIStyles.PRIMARY_DARK);

        JLabel lblSubtitle = new JLabel("Cinema Management System - Quản lý chuyên nghiệp, hiệu quả", SwingConstants.CENTER);
        lblSubtitle.setFont(UIStyles.FONT_NORMAL);
        lblSubtitle.setForeground(UIStyles.TEXT_SECONDARY);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setOpaque(false);
        headerPanel.add(lblWelcome);
        headerPanel.add(lblSubtitle);

        welcomePanel.add(headerPanel, BorderLayout.CENTER);

        // Quick Access Panel with modern cards
        JPanel quickAccessPanel = new JPanel(new GridLayout(2, 3, 25, 25));
        quickAccessPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 80, 80));
        quickAccessPanel.setOpaque(false);

        // Create modern card buttons
        JPanel btnPhim = createModernCard("PHIM", "Quản Lý Phim", "Thêm, sửa, xóa phim", UIStyles.PRIMARY_COLOR, e -> openPhimFrame());
        JPanel btnPhongChieu = createModernCard("PHÒNG", "Phòng Chiếu", "Quản lý phòng chiếu", new Color(156, 39, 176), e -> openPhongChieuFrame());
        JPanel btnLichChieu = createModernCard("LỊCH", "Lịch Chiếu", "Lập lịch chiếu phim", new Color(0, 150, 136), e -> openLichChieuFrame());
        JPanel btnDatVe = createModernCard("VÉ", "Đặt Vé", "Đặt vé xem phim", UIStyles.ACCENT_COLOR, e -> openDatVeFrame());
        JPanel btnKhachHang = createModernCard("KHÁCH", "Khách Hàng", "Quản lý khách hàng", new Color(63, 81, 181), e -> openKhachHangFrame());
        JPanel btnHoaDon = createModernCard("HĐ", "Hóa Đơn", "Quản lý hóa đơn", UIStyles.SUCCESS_COLOR, e -> openHoaDonFrame());

        quickAccessPanel.add(btnPhim);
        quickAccessPanel.add(btnPhongChieu);
        quickAccessPanel.add(btnLichChieu);
        quickAccessPanel.add(btnDatVe);
        quickAccessPanel.add(btnKhachHang);
        quickAccessPanel.add(btnHoaDon);

        contentPanel.add(welcomePanel, BorderLayout.NORTH);
        contentPanel.add(quickAccessPanel, BorderLayout.CENTER);

        add(contentPanel);
    }

    private JPanel createModernCard(String icon, String title, String description, Color accentColor, java.awt.event.ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 15, 15);

                // Draw card background
                g2d.setColor(UIStyles.BG_SECONDARY);
                g2d.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 15, 15);

                // Draw accent top border
                g2d.setColor(accentColor);
                g2d.fillRoundRect(0, 0, getWidth() - 8, 8, 15, 15);

                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon label with text
        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblIcon.setForeground(accentColor);

        // Title label
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(UIStyles.FONT_HEADER);
        lblTitle.setForeground(UIStyles.TEXT_PRIMARY);

        // Description label
        JLabel lblDesc = new JLabel("<html><center>" + description + "</center></html>", SwingConstants.CENTER);
        lblDesc.setFont(UIStyles.FONT_SMALL);
        lblDesc.setForeground(UIStyles.TEXT_SECONDARY);

        // Layout
        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        contentPanel.setOpaque(false);
        contentPanel.add(lblIcon);
        contentPanel.add(lblTitle);
        contentPanel.add(lblDesc);

        card.add(contentPanel, BorderLayout.CENTER);

        // Click handler
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.actionPerformed(new java.awt.event.ActionEvent(card, 0, ""));
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    BorderFactory.createEmptyBorder(13, 18, 18, 18)
                ));
                lblTitle.setForeground(accentColor);
                lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 36));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
                lblTitle.setForeground(UIStyles.TEXT_PRIMARY);
                lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 32));
            }
        });

        return card;
    }

    private void openPhimFrame() {
        PhimFrameModern frame = new PhimFrameModern();
        frame.setVisible(true);
    }

    private void openPhongChieuFrame() {
        PhongChieuFrame frame = new PhongChieuFrame();
        frame.setVisible(true);
    }

    private void openLichChieuFrame() {
        LichChieuFrame frame = new LichChieuFrame();
        frame.setVisible(true);
    }

    private void openKhachHangFrame() {
        KhachHangFrame frame = new KhachHangFrame();
        frame.setVisible(true);
    }

    private void openDatVeFrame() {
        DatVeFrameModern frame = new DatVeFrameModern();
        frame.setVisible(true);
    }

    private void openHoaDonFrame() {
        HoaDonFrame frame = new HoaDonFrame();
        frame.setVisible(true);
    }

    private void openDashboard() {
        DashboardFrame frame = new DashboardFrame();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
