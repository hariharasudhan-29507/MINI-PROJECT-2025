package ui.components;

import ui.themes.ThemeConstants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class NavigationBar extends JPanel {
    private JLabel logoLabel;
    private CustomButton aboutBtn;
    private CustomButton newsBtn;
    private CustomButton feedbackBtn;
    private CustomButton backBtn;
    private CustomButton logoutBtn;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;
    private List<NavigationBarListener> listeners;
    private boolean showBackButton;
    private boolean showLogoutButton;

    // Constructor
    public NavigationBar(boolean showBackButton, boolean showLogoutButton) {
        this.showBackButton = showBackButton;
        this.showLogoutButton = showLogoutButton;
        this.listeners = new ArrayList<>();
        
        setLayout(new BorderLayout());
        setBackground(ThemeConstants.PRIMARY_BLACK);
        setPreferredSize(new Dimension(0, 75));
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ThemeConstants.PRIMARY_RED));
        
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        // Left Panel - Logo and Back Button
        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        leftPanel.setBackground(ThemeConstants.PRIMARY_BLACK);
        leftPanel.setOpaque(true);
        
        // UFC Logo
        logoLabel = new JLabel("UFC");
        logoLabel.setFont(new Font("Arial Black", Font.BOLD, 32));
        logoLabel.setForeground(ThemeConstants.PRIMARY_RED);
        leftPanel.add(logoLabel);
        
        // Back Button (if needed)
        if (showBackButton) {
            backBtn = new CustomButton("← Back", ThemeConstants.SECONDARY_BLACK, 
                ThemeConstants.PRIMARY_RED, ThemeConstants.DARK_RED);
            backBtn.setPreferredSize(new Dimension(120, 45));
            leftPanel.add(backBtn);
        }
        
        // Center Panel - Navigation Buttons
        centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        centerPanel.setBackground(ThemeConstants.PRIMARY_BLACK);
        centerPanel.setOpaque(true);
        
        // About Us Button
        aboutBtn = createNavButton("About Us");
        centerPanel.add(aboutBtn);
        
        // News Button
        newsBtn = createNavButton("News");
        centerPanel.add(newsBtn);
        
        // Feedback Button
        feedbackBtn = createNavButton("Feedback");
        centerPanel.add(feedbackBtn);
        
        // Right Panel - Logout Button
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        rightPanel.setBackground(ThemeConstants.PRIMARY_BLACK);
        rightPanel.setOpaque(true);
        
        // Logout Button (if needed)
        if (showLogoutButton) {
            logoutBtn = new CustomButton("Logout", ThemeConstants.SECONDARY_BLACK, 
                ThemeConstants.PRIMARY_RED, ThemeConstants.DARK_RED);
            logoutBtn.setPreferredSize(new Dimension(120, 45));
            rightPanel.add(logoutBtn);
        }
    }

    private CustomButton createNavButton(String text) {
        CustomButton btn = new CustomButton(text, ThemeConstants.SECONDARY_BLACK, 
            ThemeConstants.PRIMARY_RED, ThemeConstants.DARK_RED);
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setFont(ThemeConstants.BODY_FONT);
        return btn;
    }

    private void setupLayout() {
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    // Add listeners for buttons
    public void addAboutListener(ActionListener listener) {
        aboutBtn.addActionListener(listener);
    }

    public void addNewsListener(ActionListener listener) {
        newsBtn.addActionListener(listener);
    }

    public void addFeedbackListener(ActionListener listener) {
        feedbackBtn.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        if (backBtn != null) {
            backBtn.addActionListener(listener);
        }
    }

    public void addLogoutListener(ActionListener listener) {
        if (logoutBtn != null) {
            logoutBtn.addActionListener(listener);
        }
    }

    // Update button states
    public void setBackButtonEnabled(boolean enabled) {
        if (backBtn != null) {
            backBtn.setEnabled(enabled);
        }
    }

    public void setLogoutButtonEnabled(boolean enabled) {
        if (logoutBtn != null) {
            logoutBtn.setEnabled(enabled);
        }
    }

    public void setAboutButtonEnabled(boolean enabled) {
        aboutBtn.setEnabled(enabled);
    }

    public void setNewsButtonEnabled(boolean enabled) {
        newsBtn.setEnabled(enabled);
    }

    public void setFeedbackButtonEnabled(boolean enabled) {
        feedbackBtn.setEnabled(enabled);
    }

    // Update logo
    public void setLogoText(String text) {
        logoLabel.setText(text);
    }

    public void setLogoColor(Color color) {
        logoLabel.setForeground(color);
    }

    // Update button texts
    public void setAboutButtonText(String text) {
        aboutBtn.setText(text);
    }

    public void setNewsButtonText(String text) {
        newsBtn.setText(text);
    }

    public void setFeedbackButtonText(String text) {
        feedbackBtn.setText(text);
    }

    // Get buttons for advanced customization
    public CustomButton getAboutButton() {
        return aboutBtn;
    }

    public CustomButton getNewsButton() {
        return newsBtn;
    }

    public CustomButton getFeedbackButton() {
        return feedbackBtn;
    }

    public CustomButton getBackButton() {
        return backBtn;
    }

    public CustomButton getLogoutButton() {
        return logoutBtn;
    }

    // Listener interface
    public interface NavigationBarListener {
        void onAboutClicked();
        void onNewsClicked();
        void onFeedbackClicked();
        void onBackClicked();
        void onLogoutClicked();
    }

    public void addNavigationListener(NavigationBarListener listener) {
        listeners.add(listener);
    }

    public void removeNavigationListener(NavigationBarListener listener) {
        listeners.remove(listener);
    }

    // Refresh appearance
    public void refresh() {
        repaint();
        revalidate();
    }

    // Hide/Show specific components
    public void setNavigationButtonsVisible(boolean visible) {
        aboutBtn.setVisible(visible);
        newsBtn.setVisible(visible);
        feedbackBtn.setVisible(visible);
    }

    public void setBackButtonVisible(boolean visible) {
        if (backBtn != null) {
            backBtn.setVisible(visible);
        }
    }

    public void setLogoutButtonVisible(boolean visible) {
        if (logoutBtn != null) {
            logoutBtn.setVisible(visible);
        }
    }
}