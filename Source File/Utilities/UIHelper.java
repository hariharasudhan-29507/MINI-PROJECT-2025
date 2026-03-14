package utils;

import ui.themes.ThemeConstants;
import javax.swing.*;
import java.awt.*;

public class UIHelper {
    
    /**
     * Create styled label
     */
    public static JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    /**
     * Create title label
     */
    public static JLabel createTitleLabel(String text) {
        return createLabel(text, ThemeConstants.TITLE_FONT, ThemeConstants.PRIMARY_RED);
    }

    /**
     * Create heading label
     */
    public static JLabel createHeadingLabel(String text) {
        return createLabel(text, ThemeConstants.HEADING_FONT, ThemeConstants.PRIMARY_RED);
    }

    /**
     * Create body label
     */
    public static JLabel createBodyLabel(String text) {
        return createLabel(text, ThemeConstants.BODY_FONT, ThemeConstants.TEXT_WHITE);
    }

    /**
     * Create styled panel
     */
    public static JPanel createPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        return panel;
    }

    /**
     * Create bordered panel
     */
    public static JPanel createBorderedPanel(LayoutManager layout) {
        JPanel panel = createPanel(layout);
        panel.setBorder(BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2));
        return panel;
    }

    /**
     * Show error dialog
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show success dialog
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show confirmation dialog
     */
    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirm", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Show input dialog
     */
    public static String showInput(Component parent, String message) {
        return JOptionPane.showInputDialog(parent, message);
    }

    /**
     * Center window on screen
     */
    public static void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }

    /**
     * Apply theme to dialog
     */
    public static void applyThemeToDialog(JDialog dialog) {
        dialog.getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
    }

    /**
     * Create vertical spacer
     */
    public static Component createVerticalSpacer(int height) {
        return Box.createVerticalStrut(height);
    }

    /**
     * Create horizontal spacer
     */
    public static Component createHorizontalSpacer(int width) {
        return Box.createHorizontalStrut(width);
    }

    /**
     * Format fighter record string
     */
    public static String formatRecord(int wins, int losses, int draws) {
        return wins + "-" + losses + "-" + draws;
    }

    /**
     * Format percentage
     */
    public static String formatPercentage(double value) {
        return String.format("%.2f%%", value);
    }

    /**
     * Format points
     */
    public static String formatPoints(double points) {
        return String.format("%.2f pts", points);
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Validate number input
     */
    public static boolean isValidNumber(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate integer input
     */
    public static boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}