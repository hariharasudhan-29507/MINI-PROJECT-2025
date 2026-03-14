
package ui.components;

import ui.themes.ThemeConstants;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class CustomButton extends JButton {
    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color borderColor;
    private boolean isHovered;
    private boolean isPressed;
    private int radius;
    private int borderWidth;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;

    // Constructors
    public CustomButton(String text) {
        this(text, ThemeConstants.PRIMARY_RED, ThemeConstants.HOVER_RED, ThemeConstants.DARK_RED);
    }

    public CustomButton(String text, Color bgColor, Color hoverColor, Color pressedColor) {
        super(text);
        this.backgroundColor = bgColor;
        this.hoverColor = hoverColor;
        this.pressedColor = pressedColor;
        this.borderColor = ThemeConstants.TEXT_WHITE;
        this.radius = ThemeConstants.BORDER_RADIUS;
        this.borderWidth = 2;
        this.paddingLeft = 20;
        this.paddingRight = 20;
        this.paddingTop = 10;
        this.paddingBottom = 10;
        this.isHovered = false;
        this.isPressed = false;

        initializeButton();
    }

    private void initializeButton() {
        setFont(ThemeConstants.BUTTON_FONT);
        setForeground(ThemeConstants.TEXT_WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Determine button color based on state
        Color currentColor = backgroundColor;
        if (!isEnabled()) {
            currentColor = new Color(100, 100, 100);  // Disabled gray
        } else if (isPressed) {
            currentColor = pressedColor;
        } else if (isHovered) {
            currentColor = hoverColor;
        }

        // Draw rounded rectangle background
        g2d.setColor(currentColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Draw border
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.drawRoundRect(borderWidth / 2, borderWidth / 2, 
            getWidth() - borderWidth, getHeight() - borderWidth, radius, radius);

        // Draw shadow effect when pressed
        if (isPressed) {
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
        }

        // Draw text
        g2d.setColor(getForeground());
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        String text = getText();
        int textX = (getWidth() - fm.stringWidth(text)) / 2;
        int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(text, textX, textY);

        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = this.getFontMetrics(getFont());
        String text = getText();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        
        int width = textWidth + paddingLeft + paddingRight + 10;
        int height = textHeight + paddingTop + paddingBottom + 10;
        
        return new Dimension(Math.max(width, ThemeConstants.BUTTON_WIDTH), 
                           Math.max(height, ThemeConstants.BUTTON_HEIGHT));
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }

    // Setters for customization
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }

    public void setHoverColor(Color color) {
        this.hoverColor = color;
        repaint();
    }

    public void setPressedColor(Color color) {
        this.pressedColor = color;
        repaint();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        repaint();
    }

    public void setPadding(int left, int right, int top, int bottom) {
        this.paddingLeft = left;
        this.paddingRight = right;
        this.paddingTop = top;
        this.paddingBottom = bottom;
        revalidate();
    }

    // Getters
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public Color getPressedColor() {
        return pressedColor;
    }

    public int getRadius() {
        return radius;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public boolean isPressed() {
        return isPressed;
    }
}