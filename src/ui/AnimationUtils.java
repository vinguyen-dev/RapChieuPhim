package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for smooth animations and transitions
 */
public class AnimationUtils {

    /**
     * Fade in animation for component
     */
    public static void fadeIn(Component component, int duration) {
        if (!component.isVisible()) {
            component.setVisible(true);
        }

        if (!(component instanceof JComponent)) {
            return;
        }

        JComponent jc = (JComponent) component;
        Timer timer = new Timer(20, null);
        final float[] opacity = {0f};
        final float step = 1f / (duration / 20f);

        timer.addActionListener(e -> {
            opacity[0] += step;
            if (opacity[0] >= 1f) {
                opacity[0] = 1f;
                timer.stop();
            }

            jc.putClientProperty("alpha", opacity[0]);
            jc.repaint();
        });

        timer.start();
    }

    /**
     * Fade out animation for component
     */
    public static void fadeOut(Component component, int duration) {
        if (!(component instanceof JComponent)) {
            component.setVisible(false);
            return;
        }

        JComponent jc = (JComponent) component;
        Timer timer = new Timer(20, null);
        final float[] opacity = {1f};
        final float step = 1f / (duration / 20f);

        timer.addActionListener(e -> {
            opacity[0] -= step;
            if (opacity[0] <= 0f) {
                opacity[0] = 0f;
                timer.stop();
                jc.setVisible(false);
            }

            jc.putClientProperty("alpha", opacity[0]);
            jc.repaint();
        });

        timer.start();
    }

    /**
     * Slide in from right animation
     */
    public static void slideInFromRight(JComponent component, int duration) {
        if (component.getParent() == null) {
            component.setVisible(true);
            return;
        }

        final int targetX = component.getX();
        final int startX = component.getParent().getWidth();

        component.setLocation(startX, component.getY());
        component.setVisible(true);

        Timer timer = new Timer(10, null);
        final int[] currentX = {startX};
        final int totalSteps = duration / 10;
        final int step = Math.max(1, (startX - targetX) / totalSteps);

        timer.addActionListener(e -> {
            currentX[0] -= step;
            if (currentX[0] <= targetX) {
                currentX[0] = targetX;
                timer.stop();
            }
            component.setLocation(currentX[0], component.getY());
        });

        timer.start();
    }

    /**
     * Slide in from left animation
     */
    public static void slideInFromLeft(JComponent component, int duration) {
        if (component.getParent() == null) {
            component.setVisible(true);
            return;
        }

        final int targetX = component.getX();
        final int startX = -component.getWidth();

        component.setLocation(startX, component.getY());
        component.setVisible(true);

        Timer timer = new Timer(10, null);
        final int[] currentX = {startX};
        final int totalSteps = duration / 10;
        final int step = Math.max(1, (targetX - startX) / totalSteps);

        timer.addActionListener(e -> {
            currentX[0] += step;
            if (currentX[0] >= targetX) {
                currentX[0] = targetX;
                timer.stop();
            }
            component.setLocation(currentX[0], component.getY());
        });

        timer.start();
    }

    /**
     * Smooth scroll to position in scroll pane
     */
    public static void smoothScrollTo(JScrollPane scrollPane, int targetY, int duration) {
        final int startY = scrollPane.getVerticalScrollBar().getValue();
        final int distance = targetY - startY;

        if (distance == 0) {
            return;
        }

        Timer timer = new Timer(10, null);
        final long startTime = System.currentTimeMillis();

        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1f, (float) elapsed / duration);

            // Ease out cubic for smooth deceleration
            progress = 1 - (float) Math.pow(1 - progress, 3);

            int currentY = startY + (int) (distance * progress);
            scrollPane.getVerticalScrollBar().setValue(currentY);

            if (progress >= 1f) {
                timer.stop();
            }
        });

        timer.start();
    }

    /**
     * Smooth scroll to component
     */
    public static void smoothScrollToComponent(JScrollPane scrollPane, Component component, int duration) {
        Rectangle bounds = component.getBounds();
        smoothScrollTo(scrollPane, bounds.y, duration);
    }

    /**
     * Bounce animation for component (attention grabber)
     */
    public static void bounce(JComponent component) {
        final int originalY = component.getY();
        final int bounceHeight = 10;

        Timer timer = new Timer(30, null);
        final int[] step = {0};
        final int totalSteps = 10;

        timer.addActionListener(e -> {
            step[0]++;

            double angle = (step[0] * Math.PI) / totalSteps;
            int offset = (int) (Math.sin(angle) * bounceHeight);

            component.setLocation(component.getX(), originalY - offset);

            if (step[0] >= totalSteps) {
                component.setLocation(component.getX(), originalY);
                timer.stop();
            }
        });

        timer.start();
    }

    /**
     * Pulse animation for component (scale up and down)
     */
    public static void pulse(JComponent component, int duration) {
        final Dimension originalSize = component.getSize();
        final Point originalLocation = component.getLocation();
        final int scaleAmount = 10;

        Timer timer = new Timer(30, null);
        final long startTime = System.currentTimeMillis();

        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = (float) elapsed / duration;

            if (progress >= 1f) {
                component.setSize(originalSize);
                component.setLocation(originalLocation);
                timer.stop();
                return;
            }

            // Sine wave for pulse effect
            double scale = 1 + (Math.sin(progress * Math.PI * 2) * scaleAmount / originalSize.width);

            int newWidth = (int) (originalSize.width * scale);
            int newHeight = (int) (originalSize.height * scale);
            int newX = originalLocation.x - (newWidth - originalSize.width) / 2;
            int newY = originalLocation.y - (newHeight - originalSize.height) / 2;

            component.setSize(newWidth, newHeight);
            component.setLocation(newX, newY);
        });

        timer.start();
    }

    /**
     * Shake animation (error indication)
     */
    public static void shake(JComponent component) {
        final int originalX = component.getX();
        final int shakeDistance = 5;

        Timer timer = new Timer(50, null);
        final int[] step = {0};
        final int[] direction = {1};

        timer.addActionListener(e -> {
            step[0]++;

            if (step[0] % 2 == 0) {
                direction[0] *= -1;
            }

            int offsetX = direction[0] * shakeDistance;
            component.setLocation(originalX + offsetX, component.getY());

            if (step[0] >= 6) {
                component.setLocation(originalX, component.getY());
                timer.stop();
            }
        });

        timer.start();
    }

    /**
     * Show component with fade and slide animation
     */
    public static void showWithAnimation(JComponent component, int duration) {
        component.setOpaque(false);
        slideInFromLeft(component, duration);
        fadeIn(component, duration);
    }

    /**
     * Hide component with fade and slide animation
     */
    public static void hideWithAnimation(JComponent component, int duration, Runnable onComplete) {
        Timer fadeTimer = new Timer(20, null);
        final float[] opacity = {1f};
        final float step = 1f / (duration / 20f);

        fadeTimer.addActionListener(e -> {
            opacity[0] -= step;
            if (opacity[0] <= 0f) {
                opacity[0] = 0f;
                fadeTimer.stop();
                component.setVisible(false);
                if (onComplete != null) {
                    onComplete.run();
                }
            }

            component.putClientProperty("alpha", opacity[0]);
            component.repaint();
        });

        fadeTimer.start();
    }

    /**
     * Scale up animation
     */
    public static void scaleUp(JComponent component, float targetScale, int duration) {
        final Dimension originalSize = component.getPreferredSize();
        final Point originalLocation = component.getLocation();

        if (originalSize == null) return;

        Timer timer = new Timer(10, null);
        final long startTime = System.currentTimeMillis();

        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1f, (float) elapsed / duration);

            // Ease out
            progress = 1 - (float) Math.pow(1 - progress, 2);

            float currentScale = 1 + (targetScale - 1) * progress;
            int newWidth = (int) (originalSize.width * currentScale);
            int newHeight = (int) (originalSize.height * currentScale);
            int newX = originalLocation.x - (newWidth - originalSize.width) / 2;
            int newY = originalLocation.y - (newHeight - originalSize.height) / 2;

            component.setSize(newWidth, newHeight);
            component.setLocation(newX, newY);

            if (progress >= 1f) {
                timer.stop();
            }
        });

        timer.start();
    }

    /**
     * Scale down animation (back to original)
     */
    public static void scaleDown(JComponent component, int duration) {
        scaleUp(component, 1.0f, duration);
    }

    /**
     * Flash animation with color
     */
    public static void flash(JComponent component, Color flashColor, int duration) {
        final Color originalBg = component.getBackground();
        final boolean wasOpaque = component.isOpaque();

        Timer timer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();

        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1f, (float) elapsed / duration);

            // Fade from flash color to original
            int r = (int) (flashColor.getRed() + (originalBg.getRed() - flashColor.getRed()) * progress);
            int g = (int) (flashColor.getGreen() + (originalBg.getGreen() - flashColor.getGreen()) * progress);
            int b = (int) (flashColor.getBlue() + (originalBg.getBlue() - flashColor.getBlue()) * progress);

            component.setBackground(new Color(r, g, b));
            component.repaint();

            if (progress >= 1f) {
                component.setBackground(originalBg);
                component.setOpaque(wasOpaque);
                timer.stop();
            }
        });

        timer.start();
    }

    /**
     * Loading spinner animation
     */
    public static JComponent createLoadingSpinner(int size, Color color) {
        return new JComponent() {
            private int angle = 0;
            private Timer timer;

            {
                setPreferredSize(new Dimension(size, size));
                timer = new Timer(50, e -> {
                    angle = (angle + 30) % 360;
                    repaint();
                });
                timer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = size / 3;

                for (int i = 0; i < 12; i++) {
                    float alpha = 1f - (i / 12f);
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255)));

                    int currentAngle = (angle + i * 30) % 360;
                    double radians = Math.toRadians(currentAngle);

                    int x = centerX + (int) (radius * Math.cos(radians));
                    int y = centerY + (int) (radius * Math.sin(radians));

                    g2d.fillOval(x - 2, y - 2, 4, 4);
                }

                g2d.dispose();
            }

            public void stopAnimation() {
                if (timer != null) {
                    timer.stop();
                }
            }
        };
    }
}
