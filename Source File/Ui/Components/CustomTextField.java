package ui.components;

import ui.themes.ThemeConstants;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class CustomTextField extends JTextField {
    private String placeholder;
    private boolean showingPlaceholder;
    private Color placeholderColor;
    private Color focusedBorderColor;
    private Color unfocusedBorderColor;
    private int borderRadius;
    private int borderWidth;
    private boolean isFocused;

    // Constructors
    public CustomTextField() {
        this("", 20);
    }

    public CustomTextField(int columns) {
        this("", columns);
    }

    public CustomTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        this.placeholderColor = ThemeConstants.TEXT_GRAY;
        this.focusedBorderColor = ThemeConstants.PRIMARY_RED;
        this.unfocusedBorderColor = ThemeConstants.TEXT_GRAY;
        this.borderRadius = 15;
        this.borderWidth = 2;
        this.isFocused = false;
        this.showingPlaceholder = true;

        initializeTextField();
        displayPlaceholder();
    }

    private void initializeTextField() {
        setFont(ThemeConstants.BODY_FONT);
        setForeground(ThemeConstants.TEXT_WHITE);
        setBackground(ThemeConstants.SECONDARY_BLACK);
        setCaretColor(ThemeConstants.PRIMARY_RED);
        setOpaque(false);
        setBorder(new RoundedBorder());
        setPreferredSize(new Dimension(300, 45));
        setMargin(new Insets(10, 15, 10, 15));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                if (showingPlaceholder) {
                    setText("");
                    setForeground(ThemeConstants.TEXT_WHITE);
                    showingPlaceholder = false;
                }
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                if (getText().isEmpty()) {
                    displayPlaceholder();
                }
                repaint();
            }
        });
    }

    private void displayPlaceholder() {
        setText(placeholder);
        setForeground(placeholderColor);
        showingPlaceholder = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2d.setColor(ThemeConstants.SECONDARY_BLACK);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);

        g2d.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determine border color based on focus state
        Color borderColor = isFocused ? focusedBorderColor : unfocusedBorderColor;
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.drawRoundRect(borderWidth / 2, borderWidth / 2, 
            getWidth() - borderWidth, getHeight() - borderWidth, 
            borderRadius, borderRadius);

        g2d.dispose();
    }

    public String getActualText() {
        if (showingPlaceholder) {
            return "";
        }
        return getText().trim();
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (showingPlaceholder) {
            displayPlaceholder();
        }
    }

    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        repaint();
    }

    public void setFocusedBorderColor(Color color) {
        this.focusedBorderColor = color;
        repaint();
    }

    public void setUnfocusedBorderColor(Color color) {
        this.unfocusedBorderColor = color;
        repaint();
    }

    public void setBorderRadius(int radius) {
        this.borderRadius = radius;
        repaint();
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        repaint();
    }

    public boolean isPlaceholderVisible() {
        return showingPlaceholder;
    }

    public boolean isEmpty() {
        return getText().isEmpty() || showingPlaceholder;
    }

    @Override
    public void setText(String t) {
        if (t != null && !t.isEmpty() && !t.equals(placeholder)) {
            showingPlaceholder = false;
            setForeground(ThemeConstants.TEXT_WHITE);
        }
        super.setText(t);
    }

    /**
     * Custom border class for rounded corners
     */
    private class RoundedBorder implements Border {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color borderColor = isFocused ? focusedBorderColor : unfocusedBorderColor;
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(borderWidth));
            g2d.drawRoundRect(x + borderWidth / 2, y + borderWidth / 2, 
                width - borderWidth, height - borderWidth, borderRadius, borderRadius);
            
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(10, 15, 10, 15);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}