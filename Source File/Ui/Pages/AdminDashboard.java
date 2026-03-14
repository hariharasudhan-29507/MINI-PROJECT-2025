package ui.Pages;

import ui.components.CustomButton;
import ui.components.NavigationBar;
import ui.themes.ThemeConstants;

import javax.swing.*;
import java.awt.*;

/**
 * AdminDashboard.java - Admin control panel
 * Features:
 * - Manage Players/Fighters
 * - Manage Matches
 * - Verify Users
 * - View Rankings
 * - Manage Events
 * - View Analytics
 */
public class AdminDashboard extends JFrame {
    
    private NavigationBar navigationBar;
    private String adminUsername;
    private JPanel mainPanel;
    
    private CustomButton managePlayersBtn;
    private CustomButton manageMatchesBtn;
    private CustomButton verifyUsersBtn;
    private CustomButton viewRankingsBtn;
    private CustomButton manageEventsBtn;
    private CustomButton analyticsBtn;
    private CustomButton logoutBtn;
    
    public AdminDashboard(String username) {
        this.adminUsername = username;
        
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
        setTitle("UFC Management System - Admin Dashboard");
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
        navigationBar.setLogoText("UFC - ADMIN");
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
        
        JLabel welcomeLabel = new JLabel("Welcome, " + adminUsername + " (Admin)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(ThemeConstants.PRIMARY_RED);
        panel.add(welcomeLabel, BorderLayout.WEST);
        
        JLabel dateLabel = new JLabel(java.time.LocalDate.now().toString());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setForeground(ThemeConstants.TEXT_GRAY);
        panel.add(dateLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Create options panel
     */
    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 20, 20));
        panel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Manage Players Button
        managePlayersBtn = new CustomButton("👥 Manage Fighters");
        managePlayersBtn.setPreferredSize(new Dimension(300, 150));
        managePlayersBtn.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(createOptionCard(managePlayersBtn, "Add, edit, delete fighters"));
        
        // Manage Matches Button
        manageMatchesBtn = new CustomButton("🥊 Manage Matches");
        manageMatchesBtn.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(createOptionCard(manageMatchesBtn, "Schedule and manage matches"));
        
        // Verify Users Button
        verifyUsersBtn = new CustomButton("✓ Verify Users");
        verifyUsersBtn.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(createOptionCard(verifyUsersBtn, "Approve or reject user registrations"));
        
        // View Rankings Button
        viewRankingsBtn = new CustomButton("📊 P4P Rankings");
        viewRankingsBtn.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(createOptionCard(viewRankingsBtn, "View and recalculate rankings"));
        
        // Manage Events Button
        manageEventsBtn = new CustomButton("📅 Manage Events");
        manageEventsBtn.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(createOptionCard(manageEventsBtn, "Create and manage UFC events"));
        
        // Analytics Button
        analyticsBtn = new CustomButton("📈 Analytics");
        analyticsBtn.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(createOptionCard(analyticsBtn, "View system analytics and reports"));
        
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
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        button.setPreferredSize(new Dimension(250, 80));
        card.add(button, BorderLayout.CENTER);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
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
        
        managePlayersBtn.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "Manage Fighters - Feature Coming Soon", 
                "Info", JOptionPane.INFORMATION_MESSAGE));
        
        manageMatchesBtn.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "Manage Matches - Feature Coming Soon", 
                "Info", JOptionPane.INFORMATION_MESSAGE));
        
        verifyUsersBtn.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "Verify Users - Feature Coming Soon", 
                "Info", JOptionPane.INFORMATION_MESSAGE));
        
        viewRankingsBtn.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "View Rankings - Feature Coming Soon", 
                "Info", JOptionPane.INFORMATION_MESSAGE));
        
        manageEventsBtn.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "Manage Events - Feature Coming Soon", 
                "Info", JOptionPane.INFORMATION_MESSAGE));
        
        analyticsBtn.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "Analytics - Feature Coming Soon", 
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