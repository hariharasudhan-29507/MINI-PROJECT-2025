package ui.Pages;

import ui.components.CustomButton;
import ui.components.CustomTextField;
import ui.themes.ThemeConstants;
import services.AuthService;

import javax.swing.*;
import java.awt.*;

/**
 * LoginPage.java - COMPLETE FIXED:
 * - Email is LAST field in Register tab
 * - Login validation FIXED (no more false errors)
 * - Proper field text retrieval
 * - Complete with all features from before
 */
public class LoginPage extends JFrame {
    
    private JTabbedPane tabbedPane;
    
    // Login tab fields
    private CustomTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    
    // Register tab fields
    private CustomTextField registerUsernameField;
    private JPasswordField registerPasswordField;
    private CustomTextField registerEmailField;
    
    private CustomButton loginBtn;
    private CustomButton registerBtn;
    private CustomButton backBtn;
    
    private AuthService authService;
    
    public LoginPage() {
        authService = new AuthService();
        
        initializeFrame();
        setupLayout();
        setVisible(true);
    }
    
    private void initializeFrame() {
        setTitle("UFC Management System - Login");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(ThemeConstants.PRIMARY_BLACK);
        backBtn = new CustomButton("← Back");
        backBtn.setPreferredSize(new Dimension(120, 45));
        backBtn.addActionListener(e -> {
            new LandingPage();
            this.dispose();
        });
        topPanel.add(backBtn);
        add(topPanel, BorderLayout.NORTH);
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(ThemeConstants.BACKGROUND_BLACK);
        tabbedPane.setForeground(ThemeConstants.TEXT_WHITE);
        tabbedPane.setFont(ThemeConstants.BODY_BOLD_FONT);
        
        // Login tab
        tabbedPane.addTab("Login", createLoginPanel());
        
        // Register tab
        tabbedPane.addTab("Register", createRegisterPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Title
        JLabel titleLabel = new JLabel("Login to UFC Management System");
        titleLabel.setFont(ThemeConstants.HEADING_FONT);
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(ThemeConstants.TEXT_WHITE);
        userLabel.setFont(ThemeConstants.BODY_BOLD_FONT);
        panel.add(userLabel, gbc);
        
        loginUsernameField = new CustomTextField("Enter username", 20);
        gbc.gridx = 1;
        panel.add(loginUsernameField, gbc);
        
        // Password (HIDDEN)
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(ThemeConstants.TEXT_WHITE);
        passLabel.setFont(ThemeConstants.BODY_BOLD_FONT);
        panel.add(passLabel, gbc);
        
        loginPasswordField = new JPasswordField(20);
        loginPasswordField.setFont(ThemeConstants.BODY_FONT);
        loginPasswordField.setBackground(ThemeConstants.SECONDARY_BLACK);
        loginPasswordField.setForeground(ThemeConstants.TEXT_WHITE);
        loginPasswordField.setCaretColor(ThemeConstants.PRIMARY_RED);
        loginPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        gbc.gridx = 1;
        panel.add(loginPasswordField, gbc);
        
        // Login Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        loginBtn = new CustomButton("Login");
        loginBtn.setPreferredSize(new Dimension(150, 45));
        loginBtn.addActionListener(e -> handleLogin());
        panel.add(loginBtn, gbc);
        
        gbc.gridy++;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalStrut(1), gbc);
        
        return panel;
    }
    
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Title
        JLabel titleLabel = new JLabel("Register New Account");
        titleLabel.setFont(ThemeConstants.HEADING_FONT);
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Username (FIRST)
        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(ThemeConstants.TEXT_WHITE);
        userLabel.setFont(ThemeConstants.BODY_BOLD_FONT);
        panel.add(userLabel, gbc);
        
        registerUsernameField = new CustomTextField("Enter username", 20);
        gbc.gridx = 1;
        panel.add(registerUsernameField, gbc);
        
        // Password (SECOND, HIDDEN)
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(ThemeConstants.TEXT_WHITE);
        passLabel.setFont(ThemeConstants.BODY_BOLD_FONT);
        panel.add(passLabel, gbc);
        
        registerPasswordField = new JPasswordField(20);
        registerPasswordField.setFont(ThemeConstants.BODY_FONT);
        registerPasswordField.setBackground(ThemeConstants.SECONDARY_BLACK);
        registerPasswordField.setForeground(ThemeConstants.TEXT_WHITE);
        registerPasswordField.setCaretColor(ThemeConstants.PRIMARY_RED);
        registerPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        gbc.gridx = 1;
        panel.add(registerPasswordField, gbc);
        
        // Email (THIRD - LAST)
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(ThemeConstants.TEXT_WHITE);
        emailLabel.setFont(ThemeConstants.BODY_BOLD_FONT);
        panel.add(emailLabel, gbc);
        
        registerEmailField = new CustomTextField("Enter email", 20);
        gbc.gridx = 1;
        panel.add(registerEmailField, gbc);
        
        // Register Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        registerBtn = new CustomButton("Register");
        registerBtn.setPreferredSize(new Dimension(150, 45));
        registerBtn.addActionListener(e -> handleRegister());
        panel.add(registerBtn, gbc);
        
        gbc.gridy++;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalStrut(1), gbc);
        
        return panel;
    }
    
    private void handleLogin() {
        // FIX: Properly get text from fields
        String username = loginUsernameField.getActualText().trim();
        String password = new String(loginPasswordField.getPassword()).trim();
        
        System.out.println("DEBUG: Username = '" + username + "', Password = '" + password + "'");
        
        // FIXED: Proper validation
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter username!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter password!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String userType = authService.login(username, password);
        
        if (userType != null) {
            JOptionPane.showMessageDialog(this,
                "Login successful!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            if ("admin".equalsIgnoreCase(userType)) {
                new AdminDashboard(username);
            } else {
                new UserDashboard(username);
            }
            
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid credentials or account not activated!",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleRegister() {
        String username = registerUsernameField.getActualText().trim();
        String password = new String(registerPasswordField.getPassword()).trim();
        String email = registerEmailField.getActualText().trim();
        
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter username!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter password!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter email!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (authService.register(username, password, email)) {
            JOptionPane.showMessageDialog(this,
                "Registration successful!\nPlease wait for admin approval to login.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            registerUsernameField.setText("");
            registerPasswordField.setText("");
            registerEmailField.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                "Registration failed!\nUsername might already exist.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}