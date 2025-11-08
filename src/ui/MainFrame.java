package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("Hệ Thống Quản Lý Rạp Chiếu Phim");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // Menu Quản lý
        JMenu menuQuanLy = new JMenu("Quản Lý");
        JMenuItem itemPhim = new JMenuItem("Quản Lý Phim");
        JMenuItem itemPhongChieu = new JMenuItem("Quản Lý Phòng Chiếu");
        JMenuItem itemLichChieu = new JMenuItem("Quản Lý Lịch Chiếu");
        JMenuItem itemKhachHang = new JMenuItem("Quản Lý Khách Hàng");

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
        JMenuItem itemDatVe = new JMenuItem("Đặt Vé");
        JMenuItem itemHoaDon = new JMenuItem("Quản Lý Hóa Đơn");

        itemDatVe.addActionListener(e -> openDatVeFrame());
        itemHoaDon.addActionListener(e -> openHoaDonFrame());

        menuBanVe.add(itemDatVe);
        menuBanVe.add(itemHoaDon);

        // Menu Hệ thống
        JMenu menuHeThong = new JMenu("Hệ Thống");
        JMenuItem itemThoat = new JMenuItem("Thoát");
        itemThoat.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn thoát?",
                "Xác Nhận",
                JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        menuHeThong.add(itemThoat);

        menuBar.add(menuQuanLy);
        menuBar.add(menuBanVe);
        menuBar.add(menuHeThong);
        setJMenuBar(menuBar);

        // Content Panel
        contentPanel = new JPanel(new BorderLayout());

        // Welcome Panel
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(new Color(240, 240, 240));

        JLabel lblWelcome = new JLabel("HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 28));
        lblWelcome.setForeground(new Color(51, 51, 51));
        welcomePanel.add(lblWelcome, BorderLayout.CENTER);

        // Quick Access Panel
        JPanel quickAccessPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        quickAccessPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        quickAccessPanel.setBackground(new Color(240, 240, 240));

        // Buttons
        JButton btnPhim = createQuickAccessButton("Quản Lý Phim", "Thêm, sửa, xóa phim");
        JButton btnPhongChieu = createQuickAccessButton("Phòng Chiếu", "Quản lý phòng chiếu");
        JButton btnLichChieu = createQuickAccessButton("Lịch Chiếu", "Lập lịch chiếu phim");
        JButton btnDatVe = createQuickAccessButton("Đặt Vé", "Đặt vé xem phim");
        JButton btnKhachHang = createQuickAccessButton("Khách Hàng", "Quản lý khách hàng");
        JButton btnHoaDon = createQuickAccessButton("Hóa Đơn", "Quản lý hóa đơn");

        btnPhim.addActionListener(e -> openPhimFrame());
        btnPhongChieu.addActionListener(e -> openPhongChieuFrame());
        btnLichChieu.addActionListener(e -> openLichChieuFrame());
        btnDatVe.addActionListener(e -> openDatVeFrame());
        btnKhachHang.addActionListener(e -> openKhachHangFrame());
        btnHoaDon.addActionListener(e -> openHoaDonFrame());

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

    private JButton createQuickAccessButton(String title, String description) {
        JButton button = new JButton("<html><center><b>" + title + "</b><br><small>" + description + "</small></center></html>");
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(66, 133, 244));
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(66, 133, 244), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(51, 103, 214));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(66, 133, 244));
            }
        });

        return button;
    }

    private void openPhimFrame() {
        PhimFrame frame = new PhimFrame();
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
        DatVeFrame frame = new DatVeFrame();
        frame.setVisible(true);
    }

    private void openHoaDonFrame() {
        HoaDonFrame frame = new HoaDonFrame();
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
