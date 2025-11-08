package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Enhanced UI styling with modern design elements
 * Includes gradients, shadows, glassmorphism, and smooth animations
 */
public class ModernUIStyles {

    // Modern Color Palette - Deep Blues & Purples
    public static final Color PRIMARY_COLOR = new Color(79, 70, 229);        // Indigo
    public static final Color PRIMARY_DARK = new Color(67, 56, 202);
    public static final Color PRIMARY_LIGHT = new Color(129, 140, 248);
    public static final Color SECONDARY_COLOR = new Color(236, 72, 153);     // Pink
    public static final Color ACCENT_COLOR = new Color(251, 146, 60);        // Orange
    public static final Color SUCCESS_COLOR = new Color(34, 197, 94);        // Green
    public static final Color ERROR_COLOR = new Color(239, 68, 68);          // Red
    public static final Color WARNING_COLOR = new Color(251, 191, 36);       // Amber
    public static final Color INFO_COLOR = new Color(59, 130, 246);          // Blue

    // Background Colors - Modern Neutrals
    public static final Color BG_DARK = new Color(15, 23, 42);               // Slate 900
    public static final Color BG_MEDIUM = new Color(30, 41, 59);             // Slate 800
    public static final Color BG_LIGHT = new Color(248, 250, 252);           // Slate 50
    public static final Color BG_CARD = new Color(255, 255, 255);
    public static final Color BG_HOVER = new Color(241, 245, 249);           // Slate 100

    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    public static final Color TEXT_MUTED = new Color(148, 163, 184);
    public static final Color TEXT_WHITE = Color.WHITE;

    // Seat Colors
    public static final Color SEAT_AVAILABLE = new Color(34, 197, 94);
    public static final Color SEAT_SELECTED = new Color(79, 70, 229);
    public static final Color SEAT_OCCUPIED = new Color(148, 163, 184);
    public static final Color SEAT_VIP = new Color(251, 146, 60);

    // Fonts - SF Pro inspired
    public static final Font FONT_DISPLAY = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.SEMIBOLD, 16);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    // Dimensions
    public static final int BORDER_RADIUS = 12;
    public static final int BORDER_RADIUS_LARGE = 16;
    public static final int SPACING_SM = 8;
    public static final int SPACING_MD = 16;
    public static final int SPACING_LG = 24;
    public static final int SPACING_XL = 32;

    /**
     * Create gradient panel with modern colors
     */
    public static JPanel createGradientPanel(Color color1, Color color2) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                GradientPaint gradient = new GradientPaint(
                    0, 0, color1,
                    getWidth(), getHeight(), color2
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    /**
     * Create modern card with shadow and rounded corners
     */
    public static JPanel createModernCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow effect (multiple layers for soft shadow)
                for (int i = 0; i < 3; i++) {
                    g2d.setColor(new Color(0, 0, 0, 10 - i * 2));
                    g2d.fillRoundRect(i, i + 2, getWidth() - i * 2, getHeight() - i * 2,
                                     BORDER_RADIUS, BORDER_RADIUS);
                }

                // Card background
                g2d.setColor(BG_CARD);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(SPACING_MD, SPACING_MD, SPACING_MD, SPACING_MD));
        return card;
    }

    /**
     * Create glassmorphism card (frosted glass effect)
     */
    public static JPanel createGlassCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Frosted glass effect
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS_LARGE, BORDER_RADIUS_LARGE);

                // Border
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
                                 BORDER_RADIUS_LARGE, BORDER_RADIUS_LARGE);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(SPACING_LG, SPACING_LG, SPACING_LG, SPACING_LG));
        return card;
    }

    /**
     * Create modern button with gradient
     */
    public static JButton createModernButton(String text, Color color1, Color color2) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, color1,
                    getWidth(), getHeight(), color2
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);

                // Text
                g2d.setColor(TEXT_WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), textX, textY);

                // Icon
                if (getIcon() != null) {
                    int iconX = textX - getIcon().getIconWidth() - 8;
                    int iconY = (getHeight() - getIcon().getIconHeight()) / 2;
                    getIcon().paintIcon(this, g2d, iconX, iconY);
                }
            }
        };

        button.setFont(FONT_NORMAL);
        button.setForeground(TEXT_WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 42));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setFont(FONT_NORMAL.deriveFont(Font.BOLD));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setFont(FONT_NORMAL);
            }
        });

        return button;
    }

    /**
     * Create icon button (circular)
     */
    public static JButton createIconButton(Icon icon, Color bgColor) {
        JButton button = new JButton(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Circular background
                if (getModel().isPressed()) {
                    g2d.setColor(darken(bgColor, 0.8f));
                } else if (getModel().isRollover()) {
                    g2d.setColor(darken(bgColor, 0.9f));
                } else {
                    g2d.setColor(bgColor);
                }
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Icon
                if (getIcon() != null) {
                    int iconX = (getWidth() - getIcon().getIconWidth()) / 2;
                    int iconY = (getHeight() - getIcon().getIconHeight()) / 2;
                    getIcon().paintIcon(this, g2d, iconX, iconY);
                }
            }
        };

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(48, 48));

        return button;
    }

    /**
     * Create modern text field with floating label
     */
    public static JPanel createModernTextField(String label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(FONT_SMALL);
        lblLabel.setForeground(TEXT_SECONDARY);
        lblLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        JTextField textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(BG_LIGHT);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);

                // Border
                if (isFocusOwner()) {
                    g2d.setColor(PRIMARY_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(new Color(226, 232, 240));
                    g2d.setStroke(new BasicStroke(1));
                }
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);

                super.paintComponent(g);
            }
        };

        textField.setFont(FONT_NORMAL);
        textField.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        textField.setOpaque(false);

        panel.add(lblLabel, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Create badge label
     */
    public static JLabel createBadge(String text, Color bgColor) {
        JLabel badge = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                super.paintComponent(g);
            }
        };

        badge.setFont(FONT_SMALL.deriveFont(Font.BOLD));
        badge.setForeground(TEXT_WHITE);
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        badge.setOpaque(false);

        return badge;
    }

    /**
     * Create progress bar with modern styling
     */
    public static JProgressBar createModernProgressBar() {
        JProgressBar progressBar = new JProgressBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(new Color(226, 232, 240));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

                // Progress
                int progressWidth = (int) ((getWidth() * getValue()) / getMaximum());
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    progressWidth, 0, PRIMARY_LIGHT
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, progressWidth, getHeight(), getHeight(), getHeight());
            }
        };

        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(200, 8));
        progressBar.setOpaque(false);

        return progressBar;
    }

    /**
     * Darken color
     */
    public static Color darken(Color color, float factor) {
        return new Color(
            (int) (color.getRed() * factor),
            (int) (color.getGreen() * factor),
            (int) (color.getBlue() * factor),
            color.getAlpha()
        );
    }

    /**
     * Lighten color
     */
    public static Color lighten(Color color, float factor) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        r = (int) (r + (255 - r) * factor);
        g = (int) (g + (255 - g) * factor);
        b = (int) (b + (255 - b) * factor);

        return new Color(r, g, b, color.getAlpha());
    }

    /**
     * Add shadow to component
     */
    public static void addShadow(JComponent component) {
        component.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(4, 4, 8, 8),
            component.getBorder()
        ));
    }
}
