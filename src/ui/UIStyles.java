package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * UI styling constants and utilities for consistent modern design
 */
public class UIStyles {

    // Modern Color Palette
    public static final Color PRIMARY_COLOR = new Color(33, 150, 243);      // Blue
    public static final Color PRIMARY_DARK = new Color(25, 118, 210);
    public static final Color PRIMARY_LIGHT = new Color(100, 181, 246);
    public static final Color ACCENT_COLOR = new Color(255, 152, 0);        // Orange
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);       // Green
    public static final Color ERROR_COLOR = new Color(244, 67, 54);         // Red
    public static final Color WARNING_COLOR = new Color(255, 193, 7);       // Amber

    // Background Colors
    public static final Color BG_PRIMARY = new Color(250, 250, 250);
    public static final Color BG_SECONDARY = new Color(255, 255, 255);
    public static final Color BG_DARK = new Color(245, 245, 245);

    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);
    public static final Color TEXT_WHITE = Color.WHITE;

    // Seat Colors for Cinema
    public static final Color SEAT_AVAILABLE = new Color(76, 175, 80);      // Green
    public static final Color SEAT_SELECTED = new Color(33, 150, 243);      // Blue
    public static final Color SEAT_OCCUPIED = new Color(189, 189, 189);     // Gray
    public static final Color SEAT_VIP = new Color(255, 152, 0);            // Orange

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    // Borders
    public static final Border BORDER_PADDING = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    public static final Border BORDER_CARD = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    );

    // Dimensions
    public static final Dimension BUTTON_SIZE = new Dimension(120, 36);
    public static final Dimension LARGE_BUTTON_SIZE = new Dimension(150, 42);
    public static final int BORDER_RADIUS = 8;
    public static final int SPACING = 10;

    /**
     * Apply modern styling to a button
     */
    public static void styleButton(JButton button, Color bgColor) {
        button.setFont(FONT_NORMAL);
        button.setBackground(bgColor);
        button.setForeground(TEXT_WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(BUTTON_SIZE);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(brighten(originalColor, 0.9f));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    /**
     * Apply primary button styling
     */
    public static void stylePrimaryButton(JButton button) {
        styleButton(button, PRIMARY_COLOR);
    }

    /**
     * Apply success button styling
     */
    public static void styleSuccessButton(JButton button) {
        styleButton(button, SUCCESS_COLOR);
    }

    /**
     * Apply error/danger button styling
     */
    public static void styleErrorButton(JButton button) {
        styleButton(button, ERROR_COLOR);
    }

    /**
     * Apply accent button styling
     */
    public static void styleAccentButton(JButton button) {
        styleButton(button, ACCENT_COLOR);
    }

    /**
     * Apply secondary button styling (outline style)
     */
    public static void styleSecondaryButton(JButton button) {
        button.setFont(FONT_NORMAL);
        button.setBackground(BG_SECONDARY);
        button.setForeground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(BUTTON_SIZE);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY_LIGHT);
                button.setForeground(TEXT_WHITE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(BG_SECONDARY);
                button.setForeground(PRIMARY_COLOR);
            }
        });
    }

    /**
     * Style a panel with card-like appearance
     */
    public static void styleCard(JPanel panel) {
        panel.setBackground(BG_SECONDARY);
        panel.setBorder(BORDER_CARD);
    }

    /**
     * Style a table with modern appearance
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_NORMAL);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(TEXT_PRIMARY);

        // Style table header
        if (table.getTableHeader() != null) {
            table.getTableHeader().setFont(FONT_SUBHEADER);
            table.getTableHeader().setBackground(BG_DARK);
            table.getTableHeader().setForeground(TEXT_PRIMARY);
            table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
            table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        }
    }

    /**
     * Style a text field
     */
    public static void styleTextField(JTextField textField) {
        textField.setFont(FONT_NORMAL);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        textField.setPreferredSize(new Dimension(200, 36));
    }

    /**
     * Style a combo box
     */
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(FONT_NORMAL);
        comboBox.setBackground(BG_SECONDARY);
        comboBox.setPreferredSize(new Dimension(200, 36));
    }

    /**
     * Style a label as a title
     */
    public static void styleTitleLabel(JLabel label) {
        label.setFont(FONT_TITLE);
        label.setForeground(TEXT_PRIMARY);
    }

    /**
     * Style a label as a header
     */
    public static void styleHeaderLabel(JLabel label) {
        label.setFont(FONT_HEADER);
        label.setForeground(TEXT_PRIMARY);
    }

    /**
     * Create a titled panel with modern styling
     */
    public static JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, SPACING));
        panel.setBackground(BG_SECONDARY);
        panel.setBorder(BORDER_CARD);

        JLabel titleLabel = new JLabel(title);
        styleHeaderLabel(titleLabel);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panel.add(titleLabel, BorderLayout.NORTH);

        return panel;
    }

    /**
     * Brighten or darken a color
     */
    private static Color brighten(Color color, float factor) {
        int r = (int) Math.min(color.getRed() * factor, 255);
        int g = (int) Math.min(color.getGreen() * factor, 255);
        int b = (int) Math.min(color.getBlue() * factor, 255);
        return new Color(r, g, b);
    }

    /**
     * Show a modern success message dialog
     */
    public static void showSuccessMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thành công",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show a modern error message dialog
     */
    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Lỗi",
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show a modern confirmation dialog
     */
    public static boolean showConfirmDialog(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Xác nhận",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
}
