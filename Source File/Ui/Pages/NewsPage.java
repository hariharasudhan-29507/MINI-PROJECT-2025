package ui.Pages;

import ui.components.CustomButton;
import ui.themes.ThemeConstants;

import javax.swing.*;
import java.awt.*;

/**
 * NewsPage.java - COMPLETE FIXED:
 * - Removed About Us, News, Feedback buttons from navigation
 * - Only Back button in top navigation
 * - Full-screen news content
 * - All previous features maintained
 */
public class NewsPage extends JFrame {
    
    private JPanel newsPanel;
    private CustomButton backBtn;
    
    public NewsPage() {
        initializeFrame();
        setupLayout();
        setupComponents();
        setVisible(true);
    }
    
    private void initializeFrame() {
        setTitle("UFC News - UFC Management System");
        setSize(1400, 900);
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
        
        // News panel with scroll
        newsPanel = new JPanel();
        newsPanel.setLayout(new BoxLayout(newsPanel, BoxLayout.Y_AXIS));
        newsPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        JScrollPane scrollPane = new JScrollPane(newsPanel);
        scrollPane.getViewport().setBackground(ThemeConstants.BACKGROUND_BLACK);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupComponents() {
        // Title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JLabel titleLabel = new JLabel("UFC News & Updates");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        newsPanel.add(titlePanel);
        
        // Add news items
        addNewsItem("🔴 UFC Championship Fight Announced",
            "Champion vs Challenger match scheduled for December 15, 2025 at T-Mobile Arena, Las Vegas",
            "2025-11-17");
        
        addNewsItem("🎖️ New P4P Ranking Update",
            "The Pound-for-Pound rankings have been updated with latest match results.",
            "2025-11-16");
        
        addNewsItem("👥 New Fighter Registration",
            "Welcome new fighters! Registration is now open for upcoming UFC events.",
            "2025-11-15");
        
        addNewsItem("🥊 Epic Match Results",
            "Catch up on the latest match results and fighter performances from last weekend's event.",
            "2025-11-14");
        
        addNewsItem("📅 Upcoming Events",
            "Check out our calendar for upcoming UFC events and matches in your region.",
            "2025-11-13");
        
        // Add spacer at bottom
        newsPanel.add(Box.createVerticalStrut(50));
    }
    
    private void addNewsItem(String title, String content, String date) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(ThemeConstants.SECONDARY_BLACK);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        
        // Content
        JLabel contentLabel = new JLabel("<html>" + content + "</html>");
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        contentLabel.setForeground(ThemeConstants.TEXT_WHITE);
        
        // Date
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        dateLabel.setForeground(ThemeConstants.TEXT_GRAY);
        
        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConstants.SECONDARY_BLACK);
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(contentLabel, BorderLayout.CENTER);
        centerPanel.add(dateLabel, BorderLayout.SOUTH);
        
        itemPanel.add(centerPanel, BorderLayout.CENTER);
        itemPanel.add(Box.createHorizontalStrut(20), BorderLayout.WEST);
        itemPanel.add(Box.createHorizontalStrut(20), BorderLayout.EAST);
        
        newsPanel.add(itemPanel);
        newsPanel.add(Box.createVerticalStrut(15));
    }
}