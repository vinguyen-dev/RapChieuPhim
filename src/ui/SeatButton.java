package ui;

import entity.Ghe;

import javax.swing.*;
import java.awt.*;

/**
 * Custom button component representing a cinema seat
 */
public class SeatButton extends JButton {
    private Ghe ghe;
    private SeatStatus status;

    public enum SeatStatus {
        AVAILABLE,
        SELECTED,
        OCCUPIED,
        VIP_AVAILABLE,
        VIP_SELECTED,
        COUPLE_AVAILABLE,
        COUPLE_SELECTED
    }

    public SeatButton(Ghe ghe, SeatStatus initialStatus) {
        this.ghe = ghe;
        this.status = initialStatus;

        // Couple seats are wider
        boolean isCouple = "Couple".equalsIgnoreCase(ghe.getLoaiGhe());
        if (isCouple) {
            setPreferredSize(new Dimension(105, 48));  // Double width + gap
            setMinimumSize(new Dimension(105, 48));
            setMaximumSize(new Dimension(105, 48));
            setFont(new Font("Segoe UI", Font.BOLD, 10));
        } else {
            setPreferredSize(new Dimension(50, 48));  // Wider to fit E10
            setMinimumSize(new Dimension(50, 48));
            setMaximumSize(new Dimension(50, 48));
            setFont(new Font("Segoe UI", Font.BOLD, 10));
        }

        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        updateAppearance();
        updateTooltip();
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
        updateAppearance();
    }

    public SeatStatus getStatus() {
        return status;
    }

    public Ghe getGhe() {
        return ghe;
    }

    private void updateAppearance() {
        // Display seat number with icon for couple seats
        if ("Couple".equalsIgnoreCase(ghe.getLoaiGhe())) {
            setText("💑 " + ghe.getSoGhe());
        } else {
            setText(ghe.getSoGhe());
        }

        switch (status) {
            case AVAILABLE:
                setBackground(UIStyles.SEAT_AVAILABLE);
                setForeground(Color.WHITE);
                setEnabled(true);
                break;
            case SELECTED:
                setBackground(UIStyles.SEAT_SELECTED);
                setForeground(Color.WHITE);
                setEnabled(true);
                break;
            case OCCUPIED:
                setBackground(UIStyles.SEAT_OCCUPIED);
                setForeground(Color.WHITE);
                setEnabled(false);
                break;
            case VIP_AVAILABLE:
                setBackground(UIStyles.SEAT_VIP);
                setForeground(Color.WHITE);
                setEnabled(true);
                break;
            case VIP_SELECTED:
                setBackground(UIStyles.PRIMARY_DARK);
                setForeground(Color.WHITE);
                setEnabled(true);
                break;
            case COUPLE_AVAILABLE:
                setBackground(new Color(233, 30, 99)); // Pink for couple
                setForeground(Color.WHITE);
                setEnabled(true);
                break;
            case COUPLE_SELECTED:
                setBackground(new Color(194, 24, 91)); // Darker pink
                setForeground(Color.WHITE);
                setEnabled(true);
                break;
        }
    }

    private void updateTooltip() {
        String statusText;
        switch (status) {
            case AVAILABLE:
            case VIP_AVAILABLE:
            case COUPLE_AVAILABLE:
                statusText = "Trống";
                break;
            case SELECTED:
            case VIP_SELECTED:
            case COUPLE_SELECTED:
                statusText = "Đã chọn";
                break;
            case OCCUPIED:
                statusText = "Đã đặt";
                break;
            default:
                statusText = "";
        }

        setToolTipText(String.format("Ghế %s - Hàng %s - %s - %s",
            ghe.getSoGhe(), ghe.getHang(), ghe.getLoaiGhe(), statusText));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

        // Draw border for selected seats
        if (status == SeatStatus.SELECTED || status == SeatStatus.VIP_SELECTED) {
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
        }

        g2d.dispose();

        // Draw text
        super.paintComponent(g);
    }
}
