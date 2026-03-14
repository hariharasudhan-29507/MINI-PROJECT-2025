package ui.Pages;

import ui.components.CustomButton;
import ui.components.NavigationBar;
import ui.themes.ThemeConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * LandingPage.java - Main landing page of UFC Management System
 * Features:
 * - Navigation bar with UFC logo
 * - Hero section with welcome message
 * - "Get Started" CTA button
 * - Black & Red theme
 */
public class LandingPage extends JFrame {
    
    private NavigationBar navigationBar;
    private JPanel mainPanel;
    private JPanel heroPanel;
    private CustomButton getStartedBtn;
    
    public LandingPage() {
        initializeFrame();
        setupLayout();
        setupComponents();
        addListeners();
        setVisible(true);
    }
    
    /**
     * Initialize frame properties
     */
    private void initializeFrame() {
        setTitle("UFC Management System - Welcome");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Set frame icon color
        getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
    }
    
    /**
     * Setup main layout
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Add navigation bar at top
        navigationBar = new NavigationBar(false, false);
        add(navigationBar, BorderLayout.NORTH);
        
        // Add main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Setup UI components
     */
    private void setupComponents() {
        // Create hero panel
        heroPanel = new JPanel(new GridBagLayout());
        heroPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Welcome Title
        JLabel welcomeTitle = new JLabel("Welcome To");
        welcomeTitle.setFont(new Font("Arial", Font.PLAIN, 36));
        welcomeTitle.setForeground(ThemeConstants.TEXT_WHITE);
        welcomeTitle.setHorizontalAlignment(SwingConstants.CENTER);
        heroPanel.add(welcomeTitle, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(10, 20, 30, 20);
        
        // Main Title
        JLabel mainTitle = new JLabel("UFC MANAGEMENT SYSTEM");
        mainTitle.setFont(new Font("Arial Black", Font.BOLD, 52));
        mainTitle.setForeground(ThemeConstants.PRIMARY_RED);
        mainTitle.setHorizontalAlignment(SwingConstants.CENTER);
        heroPanel.add(mainTitle, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(10, 20, 50, 20);
        
        // Subtitle
        JLabel subtitle = new JLabel("Your Ultimate Platform for Fighter Management");
        subtitle.setFont(new Font("Arial", Font.ITALIC, 18));
        subtitle.setForeground(ThemeConstants.TEXT_GRAY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        heroPanel.add(subtitle, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(30, 20, 20, 20);
        
        // Get Started Button
        getStartedBtn = new CustomButton("Get Started");
        getStartedBtn.setPreferredSize(new Dimension(200, 60));
        getStartedBtn.setFont(new Font("Arial", Font.BOLD, 18));
        heroPanel.add(getStartedBtn, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(40, 20, 20, 20);
        gbc.weighty = 1.0;
        
        // Features panel
        JPanel featuresPanel = createFeaturesPanel();
        heroPanel.add(featuresPanel, gbc);
        
        // Add hero panel to main panel
        mainPanel.add(heroPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create features panel
     */
    private JPanel createFeaturesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 30, 0));
        panel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        // Feature 1: Manage Fighters
        JPanel feature1 = createFeatureCard(
            "Manage Fighters",
            "Add, edit, and manage\nfighter profiles and records"
        );
        panel.add(feature1);
        
        // Feature 2: Schedule Matches
        JPanel feature2 = createFeatureCard(
            "Schedule Matches",
            "Create and manage UFC\nmatches and events"
        );
        panel.add(feature2);
        
        // Feature 3: P4P Rankings
        JPanel feature3 = createFeatureCard(
            "P4P Rankings",
            "View pound-for-pound\nrankings by weight class"
        );
        panel.add(feature3);
        
        return panel;
    }
    
    /**
     * Create feature card
     */
    private JPanel createFeatureCard(String title, String description) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ThemeConstants.SECONDARY_BLACK);
        card.setBorder(BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel, BorderLayout.NORTH);
        
        // Description
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(ThemeConstants.TEXT_WHITE);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descLabel.setVerticalAlignment(SwingConstants.CENTER);
        card.add(descLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Add action listeners
     */
    private void addListeners() {
        // Get Started button
        getStartedBtn.addActionListener(e -> handleGetStarted());
        
        // Navigation bar buttons
        navigationBar.addAboutListener(e -> handleAbout());
        navigationBar.addNewsListener(e -> handleNews());
        navigationBar.addFeedbackListener(e -> handleFeedback());
    }
    
    /**
     * Handle Get Started button click
     */
    private void handleGetStarted() {
        // Open login page
        new LoginPage();
        this.dispose();
    }
    
    /**
     * Handle About button click
     */
    private void handleAbout() {
        showAboutDialog();
    }
    
    /**
     * Handle News button click
     */

private void handleNews() {
    new NewsPage();
}

private void handleFeedback() {
    new FeedbackPage();
}
    /**
     * Show about dialog
     */
    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog(this, "About UFC Management System", true);
        aboutDialog.setSize(600, 500);
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("About UFC Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Content
        JTextArea contentArea = new JTextArea(
            "UFC MANAGEMENT SYSTEM\n\n" +
            "Developers:\n" +
            "• HARIHARASUDHAN.A - 24BCS040, II CSE A\n" +
            "• HARIPRASAD.V - 24BCS041, II CSE A\n" +
            "• MUHAMMED YOUSUF.M - 24BCS051, II CSE A\n\n" +
            "Project Description:\n" +
            "This system is designed to store, retrieve, and manage player details, \n" +
            "match details management, calculate P4P (Pound-for-Pound) rankings, \n" +
            "and handle bank transactions for UFC events.\n\n" +
            "Technologies Used:\n" +
            "• Frontend: Java Swing\n" +
            "• Backend: Java\n" +
            "• Database: Oracle SQL\n" +
            "• Version: 1.0.0"
        );
        contentArea.setEditable(false);
        contentArea.setBackground(ThemeConstants.SECONDARY_BLACK);
        contentArea.setForeground(ThemeConstants.TEXT_WHITE);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 13));
        contentArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.getViewport().setBackground(ThemeConstants.SECONDARY_BLACK);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        CustomButton closeBtn = new CustomButton("Close");
        closeBtn.addActionListener(e -> aboutDialog.dispose());
        buttonPanel.add(closeBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        aboutDialog.add(panel);
        aboutDialog.setVisible(true);
    }
}