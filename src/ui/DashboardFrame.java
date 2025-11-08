package ui;

import dao.ThongKeDAO;
import util.Constants;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;

/**
 * Dashboard with statistics and charts
 */
public class DashboardFrame extends JFrame {
    private ThongKeDAO thongKeDAO;

    private JLabel lblTongDoanhThu, lblTongVe, lblTongKhachHang, lblTongPhim;
    private BarChart chartDoanhThuPhim, chartSoVePhim, chartDoanhThuTheLoai;

    public DashboardFrame() {
        thongKeDAO = ThongKeDAO.getInstance();

        setTitle("Dashboard - Thống Kê & Báo Cáo");
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(UIStyles.BG_PRIMARY);

        // Top panel - Summary cards
        JPanel topPanel = createSummaryPanel();
        add(topPanel, BorderLayout.NORTH);

        // Center - Charts
        JPanel centerPanel = createChartsPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Bottom - Refresh button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        JButton btnRefresh = new JButton("Làm Mới");
        btnRefresh.setIcon(IconFactory.createRefreshIcon(16, Color.WHITE));
        UIStyles.stylePrimaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> loadData());
        bottomPanel.add(btnRefresh);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Revenue card
        JPanel cardRevenue = createSummaryCard(
            "Tổng Doanh Thu",
            "0 VNĐ",
            IconFactory.createChartIcon(48, UIStyles.SUCCESS_COLOR),
            UIStyles.SUCCESS_COLOR
        );
        lblTongDoanhThu = (JLabel) ((JPanel) cardRevenue.getComponent(1)).getComponent(1);

        // Tickets card
        JPanel cardTickets = createSummaryCard(
            "Vé Đã Bán",
            "0 vé",
            IconFactory.createTicketIcon(48, UIStyles.PRIMARY_COLOR),
            UIStyles.PRIMARY_COLOR
        );
        lblTongVe = (JLabel) ((JPanel) cardTickets.getComponent(1)).getComponent(1);

        // Customers card
        JPanel cardCustomers = createSummaryCard(
            "Khách Hàng",
            "0 khách",
            IconFactory.createPeopleIcon(48, UIStyles.ACCENT_COLOR),
            UIStyles.ACCENT_COLOR
        );
        lblTongKhachHang = (JLabel) ((JPanel) cardCustomers.getComponent(1)).getComponent(1);

        // Movies card
        JPanel cardMovies = createSummaryCard(
            "Số Phim",
            "0 phim",
            IconFactory.createMovieIcon(48, new Color(156, 39, 176)),
            new Color(156, 39, 176)
        );
        lblTongPhim = (JLabel) ((JPanel) cardMovies.getComponent(1)).getComponent(1);

        panel.add(cardRevenue);
        panel.add(cardTickets);
        panel.add(cardCustomers);
        panel.add(cardMovies);

        return panel;
    }

    private JPanel createSummaryCard(String title, String value, Icon icon, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(UIStyles.BG_SECONDARY);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Icon panel
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        // Text panel
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIStyles.FONT_NORMAL);
        lblTitle.setForeground(UIStyles.TEXT_SECONDARY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValue.setForeground(accentColor);

        textPanel.add(lblTitle);
        textPanel.add(lblValue);

        card.add(lblIcon, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    BorderFactory.createEmptyBorder(19, 19, 19, 19)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
        });

        return card;
    }

    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Top row - Revenue by movie
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        chartDoanhThuPhim = new BarChart("Top 10 Phim Theo Doanh Thu", "Phim", "Doanh thu (VNĐ)");
        chartDoanhThuPhim.setBarColor(UIStyles.SUCCESS_COLOR);
        JPanel chartPanel1 = wrapInPanel(chartDoanhThuPhim, "Doanh Thu Theo Phim");
        gbc.weighty = 1.2;
        panel.add(chartPanel1, gbc);

        // Bottom left - Tickets by movie
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 1.0;
        chartSoVePhim = new BarChart("Top 10 Phim Phổ Biến", "Phim", "Số vé");
        chartSoVePhim.setBarColor(UIStyles.PRIMARY_COLOR);
        JPanel chartPanel2 = wrapInPanel(chartSoVePhim, "Số Vé Bán Ra");
        panel.add(chartPanel2, gbc);

        // Bottom right - Revenue by genre
        gbc.gridx = 1;
        gbc.gridy = 1;
        chartDoanhThuTheLoai = new BarChart("Doanh Thu Theo Thể Loại", "Thể loại", "Doanh thu (VNĐ)");
        chartDoanhThuTheLoai.setBarColor(UIStyles.ACCENT_COLOR);
        JPanel chartPanel3 = wrapInPanel(chartDoanhThuTheLoai, "Thể Loại Phổ Biến");
        panel.add(chartPanel3, gbc);

        return panel;
    }

    private JPanel wrapInPanel(Component component, String title) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UIStyles.BG_SECONDARY);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                UIStyles.FONT_SUBHEADER,
                UIStyles.TEXT_PRIMARY
            )
        ));
        wrapper.add(component, BorderLayout.CENTER);
        return wrapper;
    }

    private void loadData() {
        // Load summary data
        double tongDoanhThu = thongKeDAO.getTongDoanhThu();
        int tongVe = thongKeDAO.getTongVeDaBan();
        int tongKhachHang = thongKeDAO.getTongKhachHang();
        int tongPhim = thongKeDAO.getTongPhim();

        lblTongDoanhThu.setText(Constants.formatCurrency(tongDoanhThu));
        lblTongVe.setText(tongVe + " vé");
        lblTongKhachHang.setText(tongKhachHang + " khách");
        lblTongPhim.setText(tongPhim + " phim");

        // Load chart data
        Map<String, Double> doanhThuPhim = thongKeDAO.getDoanhThuTheoPhim();
        Map<String, Double> soVePhim = thongKeDAO.getSoVeTheoPhim();
        Map<String, Double> doanhThuTheLoai = thongKeDAO.getDoanhThuTheoTheLoai();

        chartDoanhThuPhim.setData(doanhThuPhim);
        chartSoVePhim.setData(soVePhim);
        chartDoanhThuTheLoai.setData(doanhThuTheLoai);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            DashboardFrame frame = new DashboardFrame();
            frame.setVisible(true);
        });
    }
}
