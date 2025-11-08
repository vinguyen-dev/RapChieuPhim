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

        // Modern Welcome Panel with interactive cards
        WelcomePanel welcomePanel = new WelcomePanel(this);
        contentPanel.add(welcomePanel, BorderLayout.CENTER);

        add(contentPanel);
    }

    public void openPhimFrame() {
        PhimFrameModern frame = new PhimFrameModern();
        frame.setVisible(true);
    }

    public void openPhongChieuFrame() {
        PhongChieuFrame frame = new PhongChieuFrame();
        frame.setVisible(true);
    }

    public void openLichChieuFrame() {
        LichChieuFrameModern frame = new LichChieuFrameModern();
        frame.setVisible(true);
    }

    public void openKhachHangFrame() {
        KhachHangFrame frame = new KhachHangFrame();
        frame.setVisible(true);
    }

    public void openDatVeFrame() {
        DatVeFrameModern frame = new DatVeFrameModern();
        frame.setVisible(true);
    }

    public void openHoaDonFrame() {
        HoaDonFrame frame = new HoaDonFrame();
        frame.setVisible(true);
    }

    public void openDashboard() {
        DashboardFrame frame = new DashboardFrame();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            // Try to use FlatLaf if available
            try {
                Class<?> flatLafClass = Class.forName("com.formdev.flatlaf.FlatLightLaf");
                UIManager.setLookAndFeel((javax.swing.LookAndFeel) flatLafClass.getDeclaredConstructor().newInstance());

                // FlatLaf customizations for modern appearance
                UIManager.put("Button.arc", 8);
                UIManager.put("Component.arc", 8);
                UIManager.put("ProgressBar.arc", 8);
                UIManager.put("TextComponent.arc", 8);
                UIManager.put("ScrollBar.thumbArc", 999);
                UIManager.put("ScrollBar.thumbInsets", new java.awt.Insets(2, 2, 2, 2));
                UIManager.put("Table.showHorizontalLines", false);
                UIManager.put("Table.showVerticalLines", false);
                UIManager.put("Table.intercellSpacing", new java.awt.Dimension(0, 0));

                System.out.println("✓ FlatLaf initialized successfully!");
            } catch (ClassNotFoundException e) {
                // FlatLaf not available, use system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                System.out.println("ℹ FlatLaf not found. Using system Look & Feel.");
                System.out.println("  To enable modern UI, add FlatLaf library:");
                System.out.println("  com.formdev:flatlaf:3.2.5");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
