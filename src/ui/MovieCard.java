package ui;

import entity.Phim;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Card component for displaying movie information with poster
 */
public class MovieCard extends JPanel {
    private Phim phim;
    private JLabel lblPoster;
    private JLabel lblTitle;
    private JLabel lblInfo;
    private boolean isSelected = false;
    private Color borderColor = new Color(224, 224, 224);

    public MovieCard(Phim phim) {
        this.phim = phim;

        setLayout(new BorderLayout(0, 8));
        setBackground(UIStyles.BG_SECONDARY);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setPreferredSize(new Dimension(200, 340));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        initComponents();
        addHoverEffect();
    }

    private void initComponents() {
        // Poster panel
        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.setBackground(UIStyles.BG_SECONDARY);
        posterPanel.setPreferredSize(new Dimension(180, 260));

        lblPoster = new JLabel();
        lblPoster.setHorizontalAlignment(SwingConstants.CENTER);
        lblPoster.setVerticalAlignment(SwingConstants.CENTER);
        lblPoster.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Load poster image
        loadPoster();

        posterPanel.add(lblPoster, BorderLayout.CENTER);

        // Title
        lblTitle = new JLabel("<html><center>" + phim.getTenPhim() + "</center></html>");
        lblTitle.setFont(UIStyles.FONT_SUBHEADER);
        lblTitle.setForeground(UIStyles.TEXT_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        // Info (Genre and Duration)
        String infoText = String.format("<html><center>%s<br>%d phút</center></html>",
            phim.getTheLoai() != null && !phim.getTheLoai().isEmpty() ? phim.getTheLoai() : "Chưa rõ",
            phim.getThoiLuong());
        lblInfo = new JLabel(infoText);
        lblInfo.setFont(UIStyles.FONT_SMALL);
        lblInfo.setForeground(UIStyles.TEXT_SECONDARY);
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        infoPanel.setOpaque(false);
        infoPanel.add(lblTitle);
        infoPanel.add(lblInfo);

        add(posterPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private void loadPoster() {
        try {
            String imagePath = phim.getHinhAnh();
            if (imagePath != null && !imagePath.isEmpty()) {
                File imgFile = new File("resources/images/movies/" + imagePath);
                if (imgFile.exists()) {
                    Image img = ImageIO.read(imgFile);
                    Image scaled = img.getScaledInstance(180, 260, Image.SCALE_SMOOTH);
                    lblPoster.setIcon(new ImageIcon(scaled));
                    return;
                }
            }
        } catch (Exception e) {
            // Silently fail - will show placeholder
        }

        // Show placeholder
        lblPoster.setIcon(createPlaceholderIcon());
    }

    private Icon createPlaceholderIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(new Color(240, 240, 240));
                g2d.fillRect(x, y, getIconWidth(), getIconHeight());

                // Film icon
                g2d.setColor(new Color(180, 180, 180));
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
                FontMetrics fm = g2d.getFontMetrics();
                String icon = "🎬";
                int textX = x + (getIconWidth() - fm.stringWidth(icon)) / 2;
                int textY = y + ((getIconHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(icon, textX, textY);

                // Text
                g2d.setColor(new Color(150, 150, 150));
                g2d.setFont(UIStyles.FONT_SMALL);
                fm = g2d.getFontMetrics();
                String text = "No Poster";
                textX = x + (getIconWidth() - fm.stringWidth(text)) / 2;
                textY = y + getIconHeight() - 20;
                g2d.drawString(text, textX, textY);

                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return 180;
            }

            @Override
            public int getIconHeight() {
                return 260;
            }
        };
    }

    private void addHoverEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isSelected) {
                    borderColor = UIStyles.PRIMARY_COLOR;
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor, 2),
                        BorderFactory.createEmptyBorder(9, 9, 9, 9)
                    ));
                    lblTitle.setForeground(UIStyles.PRIMARY_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isSelected) {
                    borderColor = new Color(224, 224, 224);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor, 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));
                    lblTitle.setForeground(UIStyles.TEXT_PRIMARY);
                }
            }
        });
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        if (selected) {
            borderColor = UIStyles.PRIMARY_DARK;
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 3),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));
            lblTitle.setForeground(UIStyles.PRIMARY_DARK);
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        } else {
            borderColor = new Color(224, 224, 224);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            lblTitle.setForeground(UIStyles.TEXT_PRIMARY);
            lblTitle.setFont(UIStyles.FONT_SUBHEADER);
        }
        repaint();
    }

    public Phim getPhim() {
        return phim;
    }

    public void updateData(Phim phim) {
        this.phim = phim;
        lblTitle.setText("<html><center>" + phim.getTenPhim() + "</center></html>");
        String infoText = String.format("<html><center>%s<br>%d phút</center></html>",
            phim.getTheLoai() != null && !phim.getTheLoai().isEmpty() ? phim.getTheLoai() : "Chưa rõ",
            phim.getThoiLuong());
        lblInfo.setText(infoText);
        loadPoster();
    }
}
