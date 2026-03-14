package ui.Pages;

import ui.components.CustomButton;
import ui.components.CustomTextField;
import ui.themes.ThemeConstants;

import javax.swing.*;
import java.awt.*;

/**
 * FeedbackPage.java - COMPLETE FIXED:
 * - Removed About Us, News, Feedback buttons from navigation
 * - Only Back button in top navigation
 * - Full feedback form centered
 * - All previous features maintained
 */
public class FeedbackPage extends JFrame {
    
    private CustomTextField nameField;
    private CustomTextField emailField;
    private JTextArea feedbackArea;
    private CustomButton submitBtn;
    private CustomButton clearBtn;
    private CustomButton backBtn;
    
    public FeedbackPage() {
        initializeFrame();
        setupLayout();
        setupComponents();
        addListeners();
        setVisible(true);
    }
    
    private void initializeFrame() {
        setTitle("Feedback - UFC Management System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with ONLY Back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(ThemeConstants.PRIMARY_BLACK);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ThemeConstants.PRIMARY_RED));
        
        backBtn = new CustomButton("← Back");
        backBtn.setPreferredSize(new Dimension(120, 45));
        backBtn.addActionListener(e -> this.dispose());
        topPanel.add(backBtn);
        add(topPanel, BorderLayout.NORTH);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        add(mainPanel, BorderLayout.CENTER);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Title
        JLabel titleLabel = new JLabel("Send Us Your Feedback");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);
        
        // Name field
        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(ThemeConstants.TEXT_WHITE);
        formPanel.add(nameLabel, gbc);
        
        nameField = new CustomTextField("Enter your name", 30);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        // Email field
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(ThemeConstants.TEXT_WHITE);
        formPanel.add(emailLabel, gbc);
        
        emailField = new CustomTextField("Enter your email", 30);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        // Feedback text area
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel feedbackLabel = new JLabel("Feedback:");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
        feedbackLabel.setForeground(ThemeConstants.TEXT_WHITE);
        formPanel.add(feedbackLabel, gbc);
        
        feedbackArea = new JTextArea(8, 30);
        feedbackArea.setFont(new Font("Arial", Font.PLAIN, 13));
        feedbackArea.setBackground(ThemeConstants.SECONDARY_BLACK);
        feedbackArea.setForeground(ThemeConstants.TEXT_WHITE);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setBorder(BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2));
        
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        scrollPane.getViewport().setBackground(ThemeConstants.SECONDARY_BLACK);
        scrollPane.setBorder(null);
        gbc.gridx = 1;
        gbc.weighty = 1.0;
        formPanel.add(scrollPane, gbc);
        
        // Buttons
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        submitBtn = new CustomButton("Submit Feedback");
        submitBtn.setPreferredSize(new Dimension(160, 45));
        buttonPanel.add(submitBtn);
        
        clearBtn = new CustomButton("Clear");
        clearBtn.setPreferredSize(new Dimension(120, 45));
        buttonPanel.add(clearBtn);
        
        formPanel.add(buttonPanel, gbc);
    }
    
    private void setupComponents() {
        // Components already added in setupLayout
    }
    
    private void addListeners() {
        submitBtn.addActionListener(e -> handleSubmit());
        clearBtn.addActionListener(e -> handleClear());
    }
    
    private void handleSubmit() {
        String name = nameField.getActualText().trim();
        String email = emailField.getActualText().trim();
        String feedback = feedbackArea.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter your name!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter your email!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (feedback.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter your feedback!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this,
            "Thank you for your feedback!\n\n" +
            "Name: " + name + "\n" +
            "Email: " + email + "\n\n" +
            "We appreciate your input.",
            "Feedback Submitted",
            JOptionPane.INFORMATION_MESSAGE);
        
        handleClear();
    }
    
    private void handleClear() {
        nameField.setText("");
        emailField.setText("");
        feedbackArea.setText("");
    }
}