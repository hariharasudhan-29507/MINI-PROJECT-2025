package ui.components;

import ui.themes.ThemeConstants;
import javax.swing.*;
import java.awt.*;

public class CustomPanel extends JPanel {
    private boolean hasBorder;
    private Color borderColor;
    private int borderWidth;

    public CustomPanel() {
        this(new FlowLayout(), false);
    }

    public CustomPanel(LayoutManager layout) {
        this(layout, false);
    }

    public CustomPanel(LayoutManager layout, boolean hasBorder) {
        super(layout);
        this.hasBorder = hasBorder;
        this.borderColor = ThemeConstants.PRIMARY_RED;
        this.borderWidth = 2;
        
        setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        if (hasBorder) {
            setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
        }
    }

    public void setBorderEnabled(boolean enabled) {
        this.hasBorder = enabled;
        if (enabled) {
            setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
        } else {
            setBorder(null);
        }
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        if (hasBorder) {
            setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
        }
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        if (hasBorder) {
            setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (hasBorder) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(borderWidth));
            g2d.drawRect(borderWidth / 2, borderWidth / 2, 
                getWidth() - borderWidth, getHeight() - borderWidth);
            g2d.dispose();
        }
    }
}