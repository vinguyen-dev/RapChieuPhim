package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * Factory for creating custom vector icons
 */
public class IconFactory {

    /**
     * Create a movie/film icon
     */
    public static Icon createMovieIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Film strip
                g2d.setColor(color);
                g2d.fillRoundRect(x + 2, y + 4, size - 4, size - 8, 4, 4);

                // Perforations
                g2d.setColor(Color.WHITE);
                int holeSize = Math.max(2, size / 8);
                for (int i = 0; i < 3; i++) {
                    int yPos = y + 6 + i * (size / 4);
                    g2d.fillRect(x + 3, yPos, holeSize, holeSize);
                    g2d.fillRect(x + size - holeSize - 3, yPos, holeSize, holeSize);
                }

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Create a theater/room icon
     */
    public static Icon createTheaterIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Screen
                g2d.setColor(color);
                g2d.fillRoundRect(x + 4, y + 2, size - 8, size / 4, 3, 3);

                // Seats (3 rows)
                int seatSize = size / 6;
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 4; col++) {
                        int sx = x + 4 + col * (seatSize + 2);
                        int sy = y + size / 3 + row * (seatSize + 2);
                        g2d.fillRoundRect(sx, sy, seatSize, seatSize, 2, 2);
                    }
                }

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Create a ticket icon
     */
    public static Icon createTicketIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ticket body
                g2d.setColor(color);
                Path2D ticket = new Path2D.Double();
                ticket.moveTo(x + 2, y + size / 4);
                ticket.lineTo(x + size - 2, y + size / 4);
                ticket.lineTo(x + size - 2, y + size - size / 4);
                ticket.lineTo(x + 2, y + size - size / 4);
                ticket.closePath();
                g2d.fill(ticket);

                // Perforation line
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{2, 2}, 0));
                g2d.drawLine(x + size / 2, y + size / 4, x + size / 2, y + size - size / 4);

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Create a calendar icon
     */
    public static Icon createCalendarIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Calendar body
                g2d.setColor(color);
                g2d.fillRoundRect(x + 3, y + 4, size - 6, size - 6, 3, 3);

                // Header
                g2d.setColor(color.darker());
                g2d.fillRoundRect(x + 3, y + 4, size - 6, size / 4, 3, 3);

                // Grid
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(1));
                int gridSize = (size - 8) / 3;
                for (int i = 1; i < 3; i++) {
                    g2d.drawLine(x + 3 + i * gridSize, y + size / 4 + 4, x + 3 + i * gridSize, y + size - 2);
                    g2d.drawLine(x + 3, y + size / 4 + 4 + i * gridSize / 2, x + size - 3, y + size / 4 + 4 + i * gridSize / 2);
                }

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Create a people/customer icon
     */
    public static Icon createPeopleIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(color);
                int headSize = size / 3;
                int bodyWidth = size / 2;
                int bodyHeight = size / 2;

                // Head
                g2d.fillOval(x + (size - headSize) / 2, y + 2, headSize, headSize);

                // Body
                Path2D body = new Path2D.Double();
                body.moveTo(x + (size - bodyWidth) / 2, y + headSize + 2);
                body.lineTo(x + 2, y + size - 2);
                body.lineTo(x + size - 2, y + size - 2);
                body.lineTo(x + size - (size - bodyWidth) / 2, y + headSize + 2);
                body.closePath();
                g2d.fill(body);

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Create an invoice/document icon
     */
    public static Icon createInvoiceIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Document
                g2d.setColor(color);
                g2d.fillRoundRect(x + 4, y + 2, size - 8, size - 4, 3, 3);

                // Lines
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(1.5f));
                for (int i = 0; i < 4; i++) {
                    int yPos = y + 6 + i * ((size - 10) / 4);
                    g2d.drawLine(x + 7, yPos, x + size - 7, yPos);
                }

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Create a chart/statistics icon
     */
    public static Icon createChartIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(color);

                // Bars
                int barWidth = size / 5;
                int[] heights = {size / 3, size / 2, size - 4, size / 2 + 2};
                for (int i = 0; i < 4; i++) {
                    int bx = x + 2 + i * (barWidth + 2);
                    int by = y + size - 2 - heights[i];
                    g2d.fillRect(bx, by, barWidth, heights[i]);
                }

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Create a search icon
     */
    public static Icon createSearchIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2));

                // Magnifying glass
                int circleSize = (int) (size * 0.6);
                g2d.drawOval(x + 2, y + 2, circleSize, circleSize);

                // Handle
                int handleX = x + circleSize;
                int handleY = y + circleSize;
                g2d.drawLine(handleX, handleY, x + size - 2, y + size - 2);

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Create an add/plus icon
     */
    public static Icon createAddIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int center = size / 2;
                int lineLength = (int) (size * 0.6);

                // Vertical line
                g2d.drawLine(x + center, y + center - lineLength / 2, x + center, y + center + lineLength / 2);
                // Horizontal line
                g2d.drawLine(x + center - lineLength / 2, y + center, x + center + lineLength / 2, y + center);

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Create an edit/pencil icon
     */
    public static Icon createEditIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(color);

                // Pencil body
                Path2D pencil = new Path2D.Double();
                pencil.moveTo(x + size - 4, y + 2);
                pencil.lineTo(x + 4, y + size - 4);
                pencil.lineTo(x + 2, y + size - 2);
                pencil.lineTo(x + size - 2, y + 4);
                pencil.closePath();
                g2d.fill(pencil);

                // Tip
                g2d.setColor(color.darker());
                g2d.fillPolygon(new int[]{x + 2, x + 4, x + 4}, new int[]{y + size - 2, y + size - 4, y + size - 2}, 3);

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Create a delete/trash icon
     */
    public static Icon createDeleteIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(color);

                // Lid
                g2d.fillRoundRect(x + 2, y + 3, size - 4, size / 6, 2, 2);

                // Can
                Path2D can = new Path2D.Double();
                can.moveTo(x + 4, y + size / 6 + 3);
                can.lineTo(x + 3, y + size - 2);
                can.lineTo(x + size - 3, y + size - 2);
                can.lineTo(x + size - 4, y + size / 6 + 3);
                can.closePath();
                g2d.fill(can);

                // Lines
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawLine(x + size / 2, y + size / 6 + 5, x + size / 2, y + size - 5);

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Create a refresh icon
     */
    public static Icon createRefreshIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // Circular arrow
                Arc2D arc = new Arc2D.Double(x + 3, y + 3, size - 6, size - 6, 45, 270, Arc2D.OPEN);
                g2d.draw(arc);

                // Arrow head
                int[] xPoints = {x + size - 4, x + size - 4, x + size - 8};
                int[] yPoints = {y + size / 2 - 2, y + size / 2 + 4, y + size / 2 + 1};
                g2d.fillPolygon(xPoints, yPoints, 3);

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }
}
