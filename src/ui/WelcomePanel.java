package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modern welcome panel with quick actions and statistics preview
 */
public class WelcomePanel extends JPanel {
    private MainFrame mainFrame;

    public WelcomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setOpaque(false);

        initComponents();
    }

    private void initComponents() {
        // Header section
        JPanel headerPanel = createHeaderPanel();

        // Quick actions section
        JPanel quickActionsPanel = createQuickActionsPanel();

        // Main container
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setOpaque(false);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        mainContainer.add(headerPanel);
        mainContainer.add(Box.createVerticalStrut(40));
        mainContainer.add(quickActionsPanel);

        add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Welcome message
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        String greeting = hour < 12 ? "Chào buổi sáng" :
                         hour < 18 ? "Chào buổi chiều" : "Chào buổi tối";

        JLabel lblGreeting = new JLabel(greeting + "!");
        lblGreeting.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblGreeting.setForeground(ModernUIStyles.TEXT_PRIMARY);
        lblGreeting.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Chào mừng bạn đến với hệ thống quản lý rạp chiếu phim");
        lblSubtitle.setFont(ModernUIStyles.FONT_HEADER);
        lblSubtitle.setForeground(ModernUIStyles.TEXT_SECONDARY);
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Current date/time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy - HH:mm");
        JLabel lblDateTime = new JLabel(now.format(formatter));
        lblDateTime.setFont(ModernUIStyles.FONT_NORMAL);
        lblDateTime.setForeground(ModernUIStyles.TEXT_MUTED);
        lblDateTime.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblGreeting);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblSubtitle);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblDateTime);

        return panel;
    }

    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3, 24, 24));
        panel.setOpaque(false);

        // Quick action cards
        panel.add(createQuickActionCard(
            "Bán Vé",
            "Đặt vé và quản lý booking",
            IconFactory.createTicketIcon(48, ModernUIStyles.PRIMARY_COLOR),
            ModernUIStyles.PRIMARY_COLOR,
            () -> mainFrame.openDatVeFrame()
        ));

        panel.add(createQuickActionCard(
            "Quản Lý Phim",
            "Thêm, sửa, xóa phim",
            IconFactory.createMovieIcon(48, ModernUIStyles.SECONDARY_COLOR),
            ModernUIStyles.SECONDARY_COLOR,
            () -> mainFrame.openPhimFrame()
        ));

        panel.add(createQuickActionCard(
            "Lịch Chiếu",
            "Quản lý lịch chiếu phim",
            IconFactory.createCalendarIcon(48, ModernUIStyles.ACCENT_COLOR),
            ModernUIStyles.ACCENT_COLOR,
            () -> mainFrame.openLichChieuFrame()
        ));

        panel.add(createQuickActionCard(
            "Thống Kê",
            "Dashboard và báo cáo",
            IconFactory.createChartIcon(48, ModernUIStyles.SUCCESS_COLOR),
            ModernUIStyles.SUCCESS_COLOR,
            () -> mainFrame.openDashboard()
        ));

        panel.add(createQuickActionCard(
            "Khách Hàng",
            "Quản lý khách hàng",
            IconFactory.createUserIcon(48, ModernUIStyles.INFO_COLOR),
            ModernUIStyles.INFO_COLOR,
            () -> mainFrame.openKhachHangFrame()
        ));

        panel.add(createQuickActionCard(
            "Hóa Đơn",
            "Lịch sử giao dịch",
            IconFactory.createReceiptIcon(48, ModernUIStyles.WARNING_COLOR),
            ModernUIStyles.WARNING_COLOR,
            () -> mainFrame.openHoaDonFrame()
        ));

        return panel;
    }

    private JPanel createQuickActionCard(String title, String description, Icon icon,
                                         Color accentColor, Runnable action) {
        // Mutable state for hover effects
        final boolean[] isHovered = {false};
        final float[] shadowSize = {4};

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                if (shadowSize[0] > 0) {
                    for (int i = 0; i < shadowSize[0]; i++) {
                        int alpha = (int) (20 - (i * 2));
                        g2d.setColor(new Color(0, 0, 0, alpha));
                        g2d.fillRoundRect(i, i + 4, getWidth() - i * 2, getHeight() - i * 2,
                                         ModernUIStyles.BORDER_RADIUS_LARGE, ModernUIStyles.BORDER_RADIUS_LARGE);
                    }
                }

                // Card background
                g2d.setColor(ModernUIStyles.BG_CARD);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(),
                                 ModernUIStyles.BORDER_RADIUS_LARGE, ModernUIStyles.BORDER_RADIUS_LARGE);

                // Accent bar (top)
                g2d.setColor(accentColor);
                g2d.fillRoundRect(0, 0, getWidth(), 6,
                                 ModernUIStyles.BORDER_RADIUS_LARGE, ModernUIStyles.BORDER_RADIUS_LARGE);

                // Border on hover
                if (isHovered[0]) {
                    g2d.setColor(accentColor);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
                                     ModernUIStyles.BORDER_RADIUS_LARGE, ModernUIStyles.BORDER_RADIUS_LARGE);
                }
            }
        };

        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(240, 160));

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 20, 20, 20));

        // Icon
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(ModernUIStyles.FONT_HEADER);
        lblTitle.setForeground(ModernUIStyles.TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Description
        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(ModernUIStyles.FONT_SMALL);
        lblDesc.setForeground(ModernUIStyles.TEXT_SECONDARY);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblIcon);
        contentPanel.add(Box.createVerticalStrut(16));
        contentPanel.add(lblTitle);
        contentPanel.add(Box.createVerticalStrut(6));
        contentPanel.add(lblDesc);

        card.add(contentPanel, BorderLayout.CENTER);

        // Hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered[0] = true;
                shadowSize[0] = 8;
                card.repaint();
                // Simple scale effect without animation (more stable)
                card.setBorder(BorderFactory.createEmptyBorder(-2, -2, -2, -2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered[0] = false;
                shadowSize[0] = 4;
                card.repaint();
                card.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Flash effect on click
                final Color originalBg = ModernUIStyles.BG_CARD;
                Timer flashTimer = new Timer(50, null);
                final int[] flashCount = {0};

                flashTimer.addActionListener(evt -> {
                    flashCount[0]++;
                    if (flashCount[0] % 2 == 0) {
                        card.setBackground(originalBg);
                    } else {
                        card.setBackground(accentColor.brighter());
                    }

                    if (flashCount[0] >= 4) {
                        flashTimer.stop();
                        card.setBackground(originalBg);
                        action.run();
                    }
                });
                flashTimer.start();
            }
        });

        return card;
    }
}
