package ui.Pages;

import ui.components.CustomButton;
import ui.components.NavigationBar;
import ui.themes.ThemeConstants;

import javax.swing.*;
import java.awt.*;

/**
 * UserDashboard.java - User dashboard
 * Features:
 * - Search Players
 * - View Rankings
 * - View Matches
 * - Manage Profile
 */
public class UserDashboard extends JFrame {
    
    private NavigationBar navigationBar;
    private String username;
    private JPanel mainPanel;
    
    private CustomButton searchPlayersBtn;
    private CustomButton viewRankingsBtn;
    private CustomButton viewMatchesBtn;
    private CustomButton profileBtn;
    private CustomButton logoutBtn;
    
    public UserDashboard(String username) {
        this.username = username;
        
        initializeFrame();
        setupLayout();
        setupComponents();
        addListeners();
        setVisible(true);
    }
    
    /**
     * Initialize frame
     */
    private void initializeFrame() {
        setTitle("UFC Management System - User Dashboard");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
    }
    
    /**
     * Setup main layout
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Navigation bar
        navigationBar = new NavigationBar(true, true);
        navigationBar.setLogoText("UFC - USER");
        add(navigationBar, BorderLayout.NORTH);
        
        // Main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Setup components
     */
    private void setupComponents() {
        // Welcome section
        JPanel welcomePanel = createWelcomePanel();
        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        
        // Options grid
        JPanel optionsPanel = createOptionsPanel();
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create welcome panel
     */
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeConstants.PRIMARY_BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(ThemeConstants.PRIMARY_RED);
        panel.add(welcomeLabel, BorderLayout.WEST);
        
        JLabel infoLabel = new JLabel("Explore fighters and rankings");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoLabel.setForeground(ThemeConstants.TEXT_GRAY);
        panel.add(infoLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Create options panel
     */
    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 30, 30));
        panel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));
        
        // Search Players Button
        searchPlayersBtn = new CustomButton("🔍 Search Players");
        searchPlayersBtn.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(createOptionCard(searchPlayersBtn, "Find and view fighter profiles"));
        
        // View Rankings Button
        viewRankingsBtn = new CustomButton("📊 View Rankings");
        viewRankingsBtn.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(createOptionCard(viewRankingsBtn, "Check P4P rankings by weight class"));
        
        // View Matches Button
        viewMatchesBtn = new CustomButton("🥊 View Matches");
        viewMatchesBtn.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(createOptionCard(viewMatchesBtn, "Browse scheduled and completed matches"));
        
        // Profile Button
        profileBtn = new CustomButton("👤 My Profile");
        profileBtn.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(createOptionCard(profileBtn, "View and edit your profile"));
        
        return panel;
    }
    
    /**
     * Create option card
     */
    private JPanel createOptionCard(CustomButton button, String description) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(ThemeConstants.SECONDARY_BLACK);
        card.setBorder(BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        button.setPreferredSize(new Dimension(200, 100));
        card.add(button, BorderLayout.CENTER);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        descLabel.setForeground(ThemeConstants.TEXT_GRAY);
        card.add(descLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    /**
     * Create footer panel
     */
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        panel.setBackground(ThemeConstants.PRIMARY_BLACK);
        
        logoutBtn = new CustomButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(120, 40));
        panel.add(logoutBtn);
        
        return panel;
    }
    
    /**
     * Add action listeners
     */
    private void addListeners() {
        navigationBar.addBackListener(e -> {
            new LandingPage();
            this.dispose();
        });
        
        navigationBar.addLogoutListener(e -> handleLogout());
        
        searchPlayersBtn.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "Search Players - Feature Coming Soon", 
                "Info", JOptionPane.INFORMATION_MESSAGE));
        
        viewRankingsBtn.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "View Rankings - Feature Coming Soon", 
                "Info", JOptionPane.INFORMATION_MESSAGE));
        
        viewMatchesBtn.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "View Matches - Feature Coming Soon", 
                "Info", JOptionPane.INFORMATION_MESSAGE));
        
        profileBtn.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "My Profile - Feature Coming Soon", 
                "Info", JOptionPane.INFORMATION_MESSAGE));
        
        logoutBtn.addActionListener(e -> handleLogout());
    }
    
    /**
     * Handle logout
     */
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            new LandingPage();
            this.dispose();
        }
    }
}