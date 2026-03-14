package ui.Pages;

import ui.components.*;
import ui.themes.ThemeConstants;
import dao.MatchDAO;
import dao.FighterDAO;
import dao.EventDAO;
import models.Match;
import models.Fighter;
import models.Event;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class MatchDetailsPage extends JFrame {
    private NavigationBar navBar;
    private String username;
    private boolean isAdmin;
    private MatchDAO matchDAO;
    private FighterDAO fighterDAO;
    private EventDAO eventDAO;
    
    private JComboBox<String> statusFilterComboBox;
    private CustomButton refreshBtn;
    private JTable matchesTable;
    private DefaultTableModel tableModel;
    private CustomButton viewDetailsBtn;
    private CustomButton markCompletedBtn;
    
    private List<Match> currentMatches;

    public MatchDetailsPage(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
        this.matchDAO = new MatchDAO();
        this.fighterDAO = new FighterDAO();
        this.eventDAO = new EventDAO();
        
        setTitle("Match Details - UFC Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        setupLayout();
        loadMatches("all");
        addListeners();
        
        getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
        setVisible(true);
    }

    private void initComponents() {
        navBar = new NavigationBar(true, true);
        
        statusFilterComboBox = new JComboBox<>(new String[]{"All Matches", "Scheduled", "Completed", "Cancelled"});
        statusFilterComboBox.setBackground(ThemeConstants.SECONDARY_BLACK);
        statusFilterComboBox.setForeground(ThemeConstants.TEXT_WHITE);
        
        refreshBtn = new CustomButton("Refresh");
        
        String[] columnNames = {"Match ID", "Event", "Fighter 1", "Fighter 2", "Type", "Weight Class", "Rounds", "Winner", "Finish", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        matchesTable = new JTable(tableModel);
        matchesTable.setFont(ThemeConstants.BODY_FONT);
        matchesTable.setBackground(ThemeConstants.SECONDARY_BLACK);
        matchesTable.setForeground(ThemeConstants.TEXT_WHITE);
        matchesTable.setSelectionBackground(ThemeConstants.PRIMARY_RED);
        matchesTable.setSelectionForeground(ThemeConstants.TEXT_WHITE);
        matchesTable.setGridColor(ThemeConstants.TEXT_GRAY);
        matchesTable.setRowHeight(35);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(ThemeConstants.SECONDARY_BLACK);
        centerRenderer.setForeground(ThemeConstants.TEXT_WHITE);
        centerRenderer.setFont(ThemeConstants.BODY_FONT);
        
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            matchesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        viewDetailsBtn = new CustomButton("View Details");
        markCompletedBtn = new CustomButton("Mark as Completed");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(navBar, BorderLayout.NORTH);
        
        // Top Panel - Filters
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel titleLabel = new JLabel("Match Details");
        titleLabel.setFont(ThemeConstants.TITLE_FONT);
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        filterPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setForeground(ThemeConstants.TEXT_WHITE);
        filterLabel.setFont(ThemeConstants.BODY_FONT);
        filterPanel.add(filterLabel);
        filterPanel.add(statusFilterComboBox);
        filterPanel.add(refreshBtn);
        
        topPanel.add(filterPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center Panel - Table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JScrollPane scrollPane = new JScrollPane(matchesTable);
        scrollPane.getViewport().setBackground(ThemeConstants.SECONDARY_BLACK);
        scrollPane.setBorder(BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom Panel - Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        bottomPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        bottomPanel.add(viewDetailsBtn);
        if (isAdmin) {
            bottomPanel.add(markCompletedBtn);
        }
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadMatches(String filter) {
        tableModel.setRowCount(0);
        
        if ("all".equals(filter)) {
            currentMatches = matchDAO.getAllMatches();
        } else if ("scheduled".equals(filter)) {
            currentMatches = matchDAO.getScheduledMatches();
        } else if ("completed".equals(filter)) {
            currentMatches = matchDAO.getCompletedMatches();
        } else if ("cancelled".equals(filter)) {
            currentMatches = matchDAO.getCancelledMatches();
        }
        
        if (currentMatches != null) {
            for (Match match : currentMatches) {
                Fighter fighter1 = fighterDAO.getFighterById(match.getFighter1Id());
                Fighter fighter2 = fighterDAO.getFighterById(match.getFighter2Id());
                Fighter winner = (match.getWinnerId() != null) ? fighterDAO.getFighterById(match.getWinnerId()) : null;
                Event event = eventDAO.getEventById(match.getEventId());
                
                String f1Name = (fighter1 != null) ? fighter1.getFighterName() : "Unknown";
                String f2Name = (fighter2 != null) ? fighter2.getFighterName() : "Unknown";
                String winnerName = (winner != null) ? winner.getFighterName() : "-";
                String eventName = (event != null) ? event.getEventName() : "Unknown";
                
                Object[] row = {
                    match.getMatchId(),
                    eventName,
                    f1Name,
                    f2Name,
                    match.getMatchType(),
                    match.getWeightClass(),
                    match.getScheduledRounds(),
                    winnerName,
                    (match.getFinishMethod() != null) ? match.getFinishMethod() : "-",
                    match.getStatus()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void addListeners() {
        navBar.addBackListener(e -> {
            if (isAdmin) {
                new AdminDashboard(username);
            } else {
                new UserDashboard(username);
            }
            this.dispose();
        });
        
        navBar.addLogoutListener(e -> {
            new LandingPage();
            this.dispose();
        });
        
        statusFilterComboBox.addActionListener(e -> {
            String selected = (String) statusFilterComboBox.getSelectedItem();
            String filter = "all";
            if ("Scheduled".equals(selected)) filter = "scheduled";
            else if ("Completed".equals(selected)) filter = "completed";
            else if ("Cancelled".equals(selected)) filter = "cancelled";
            loadMatches(filter);
        });
        
        refreshBtn.addActionListener(e -> {
            String selected = (String) statusFilterComboBox.getSelectedItem();
            String filter = "all";
            if ("Scheduled".equals(selected)) filter = "scheduled";
            else if ("Completed".equals(selected)) filter = "completed";
            else if ("Cancelled".equals(selected)) filter = "cancelled";
            loadMatches(filter);
        });
        
        viewDetailsBtn.addActionListener(e -> viewMatchDetails());
        markCompletedBtn.addActionListener(e -> markMatchCompleted());
    }

    private void viewMatchDetails() {
        int selectedRow = matchesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a match to view details!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Match match = currentMatches.get(selectedRow);
        Fighter fighter1 = fighterDAO.getFighterById(match.getFighter1Id());
        Fighter fighter2 = fighterDAO.getFighterById(match.getFighter2Id());
        Event event = eventDAO.getEventById(match.getEventId());
        
        StringBuilder details = new StringBuilder();
        details.append("MATCH DETAILS\n");
        details.append("================================\n\n");
        details.append("Match ID: ").append(match.getMatchId()).append("\n");
        details.append("Event: ").append((event != null) ? event.getEventName() : "Unknown").append("\n");
        details.append("Event Date: ").append((event != null) ? event.getEventDate() : "N/A").append("\n");
        details.append("Venue: ").append((event != null) ? event.getVenue() : "N/A").append("\n");
        details.append("City: ").append((event != null) ? event.getCity() : "N/A").append("\n\n");
        
        details.append("FIGHTERS\n");
        details.append("--------------------------------\n");
        if (fighter1 != null) {
            details.append("Fighter 1: ").append(fighter1.getFighterName()).append(" (\"").append(fighter1.getNickname()).append("\")\n");
            details.append("  Record: ").append(fighter1.getWins()).append("-").append(fighter1.getLosses()).append("-").append(fighter1.getDraws()).append("\n");
            details.append("  Height: ").append(fighter1.getHeight()).append(" cm | Weight: ").append(fighter1.getWeight()).append(" kg\n");
            details.append("  Reach: ").append(fighter1.getReach()).append(" cm\n\n");
        }
        
        if (fighter2 != null) {
            details.append("Fighter 2: ").append(fighter2.getFighterName()).append(" (\"").append(fighter2.getNickname()).append("\")\n");
            details.append("  Record: ").append(fighter2.getWins()).append("-").append(fighter2.getLosses()).append("-").append(fighter2.getDraws()).append("\n");
            details.append("  Height: ").append(fighter2.getHeight()).append(" cm | Weight: ").append(fighter2.getWeight()).append(" kg\n");
            details.append("  Reach: ").append(fighter2.getReach()).append(" cm\n\n");
        }
        
        details.append("MATCH INFO\n");
        details.append("--------------------------------\n");
        details.append("Match Type: ").append(match.getMatchType()).append("\n");
        details.append("Weight Class: ").append(match.getWeightClass()).append("\n");
        details.append("Scheduled Rounds: ").append(match.getScheduledRounds()).append("\n");
        details.append("Status: ").append(match.getStatus()).append("\n");
        
        if ("completed".equals(match.getStatus())) {
            details.append("\nRESULT\n");
            details.append("--------------------------------\n");
            Fighter winner = (match.getWinnerId() != null) ? fighterDAO.getFighterById(match.getWinnerId()) : null;
            details.append("Winner: ").append((winner != null) ? winner.getFighterName() : "N/A").append("\n");
            details.append("Finish Method: ").append((match.getFinishMethod() != null) ? match.getFinishMethod() : "N/A").append("\n");
            details.append("Finish Round: ").append((match.getFinishRound() != null) ? match.getFinishRound() : "N/A").append("\n");
        }
        
        JTextArea detailsArea = new JTextArea(details.toString());
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        detailsArea.setBackground(ThemeConstants.SECONDARY_BLACK);
        detailsArea.setForeground(ThemeConstants.TEXT_WHITE);
        
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        JOptionPane.showMessageDialog(this,
            scrollPane,
            "Match Details",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void markMatchCompleted() {
        int selectedRow = matchesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a match!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Match match = currentMatches.get(selectedRow);
        
        if ("completed".equals(match.getStatus())) {
            JOptionPane.showMessageDialog(this,
                "This match is already completed!",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create result dialog
        JDialog resultDialog = new JDialog(this, "Enter Match Result", true);
        resultDialog.setSize(500, 400);
        resultDialog.setLocationRelativeTo(this);
        resultDialog.getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        JLabel titleLabel = new JLabel("Match Result");
        titleLabel.setFont(ThemeConstants.HEADING_FONT);
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy++;
        
        Fighter f1 = fighterDAO.getFighterById(match.getFighter1Id());
        Fighter f2 = fighterDAO.getFighterById(match.getFighter2Id());
        
        String[] winnerOptions = {f1.getFighterName(), f2.getFighterName(), "Draw"};
        JComboBox<String> winnerCombo = new JComboBox<>(winnerOptions);
        winnerCombo.setBackground(ThemeConstants.SECONDARY_BLACK);
        winnerCombo.setForeground(ThemeConstants.TEXT_WHITE);
        
        String[] finishMethods = {"KO", "TKO", "Submission", "Decision", "Draw", "No_Contest"};
        JComboBox<String> finishMethodCombo = new JComboBox<>(finishMethods);
        finishMethodCombo.setBackground(ThemeConstants.SECONDARY_BLACK);
        finishMethodCombo.setForeground(ThemeConstants.TEXT_WHITE);
        
        JSpinner finishRoundSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        finishRoundSpinner.setBackground(ThemeConstants.SECONDARY_BLACK);
        finishRoundSpinner.setForeground(ThemeConstants.TEXT_WHITE);
        
        JLabel winnerLabel = new JLabel("Winner:");
        winnerLabel.setForeground(ThemeConstants.TEXT_WHITE);
        panel.add(winnerLabel, gbc);
        gbc.gridx = 1;
        panel.add(winnerCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel finishMethodLabel = new JLabel("Finish Method:");
        finishMethodLabel.setForeground(ThemeConstants.TEXT_WHITE);
        panel.add(finishMethodLabel, gbc);
        gbc.gridx = 1;
        panel.add(finishMethodCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel finishRoundLabel = new JLabel("Finish Round:");
        finishRoundLabel.setForeground(ThemeConstants.TEXT_WHITE);
        panel.add(finishRoundLabel, gbc);
        gbc.gridx = 1;
        panel.add(finishRoundSpinner, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        
        CustomButton submitBtn = new CustomButton("Submit Result");
        submitBtn.addActionListener(e -> {
            try {
                String winner = (String) winnerCombo.getSelectedItem();
                Integer winnerId = null;
                
                if ("Draw".equals(winner)) {
                    winnerId = null;
                } else if (f1.getFighterName().equals(winner)) {
                    winnerId = f1.getFighterId();
                } else if (f2.getFighterName().equals(winner)) {
                    winnerId = f2.getFighterId();
                }
                
                String finishMethod = (String) finishMethodCombo.getSelectedItem();
                int finishRound = (int) finishRoundSpinner.getValue();
                
                if (matchDAO.updateMatchResult(match.getMatchId(), winnerId, finishMethod, finishRound)) {
                    JOptionPane.showMessageDialog(resultDialog,
                        "Match result updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    resultDialog.dispose();
                    loadMatches("all");
                } else {
                    JOptionPane.showMessageDialog(resultDialog,
                        "Failed to update match result!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(resultDialog,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(submitBtn, gbc);
        
        resultDialog.add(panel);
        resultDialog.setVisible(true);
    }
}