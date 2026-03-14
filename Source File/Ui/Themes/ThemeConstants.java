package ui.themes;

import java.awt.*;

public class ThemeConstants {
    
    // ============ COLOR PALETTE ============
    
    // Primary Colors - UFC Black & Red Theme
    public static final Color PRIMARY_RED = new Color(220, 20, 60);           // Crimson Red
    public static final Color DARK_RED = new Color(178, 0, 0);                // Dark Red (pressed state)
    public static final Color HOVER_RED = new Color(240, 50, 90);             // Light Red (hover state)
    public static final Color LIGHT_RED = new Color(255, 100, 130);           // Light Red (accents)
    
    // Black Shades
    public static final Color PRIMARY_BLACK = new Color(20, 20, 20);          // Pure Black
    public static final Color BACKGROUND_BLACK = new Color(25, 25, 25);       // Background Black
    public static final Color SECONDARY_BLACK = new Color(40, 40, 40);        // Secondary Black (panels)
    
    // Text Colors
    public static final Color TEXT_WHITE = new Color(255, 255, 255);          // Pure White
    public static final Color TEXT_GRAY = new Color(160, 160, 160);           // Gray Text
    public static final Color TEXT_LIGHT_GRAY = new Color(200, 200, 200);     // Light Gray
    
    // Status Colors
    public static final Color SUCCESS_GREEN = new Color(76, 175, 80);         // Success/Active
    public static final Color ERROR_RED = new Color(244, 67, 54);             // Error
    public static final Color WARNING_ORANGE = new Color(255, 152, 0);        // Warning
    public static final Color INFO_BLUE = new Color(33, 150, 243);            // Info
    
    // ============ FONTS ============
    
    // Font families
    public static final String FONT_FAMILY_MAIN = "Arial";
    public static final String FONT_FAMILY_MONO = "Courier New";
    
    // Font sizes
    public static final int FONT_SIZE_TITLE = 36;
    public static final int FONT_SIZE_HEADING = 24;
    public static final int FONT_SIZE_SUBHEADING = 18;
    public static final int FONT_SIZE_BODY = 14;
    public static final int FONT_SIZE_SMALL = 12;
    public static final int FONT_SIZE_BUTTON = 14;
    
    // Font definitions
    public static final Font TITLE_FONT = new Font(FONT_FAMILY_MAIN, Font.BOLD, FONT_SIZE_TITLE);
    public static final Font HEADING_FONT = new Font(FONT_FAMILY_MAIN, Font.BOLD, FONT_SIZE_HEADING);
    public static final Font SUBHEADING_FONT = new Font(FONT_FAMILY_MAIN, Font.BOLD, FONT_SIZE_SUBHEADING);
    public static final Font BODY_FONT = new Font(FONT_FAMILY_MAIN, Font.PLAIN, FONT_SIZE_BODY);
    public static final Font BODY_BOLD_FONT = new Font(FONT_FAMILY_MAIN, Font.BOLD, FONT_SIZE_BODY);
    public static final Font SMALL_FONT = new Font(FONT_FAMILY_MAIN, Font.PLAIN, FONT_SIZE_SMALL);
    public static final Font BUTTON_FONT = new Font(FONT_FAMILY_MAIN, Font.BOLD, FONT_SIZE_BUTTON);
    public static final Font MONO_FONT = new Font(FONT_FAMILY_MONO, Font.PLAIN, FONT_SIZE_BODY);
    
    // ============ COMPONENT SIZING ============
    
    // Button dimensions
    public static final int BUTTON_WIDTH = 140;
    public static final int BUTTON_HEIGHT = 45;
    public static final int BUTTON_SMALL_WIDTH = 100;
    public static final int BUTTON_SMALL_HEIGHT = 35;
    public static final int BUTTON_LARGE_WIDTH = 200;
    public static final int BUTTON_LARGE_HEIGHT = 55;
    
    // Text field dimensions
    public static final int TEXTFIELD_WIDTH = 300;
    public static final int TEXTFIELD_HEIGHT = 45;
    public static final int TEXTFIELD_SMALL_WIDTH = 200;
    public static final int TEXTFIELD_LARGE_WIDTH = 400;
    
    // Window dimensions
    public static final int WINDOW_MIN_WIDTH = 800;
    public static final int WINDOW_MIN_HEIGHT = 600;
    public static final int WINDOW_MAX_WIDTH = 1600;
    public static final int WINDOW_MAX_HEIGHT = 1000;
    
    // Panel heights
    public static final int NAVBAR_HEIGHT = 75;
    public static final int FOOTER_HEIGHT = 50;
    public static final int SIDEBAR_WIDTH = 250;
    
    // ============ STYLING PROPERTIES ============
    
    // Border properties
    public static final int BORDER_RADIUS = 15;
    public static final int BORDER_WIDTH = 2;
    public static final int BORDER_WIDTH_THICK = 3;
    
    // Padding/Margin properties
    public static final int PADDING_XS = 5;
    public static final int PADDING_S = 10;
    public static final int PADDING_M = 15;
    public static final int PADDING_L = 20;
    public static final int PADDING_XL = 30;
    
    // Gap properties
    public static final int GAP_S = 5;
    public static final int GAP_M = 10;
    public static final int GAP_L = 15;
    public static final int GAP_XL = 20;
    
    // ============ ANIMATION PROPERTIES ============
    
    public static final int ANIMATION_DURATION = 300;  // milliseconds
    public static final int ANIMATION_DELAY = 50;      // milliseconds
    
    // ============ SHADOW PROPERTIES ============
    
    public static final int SHADOW_SIZE = 5;
    public static final float SHADOW_OPACITY = 0.3f;
    
    // ============ TABLE PROPERTIES ============
    
    public static final int TABLE_ROW_HEIGHT = 35;
    public static final int TABLE_HEADER_HEIGHT = 40;
    
    // ============ UTILITY METHODS ============
    
    /**
     * Get lighter version of a color
     */
    public static Color getLighterColor(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] = Math.min(1.0f, hsb[2] + factor);
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
    
    /**
     * Get darker version of a color
     */
    public static Color getDarkerColor(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] = Math.max(0.0f, hsb[2] - factor);
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
    
    /**
     * Create color with alpha transparency
     */
    public static Color getColorWithAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    /**
     * Get contrasting text color (black or white)
     */
    public static Color getContrastingTextColor(Color backgroundColor) {
        int r = backgroundColor.getRed();
        int g = backgroundColor.getGreen();
        int b = backgroundColor.getBlue();
        
        // Calculate luminance
        double luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255;
        
        // Return white if background is dark, black if background is light
        return luminance > 0.5 ? new Color(0, 0, 0) : new Color(255, 255, 255);
    }
    
    /**
     * Create insets for padding
     */
    public static Insets createPadding(int size) {
        return new Insets(size, size, size, size);
    }
    
    /**
     * Create insets with different values
     */
    public static Insets createPadding(int top, int left, int bottom, int right) {
        return new Insets(top, left, bottom, right);
    }
    
    /**
     * Print theme information
     */
    public static void printThemeInfo() {
        System.out.println("\n========== UFC THEME CONSTANTS ==========");
        System.out.println("Primary Red: " + PRIMARY_RED.getRGB());
        System.out.println("Background Black: " + BACKGROUND_BLACK.getRGB());
        System.out.println("Primary Font: " + TITLE_FONT.getFontName());
        System.out.println("Button Size: " + BUTTON_WIDTH + "x" + BUTTON_HEIGHT);
        System.out.println("Border Radius: " + BORDER_RADIUS);
        System.out.println("=========================================\n");
    }
    
    /**
     * Validate color values
     */
    public static boolean isValidColor(Color color) {
        return color != null && 
               color.getRed() >= 0 && color.getRed() <= 255 &&
               color.getGreen() >= 0 && color.getGreen() <= 255 &&
               color.getBlue() >= 0 && color.getBlue() <= 255;
    }
}