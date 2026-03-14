package ui.Pages;

import ui.components.*;
import ui.themes.ThemeConstants;
import dao.MatchDAO;
import dao.FighterDAO;
import dao.EventDAO;
import dao.UserDAO;
import models.Match;
import models.Fighter;
import models.Event;
import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class StartMatchPage extends JFrame {
    private NavigationBar navBar;
    private String adminUsername;
    private FighterDAO fighterDAO;
    private MatchDAO matchDAO;
    private EventDAO eventDAO;
    private UserDAO userDAO;
    
    private JComboBox<String> eventComboBox;
    private JComboBox<Fighter> fighter1ComboBox;
    private JComboBox<Fighter> fighter2ComboBox;
    private JComboBox<String> matchTypeComboBox;
    private JComboBox<String> weightClassComboBox;
    private JSpinner roundsSpinner;
    private CustomButton startMatchBtn;
    private CustomButton cancelMatchBtn;
    private CustomButton postponeMatchBtn;
    
    private JPanel matchListPanel;
    private DefaultListModel<String> matchListModel;
    private JList<String> matchList;
    
    private int currentAdminId;

    public StartMatchPage(String username) {
        this.adminUsername = username;
        this.fighterDAO = new FighterDAO();
        this.matchDAO = new MatchDAO();
        this.eventDAO = new EventDAO();
        this.userDAO = new UserDAO();
        this.currentAdminId = userDAO.getUserIdByUsername(username);
        
        setTitle("Start Match - UFC Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        setupLayout();
        loadData();
        addListeners();
        
        getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
        setVisible(true);
    }

    private void initComponents() {
        navBar = new NavigationBar(true, true);
        
        // Event ComboBox
        eventComboBox = new JComboBox<>();
        eventComboBox.setBackground(ThemeConstants.SECONDARY_BLACK);
        eventComboBox.setForeground(ThemeConstants.TEXT_WHITE);
        
        // Fighter 1 ComboBox
        fighter1ComboBox = new JComboBox<>();
        fighter1ComboBox.setBackground(ThemeConstants.SECONDARY_BLACK);
        fighter1ComboBox.setForeground(ThemeConstants.TEXT_WHITE);
        
        // Fighter 2 ComboBox
        fighter2ComboBox = new JComboBox<>();
        fighter2ComboBox.setBackground(ThemeConstants.SECONDARY_BLACK);
        fighter2ComboBox.setForeground(ThemeConstants.TEXT_WHITE);
        
        // Match Type ComboBox
        matchTypeComboBox = new JComboBox<>(new String[]{"main_card", "preliminary", "early_preliminary"});
        matchTypeComboBox.setBackground(ThemeConstants.SECONDARY_BLACK);
        matchTypeComboBox.setForeground(ThemeConstants.TEXT_WHITE);
        matchTypeComboBox.setSelectedIndex(0);
        
        // Weight Class ComboBox
        weightClassComboBox = new JComboBox<>(new String[]{
            "Heavyweight", "Light Heavyweight", "Middleweight", "Welterweight", 
            "Lightweight", "Featherweight", "Bantamweight", "Flyweight"
        });
        weightClassComboBox.setBackground(ThemeConstants.SECONDARY_BLACK);
        weightClassComboBox.setForeground(ThemeConstants.TEXT_WHITE);
        weightClassComboBox.setSelectedIndex(0);
        
        // Rounds Spinner
        roundsSpinner = new JSpinner(new SpinnerNumberModel(3, 3, 5, 1));
        roundsSpinner.setBackground(ThemeConstants.SECONDARY_BLACK);
        roundsSpinner.setForeground(ThemeConstants.TEXT_WHITE);
        
        // Buttons
        startMatchBtn = new CustomButton("Start Match");
        cancelMatchBtn = new CustomButton("Cancel Match");
        postponeMatchBtn = new CustomButton("Postpone Match");
        
        // Match List
        matchListModel = new DefaultListModel<>();
        matchList = new JList<>(matchListModel);
        matchList.setBackground(ThemeConstants.SECONDARY_BLACK);
        matchList.setForeground(ThemeConstants.TEXT_WHITE);
        matchList.setFont(ThemeConstants.BODY_FONT);
        matchList.setSelectionBackground(ThemeConstants.PRIMARY_RED);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(navBar, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left Panel - Create Match
        JPanel createMatchPanel = createCreateMatchPanel();
        
        // Right Panel - Manage Matches
        JPanel manageMatchesPanel = createManageMatchesPanel();
        
        mainPanel.add(createMatchPanel);
        mainPanel.add(manageMatchesPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createCreateMatchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        // Title
        JLabel titleLabel = new JLabel("Create New Match");
        titleLabel.setFont(ThemeConstants.HEADING_FONT);
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy++;
        
        // Event Selection
        JLabel eventLabel = new JLabel("Select Event:");
        eventLabel.setForeground(ThemeConstants.TEXT_WHITE);
        eventLabel.setFont(ThemeConstants.BODY_FONT);
        panel.add(eventLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(eventComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        
        // Fighter 1 Selection
        JLabel fighter1Label = new JLabel("Fighter 1:");
        fighter1Label.setForeground(ThemeConstants.TEXT_WHITE);
        fighter1Label.setFont(ThemeConstants.BODY_FONT);
        panel.add(fighter1Label, gbc);
        
        gbc.gridx = 1;
        panel.add(fighter1ComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        
        // Fighter 2 Selection
        JLabel fighter2Label = new JLabel("Fighter 2:");
        fighter2Label.setForeground(ThemeConstants.TEXT_WHITE);
        fighter2Label.setFont(ThemeConstants.BODY_FONT);
        panel.add(fighter2Label, gbc);
        
        gbc.gridx = 1;
        panel.add(fighter2ComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        
        // Match Type
        JLabel matchTypeLabel = new JLabel("Match Type:");
        matchTypeLabel.setForeground(ThemeConstants.TEXT_WHITE);
        matchTypeLabel.setFont(ThemeConstants.BODY_FONT);
        panel.add(matchTypeLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(matchTypeComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        
        // Weight Class
        JLabel weightClassLabel = new JLabel("Weight Class:");
        weightClassLabel.setForeground(ThemeConstants.TEXT_WHITE);
        weightClassLabel.setFont(ThemeConstants.BODY_FONT);
        panel.add(weightClassLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(weightClassComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        
        // Scheduled Rounds
        JLabel roundsLabel = new JLabel("Scheduled Rounds:");
        roundsLabel.setForeground(ThemeConstants.TEXT_WHITE);
        roundsLabel.setFont(ThemeConstants.BODY_FONT);
        panel.add(roundsLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(roundsSpinner, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(Box.createVerticalStrut(20), gbc);
        
        gbc.gridy++;
        panel.add(startMatchBtn, gbc);
        
        return panel;
    }

    private JPanel createManageMatchesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel titleLabel = new JLabel("Scheduled Matches");
        titleLabel.setFont(ThemeConstants.HEADING_FONT);
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Match List
        JScrollPane scrollPane = new JScrollPane(matchList);
        scrollPane.getViewport().setBackground(ThemeConstants.SECONDARY_BLACK);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        buttonPanel.add(cancelMatchBtn);
        buttonPanel.add(postponeMatchBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private void loadData() {
        // Load Events
        List<Event> events = eventDAO.getUpcomingEvents();
        eventComboBox.removeAllItems();
        if (events != null) {
            for (Event event : events) {
                eventComboBox.addItem(event.getEventName() + " (ID: " + event.getEventId() + ")");
            }
        }
        
        // Load Fighters
        List<Fighter> fighters = fighterDAO.getAllActiveFighters();
        fighter1ComboBox.removeAllItems();
        fighter2ComboBox.removeAllItems();
        if (fighters != null) {
            for (Fighter fighter : fighters) {
                fighter1ComboBox.addItem(fighter);
                fighter2ComboBox.addItem(fighter);
            }
        }
        
        // Load Scheduled Matches
        loadScheduledMatches();
    }

    private void loadScheduledMatches() {
        matchListModel.clear();
        List<Match> matches = matchDAO.getScheduledMatches();
        if (matches != null) {
            for (Match match : matches) {
                Fighter f1 = fighterDAO.getFighterById(match.getFighter1Id());
                Fighter f2 = fighterDAO.getFighterById(match.getFighter2Id());
                String displayText = (f1 != null && f2 != null) ? 
                    f1.getFighterName() + " vs " + f2.getFighterName() + " (ID: " + match.getMatchId() + ")" :
                    "Match ID: " + match.getMatchId();
                matchListModel.addElement(displayText);
            }
        }
    }

    private void addListeners() {
        navBar.addBackListener(e -> {
            new AdminDashboard(adminUsername);
            this.dispose();
        });
        
        navBar.addLogoutListener(e -> {
            new LandingPage();
            this.dispose();
        });
        
        startMatchBtn.addActionListener(e -> handleStartMatch());
        cancelMatchBtn.addActionListener(e -> handleCancelMatch());
        postponeMatchBtn.addActionListener(e -> handlePostponeMatch());
    }

    private void handleStartMatch() {
        try {
            if (fighter1ComboBox.getSelectedIndex() == -1 || fighter2ComboBox.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Please select both fighters!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Fighter fighter1 = (Fighter) fighter1ComboBox.getSelectedItem();
            Fighter fighter2 = (Fighter) fighter2ComboBox.getSelectedItem();
            
            if (fighter1.getFighterId() == fighter2.getFighterId()) {
                JOptionPane.showMessageDialog(this, 
                    "Cannot create match with the same fighter!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String eventText = (String) eventComboBox.getSelectedItem();
            int eventId = Integer.parseInt(eventText.replaceAll("[^0-9]", ""));
            
            Match match = new Match();
            match.setEventId(eventId);
            match.setFighter1Id(fighter1.getFighterId());
            match.setFighter2Id(fighter2.getFighterId());
            match.setWeightClass((String) weightClassComboBox.getSelectedItem());
            match.setMatchType((String) matchTypeComboBox.getSelectedItem());
            match.setScheduledRounds((int) roundsSpinner.getValue());
            match.setStatus("scheduled");
            match.setCreatedBy(currentAdminId);
            match.setMatchDate(new Timestamp(System.currentTimeMillis()));
            
            if (matchDAO.insertMatch(match)) {
                JOptionPane.showMessageDialog(this, 
                    "Match started successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadScheduledMatches();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to start match!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancelMatch() {
        int selectedIndex = matchList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a match to cancel!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel this match?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            List<Match> matches = matchDAO.getScheduledMatches();
            Match selectedMatch = matches.get(selectedIndex);
            
            if (matchDAO.updateMatchStatus(selectedMatch.getMatchId(), "cancelled")) {
                JOptionPane.showMessageDialog(this, 
                    "Match cancelled successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadScheduledMatches();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to cancel match!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handlePostponeMatch() {
        int selectedIndex = matchList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a match to postpone!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String newDateStr = JOptionPane.showInputDialog(this,
            "Enter new match date (yyyy-MM-dd HH:mm:ss):",
            "Postpone Match",
            JOptionPane.PLAIN_MESSAGE);
        
        if (newDateStr != null && !newDateStr.trim().isEmpty()) {
            try {
                java.sql.Timestamp newDate = java.sql.Timestamp.valueOf(newDateStr);
                List<Match> matches = matchDAO.getScheduledMatches();
                Match selectedMatch = matches.get(selectedIndex);
                
                if (matchDAO.updateMatchDate(selectedMatch.getMatchId(), newDate)) {
                    JOptionPane.showMessageDialog(this, 
                        "Match postponed successfully to " + newDateStr + "!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loadScheduledMatches();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to postpone match!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid date format! Use: yyyy-MM-dd HH:mm:ss", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetForm() {
        fighter1ComboBox.setSelectedIndex(-1);
        fighter2ComboBox.setSelectedIndex(-1);
        roundsSpinner.setValue(3);
    }
}