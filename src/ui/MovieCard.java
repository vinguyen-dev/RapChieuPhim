package ui;

import entity.Phim;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

/**
 * Netflix-style movie card component with poster and hover overlay
 */
public class MovieCard extends JPanel {
    private Phim phim;
    private JLabel lblPoster;
    private JPanel overlayPanel;
    private boolean isSelected = false;
    private boolean isHovered = false;
    private Image posterImage;

    private static final int CARD_WIDTH = 220;
    private static final int CARD_HEIGHT = 330;
    private static final int CORNER_RADIUS = 8;

    public MovieCard(Phim phim) {
        this.phim = phim;

        setLayout(null); // Absolute positioning for overlay
        setBackground(new Color(20, 20, 20));
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);

        initComponents();
        addHoverEffect();
    }

    private void initComponents() {
        // Load poster image first
        loadPoster();

        // Poster label (main background)
        lblPoster = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                // Draw rounded rectangle background
                g2d.setColor(new Color(40, 40, 40));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);

                // Draw poster image if available
                if (posterImage != null) {
                    g2d.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
                    g2d.drawImage(posterImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Draw placeholder
                    drawPlaceholder(g2d);
                }

                // Draw selection border
                if (isSelected) {
                    g2d.setColor(new Color(229, 9, 20)); // Netflix red
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, CORNER_RADIUS, CORNER_RADIUS);
                }

                g2d.dispose();
            }
        };
        lblPoster.setBounds(0, 0, CARD_WIDTH, CARD_HEIGHT);
        add(lblPoster);

        // Overlay panel (shown on hover)
        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isVisible()) return;

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw dark gradient overlay
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(0, 0, 0, 0),
                    0, getHeight(), new Color(0, 0, 0, 220)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);

                g2d.dispose();
            }
        };
        overlayPanel.setLayout(new BorderLayout());
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, CARD_WIDTH, CARD_HEIGHT);
        overlayPanel.setVisible(false);

        // Info panel inside overlay
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 20, 15));

        // Title
        JLabel lblTitle = new JLabel("<html>" + phim.getTenPhim() + "</html>");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Genre
        String genre = phim.getTheLoai() != null && !phim.getTheLoai().isEmpty()
            ? phim.getTheLoai() : "Chưa rõ";
        JLabel lblGenre = new JLabel(genre);
        lblGenre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblGenre.setForeground(new Color(220, 220, 220));
        lblGenre.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Duration
        JLabel lblDuration = new JLabel(phim.getThoiLuong() + " phút");
        lblDuration.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDuration.setForeground(new Color(180, 180, 180));
        lblDuration.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(lblTitle);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(lblGenre);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblDuration);

        overlayPanel.add(infoPanel, BorderLayout.SOUTH);
        add(overlayPanel);
    }

    private void loadPoster() {
        try {
            String imagePath = phim.getHinhAnh();
            if (imagePath != null && !imagePath.isEmpty()) {
                File imgFile;

                // Check if it's an absolute path first
                File absoluteFile = new File(imagePath);
                if (absoluteFile.isAbsolute() && absoluteFile.exists()) {
                    imgFile = absoluteFile;
                } else {
                    // Try relative path from resources/images/movies/
                    imgFile = new File("resources/images/movies/" + imagePath);
                }

                if (imgFile.exists()) {
                    posterImage = ImageIO.read(imgFile);
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading poster for " + phim.getTenPhim() + ": " + e.getMessage());
        }

        // posterImage remains null - will show placeholder
        posterImage = null;
    }

    private void drawPlaceholder(Graphics2D g2d) {
        // Dark background
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillRoundRect(0, 0, CARD_WIDTH, CARD_HEIGHT, CORNER_RADIUS, CORNER_RADIUS);

        // Film icon
        g2d.setColor(new Color(100, 100, 100));
        g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        FontMetrics fm = g2d.getFontMetrics();
        String icon = "🎬";
        int textX = (CARD_WIDTH - fm.stringWidth(icon)) / 2;
        int textY = (CARD_HEIGHT - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(icon, textX, textY);

        // Text
        g2d.setColor(new Color(120, 120, 120));
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fm = g2d.getFontMetrics();
        String text = "No Poster";
        textX = (CARD_WIDTH - fm.stringWidth(text)) / 2;
        textY = CARD_HEIGHT - 30;
        g2d.drawString(text, textX, textY);
    }

    private void addHoverEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                overlayPanel.setVisible(true);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                overlayPanel.setVisible(false);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Paint shadow for depth effect
        if (isHovered || isSelected) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw shadow
            g2d.setColor(new Color(0, 0, 0, 80));
            g2d.fillRoundRect(4, 4, CARD_WIDTH, CARD_HEIGHT, CORNER_RADIUS, CORNER_RADIUS);

            g2d.dispose();
        }
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        lblPoster.repaint();
        repaint();
    }

    public Phim getPhim() {
        return phim;
    }

    public void updateData(Phim phim) {
        this.phim = phim;
        loadPoster();
        // Recreate components with new data
        removeAll();
        initComponents();
        revalidate();
        repaint();
    }
}
