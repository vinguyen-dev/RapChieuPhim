package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Modern cell renderer for tables with improved styling
 */
public class ModernTableCellRenderer extends DefaultTableCellRenderer {

    private Color alternateRowColor = new Color(248, 248, 248);
    private Color selectedColor = UIStyles.PRIMARY_LIGHT;
    private Color hoverColor = new Color(240, 245, 255);

    public ModernTableCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (c instanceof JLabel) {
            JLabel label = (JLabel) c;

            // Font
            label.setFont(UIStyles.FONT_NORMAL);

            // Padding
            label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

            // Colors
            if (isSelected) {
                label.setBackground(selectedColor);
                label.setForeground(UIStyles.TEXT_PRIMARY);
            } else {
                if (row % 2 == 0) {
                    label.setBackground(Color.WHITE);
                } else {
                    label.setBackground(alternateRowColor);
                }
                label.setForeground(UIStyles.TEXT_PRIMARY);
            }

            // Alignment based on data type
            if (value instanceof Number) {
                label.setHorizontalAlignment(JLabel.RIGHT);
            } else {
                label.setHorizontalAlignment(JLabel.LEFT);
            }
        }

        return c;
    }

    public void setAlternateRowColor(Color color) {
        this.alternateRowColor = color;
    }

    public void setSelectedColor(Color color) {
        this.selectedColor = color;
    }
}

/**
 * Status cell renderer with colored badges
 */
class StatusCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            String status = value.toString();
            label.setText(status);
            label.setOpaque(true);
            label.setHorizontalAlignment(CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 11));

            // Color based on status
            Color bgColor;
            Color fgColor = Color.WHITE;

            if (status.contains("thanh toan") || status.contains("đã đặt") || status.equalsIgnoreCase("Da dat")) {
                bgColor = UIStyles.SUCCESS_COLOR;
            } else if (status.contains("Chưa") || status.contains("Hủy") || status.equalsIgnoreCase("Huy")) {
                bgColor = UIStyles.ERROR_COLOR;
            } else {
                bgColor = UIStyles.WARNING_COLOR;
            }

            label.setBackground(bgColor);
            label.setForeground(fgColor);

            // Rounded border
            label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
            ));
        }

        return label;
    }
}

/**
 * Currency cell renderer with formatted display
 */
class CurrencyCellRenderer extends DefaultTableCellRenderer {

    public CurrencyCellRenderer() {
        setHorizontalAlignment(RIGHT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        if (value instanceof Number) {
            double amount = ((Number) value).doubleValue();
            value = String.format("%,.0f VNĐ", amount);
        }

        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(UIStyles.SUCCESS_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        if (isSelected) {
            label.setBackground(UIStyles.PRIMARY_LIGHT);
        } else {
            label.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
        }

        return label;
    }
}

/**
 * Date cell renderer with formatted display
 */
class DateCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        label.setFont(UIStyles.FONT_NORMAL);
        label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        if (isSelected) {
            label.setBackground(UIStyles.PRIMARY_LIGHT);
        } else {
            label.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
        }

        return label;
    }
}

/**
 * Icon cell renderer
 */
class IconCellRenderer extends DefaultTableCellRenderer {

    private Icon icon;
    private Color iconColor;

    public IconCellRenderer(Icon icon, Color iconColor) {
        this.icon = icon;
        this.iconColor = iconColor;
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);

        label.setIcon(icon);
        label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        if (isSelected) {
            label.setBackground(UIStyles.PRIMARY_LIGHT);
        } else {
            label.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
        }

        return label;
    }
}

/**
 * Progress bar cell renderer
 */
class ProgressCellRenderer extends JProgressBar implements javax.swing.table.TableCellRenderer {

    public ProgressCellRenderer() {
        setMinimum(0);
        setMaximum(100);
        setStringPainted(true);
        setFont(UIStyles.FONT_SMALL);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        if (value instanceof Number) {
            int progress = ((Number) value).intValue();
            setValue(progress);
            setString(progress + "%");

            // Color based on value
            if (progress < 30) {
                setForeground(UIStyles.ERROR_COLOR);
            } else if (progress < 70) {
                setForeground(UIStyles.WARNING_COLOR);
            } else {
                setForeground(UIStyles.SUCCESS_COLOR);
            }
        }

        if (isSelected) {
            setBackground(UIStyles.PRIMARY_LIGHT);
        } else {
            setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
        }

        return this;
    }
}
