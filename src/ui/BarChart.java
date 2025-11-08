package ui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Custom bar chart component using Graphics2D
 */
public class BarChart extends JPanel {
    private Map<String, Double> data;
    private String title;
    private String xLabel;
    private String yLabel;
    private Color barColor = UIStyles.PRIMARY_COLOR;

    public BarChart(String title, String xLabel, String yLabel) {
        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.data = new LinkedHashMap<>();

        setPreferredSize(new Dimension(600, 400));
        setBackground(UIStyles.BG_SECONDARY);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
        repaint();
    }

    public void setBarColor(Color color) {
        this.barColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (data == null || data.isEmpty()) {
            drawNoData(g2d);
            g2d.dispose();
            return;
        }

        int width = getWidth();
        int height = getHeight();

        // Margins
        int topMargin = 60;
        int bottomMargin = 80;
        int leftMargin = 80;
        int rightMargin = 40;

        int chartWidth = width - leftMargin - rightMargin;
        int chartHeight = height - topMargin - bottomMargin;

        // Draw title
        g2d.setFont(UIStyles.FONT_HEADER);
        g2d.setColor(UIStyles.TEXT_PRIMARY);
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (width - titleWidth) / 2, 30);

        // Find max value
        double maxValue = data.values().stream().mapToDouble(Double::doubleValue).max().orElse(100);
        maxValue = Math.ceil(maxValue / 10) * 10; // Round up to nearest 10

        // Draw Y axis
        g2d.setColor(UIStyles.TEXT_SECONDARY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(leftMargin, topMargin, leftMargin, topMargin + chartHeight);

        // Y axis label
        g2d.setFont(UIStyles.FONT_NORMAL);
        g2d.rotate(-Math.PI / 2);
        g2d.drawString(yLabel, -(topMargin + chartHeight / 2 + g2d.getFontMetrics().stringWidth(yLabel) / 2), 20);
        g2d.rotate(Math.PI / 2);

        // Y axis values
        g2d.setFont(UIStyles.FONT_SMALL);
        int steps = 5;
        for (int i = 0; i <= steps; i++) {
            int y = topMargin + chartHeight - (i * chartHeight / steps);
            double value = (maxValue / steps) * i;

            g2d.setColor(new Color(224, 224, 224));
            g2d.drawLine(leftMargin, y, leftMargin + chartWidth, y);

            g2d.setColor(UIStyles.TEXT_SECONDARY);
            String valueStr = String.format("%.0f", value);
            int valueWidth = g2d.getFontMetrics().stringWidth(valueStr);
            g2d.drawString(valueStr, leftMargin - valueWidth - 10, y + 5);
        }

        // Draw X axis
        g2d.setColor(UIStyles.TEXT_SECONDARY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(leftMargin, topMargin + chartHeight, leftMargin + chartWidth, topMargin + chartHeight);

        // X axis label
        g2d.setFont(UIStyles.FONT_NORMAL);
        int xLabelWidth = g2d.getFontMetrics().stringWidth(xLabel);
        g2d.drawString(xLabel, leftMargin + (chartWidth - xLabelWidth) / 2, height - 20);

        // Draw bars
        int barCount = data.size();
        int barSpacing = chartWidth / (barCount * 2 + 1);
        int barWidth = barSpacing;
        int x = leftMargin + barSpacing;

        g2d.setFont(UIStyles.FONT_SMALL);
        List<Map.Entry<String, Double>> entries = new ArrayList<>(data.entrySet());

        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Double> entry = entries.get(i);
            String label = entry.getKey();
            double value = entry.getValue();

            int barHeight = (int) ((value / maxValue) * chartHeight);
            int barY = topMargin + chartHeight - barHeight;

            // Draw bar with gradient
            GradientPaint gradient = new GradientPaint(
                x, barY, barColor,
                x, barY + barHeight, barColor.darker()
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(x, barY, barWidth, barHeight, 5, 5);

            // Draw bar border
            g2d.setColor(barColor.darker());
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRoundRect(x, barY, barWidth, barHeight, 5, 5);

            // Draw value on top of bar
            g2d.setColor(UIStyles.TEXT_PRIMARY);
            String valueStr = String.format("%.0f", value);
            int valueWidth = g2d.getFontMetrics().stringWidth(valueStr);
            g2d.drawString(valueStr, x + (barWidth - valueWidth) / 2, barY - 5);

            // Draw label
            g2d.setColor(UIStyles.TEXT_SECONDARY);
            int labelWidth = g2d.getFontMetrics().stringWidth(label);
            if (labelWidth > barWidth) {
                // Rotate label if too long
                g2d.rotate(-Math.PI / 4, x + barWidth / 2, topMargin + chartHeight + 10);
                g2d.drawString(label, x + barWidth / 2, topMargin + chartHeight + 10);
                g2d.rotate(Math.PI / 4, x + barWidth / 2, topMargin + chartHeight + 10);
            } else {
                g2d.drawString(label, x + (barWidth - labelWidth) / 2, topMargin + chartHeight + 20);
            }

            x += barWidth + barSpacing;
        }

        g2d.dispose();
    }

    private void drawNoData(Graphics2D g2d) {
        g2d.setFont(UIStyles.FONT_HEADER);
        g2d.setColor(UIStyles.TEXT_SECONDARY);
        String msg = "Không có dữ liệu";
        FontMetrics fm = g2d.getFontMetrics();
        int msgWidth = fm.stringWidth(msg);
        g2d.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2);
    }
}
