package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Professional splash screen with loading animation
 */
public class SplashScreen extends JWindow {
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private JLabel lblLogo;
    private JLabel lblVersion;

    public SplashScreen() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        // Rounded corners
        setShape(new RoundRectangle2D.Double(0, 0, 600, 400, 20, 20));

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, ModernUIStyles.PRIMARY_DARK,
                    getWidth(), getHeight(), ModernUIStyles.PRIMARY_COLOR
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Decorative circles
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillOval(-50, -50, 200, 200);
                g2d.fillOval(getWidth() - 150, getHeight() - 150, 200, 200);

                // Animated gradient overlay
                g2d.setColor(new Color(255, 255, 255, 10));
                for (int i = 0; i < 5; i++) {
                    g2d.fillOval(100 + i * 80, 50 + i * 30, 150, 150);
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Logo area
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setOpaque(false);

        // Cinema icon (custom drawn)
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                // Film reel icon
                g2d.setColor(Color.WHITE);

                // Outer circle
                g2d.setStroke(new BasicStroke(4));
                g2d.drawOval(centerX - 40, centerY - 40, 80, 80);

                // Film strip
                g2d.fillRect(centerX - 30, centerY - 35, 60, 8);
                g2d.fillRect(centerX - 30, centerY - 4, 60, 8);
                g2d.fillRect(centerX - 30, centerY + 27, 60, 8);

                // Perforations
                for (int i = 0; i < 4; i++) {
                    g2d.fillRect(centerX - 28 + i * 20, centerY - 33, 4, 4);
                    g2d.fillRect(centerX - 28 + i * 20, centerY - 2, 4, 4);
                    g2d.fillRect(centerX - 28 + i * 20, centerY + 29, 4, 4);
                }

                // Center play button
                int[] xPoints = {centerX - 8, centerX + 12, centerX - 8};
                int[] yPoints = {centerY - 10, centerY, centerY + 10};
                g2d.fillPolygon(xPoints, yPoints, 3);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(100, 100));
        iconPanel.setMaximumSize(new Dimension(100, 100));
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblLogo = new JLabel("Cinema Manager");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Hệ Thống Quản Lý Rạp Chiếu Phim");
        lblSubtitle.setFont(ModernUIStyles.FONT_HEADER);
        lblSubtitle.setForeground(new Color(255, 255, 255, 200));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblVersion = new JLabel("Version 2.0");
        lblVersion.setFont(ModernUIStyles.FONT_SMALL);
        lblVersion.setForeground(new Color(255, 255, 255, 150));
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(Box.createVerticalStrut(20));
        logoPanel.add(iconPanel);
        logoPanel.add(Box.createVerticalStrut(20));
        logoPanel.add(lblLogo);
        logoPanel.add(Box.createVerticalStrut(10));
        logoPanel.add(lblSubtitle);
        logoPanel.add(Box.createVerticalStrut(10));
        logoPanel.add(lblVersion);

        // Status area
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setOpaque(false);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        lblStatus = new JLabel("Đang khởi động...");
        lblStatus.setFont(ModernUIStyles.FONT_NORMAL);
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressBar = new JProgressBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Progress
                int progressWidth = (int) ((getWidth() * getValue()) / (double) getMaximum());
                if (progressWidth > 0) {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(255, 255, 255, 230),
                        progressWidth, 0, new Color(255, 255, 255, 180)
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(0, 0, progressWidth, getHeight(), 20, 20);
                }
            }
        };

        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(false);
        progressBar.setOpaque(false);
        progressBar.setPreferredSize(new Dimension(400, 10));
        progressBar.setMaximumSize(new Dimension(400, 10));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusPanel.add(lblStatus);
        statusPanel.add(Box.createVerticalStrut(15));
        statusPanel.add(progressBar);

        // Footer
        JLabel lblCopyright = new JLabel("© 2025 Cinema Manager. All rights reserved.");
        lblCopyright.setFont(ModernUIStyles.FONT_SMALL);
        lblCopyright.setForeground(new Color(255, 255, 255, 120));
        lblCopyright.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        footerPanel.add(lblCopyright);

        mainPanel.add(logoPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        containerPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(containerPanel);
    }

    public void updateProgress(int value, String status) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(value);
            lblStatus.setText(status);
        });
    }

    public void showSplash() {
        setVisible(true);

        // Simulate loading process
        new Thread(() -> {
            try {
                updateProgress(10, "Đang tải cấu hình...");
                Thread.sleep(400);

                updateProgress(30, "Đang kết nối database...");
                Thread.sleep(500);

                updateProgress(50, "Đang tải thư viện...");
                Thread.sleep(400);

                updateProgress(70, "Đang khởi tạo giao diện...");
                Thread.sleep(500);

                updateProgress(90, "Hoàn tất...");
                Thread.sleep(300);

                updateProgress(100, "Sẵn sàng!");
                Thread.sleep(400);

                // Fade out effect
                SwingUtilities.invokeLater(() -> {
                    Timer fadeTimer = new Timer(20, null);
                    final float[] opacity = {1.0f};
                    fadeTimer.addActionListener(e -> {
                        opacity[0] -= 0.05f;
                        if (opacity[0] <= 0) {
                            fadeTimer.stop();
                            dispose();

                            // Launch MainFrame
                            SwingUtilities.invokeLater(() -> {
                                MainFrame mainFrame = new MainFrame();
                                mainFrame.setVisible(true);
                            });
                        }
                        setOpacity(Math.max(0, opacity[0]));
                    });
                    fadeTimer.start();
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        // Initialize FlatLaf if available
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.showSplash();
        });
    }
}
