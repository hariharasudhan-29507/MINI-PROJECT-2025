package ui.Pages;

import ui.components.*;
import ui.themes.ThemeConstants;
import dao.FighterDAO;
import dao.UserDAO;
import models.Fighter;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ManagePlayersPage extends JFrame {
    private NavigationBar navBar;
    private String adminUsername;
    private FighterDAO fighterDAO;
    private UserDAO userDAO;
    private JTable fightersTable;
    private DefaultTableModel tableModel;
    private CustomButton addBtn;
    private CustomButton editBtn;
    private CustomButton deleteBtn;
    private CustomButton retireBtn;
    private CustomButton refreshBtn;
    private int currentAdminId;

    public ManagePlayersPage(String username) {
        this.adminUsername = username;
        this.fighterDAO = new FighterDAO();
        this.userDAO = new UserDAO();
        this.currentAdminId = userDAO.getUserIdByUsername(username);
        
        setTitle("Manage Fighters - UFC Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        setupLayout();
        loadFighters();
        addListeners();
        
        getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
        setVisible(true);
    }

    private void initComponents() {
        navBar = new NavigationBar(true, true);
        
        String[] columnNames = {"ID", "Name", "Nickname", "Weight Class", "Country", "Age", "Height", "Weight", "Reach", "Record", "Win%", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        fightersTable = new JTable(tableModel);
        fightersTable.setFont(ThemeConstants.BODY_FONT);
        fightersTable.setBackground(ThemeConstants.SECONDARY_BLACK);
        fightersTable.setForeground(ThemeConstants.TEXT_WHITE);
        fightersTable.setSelectionBackground(ThemeConstants.PRIMARY_RED);
        fightersTable.setSelectionForeground(ThemeConstants.TEXT_WHITE);
        fightersTable.setGridColor(ThemeConstants.TEXT_GRAY);
        fightersTable.setRowHeight(35);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(ThemeConstants.SECONDARY_BLACK);
        centerRenderer.setForeground(ThemeConstants.TEXT_WHITE);
        centerRenderer.setFont(ThemeConstants.BODY_FONT);
        
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            fightersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        addBtn = new CustomButton("Add Fighter");
        editBtn = new CustomButton("Edit Fighter");
        deleteBtn = new CustomButton("Delete Fighter");
        retireBtn = new CustomButton("Retire Fighter");
        refreshBtn = new CustomButton("Refresh");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(navBar, BorderLayout.NORTH);
        
        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel titleLabel = new JLabel("Manage Fighters");
        titleLabel.setFont(ThemeConstants.TITLE_FONT);
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        refreshPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        refreshPanel.add(refreshBtn);
        topPanel.add(refreshPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JScrollPane scrollPane = new JScrollPane(fightersTable);
        scrollPane.getViewport().setBackground(ThemeConstants.SECONDARY_BLACK);
        scrollPane.setBorder(BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        bottomPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        bottomPanel.add(addBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(retireBtn);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadFighters() {
        tableModel.setRowCount(0);
        List<Fighter> fighters = fighterDAO.getAllFighters();
        
        if (fighters != null) {
            for (Fighter fighter : fighters) {
                Object[] row = {
                    fighter.getFighterId(),
                    fighter.getFighterName(),
                    fighter.getNickname(),
                    fighter.getWeightClass(),
                    fighter.getCountry(),
                    fighter.getAge(),
                    fighter.getHeight() + " cm",
                    fighter.getWeight() + " kg",
                    fighter.getReach() + " cm",
                    fighter.getWins() + "-" + fighter.getLosses() + "-" + fighter.getDraws(),
                    String.format("%.2f%%", fighter.getWinRatio()),
                    fighter.getStatus()
                };
                tableModel.addRow(row);
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
        
        addBtn.addActionListener(e -> showAddFighterDialog());
        editBtn.addActionListener(e -> showEditFighterDialog());
        deleteBtn.addActionListener(e -> deleteFighter());
        retireBtn.addActionListener(e -> retireFighter());
        refreshBtn.addActionListener(e -> loadFighters());
    }

    private void showAddFighterDialog() {
        JDialog dialog = new JDialog(this, "Add New Fighter", true);
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        CustomTextField[] fields = new CustomTextField[8];
        String[] labels = {"Fighter Name", "Nickname", "Weight Class", "Country", "Age", "Height (cm)", "Weight (kg)", "Reach (cm)"};
        
        JLabel titleLabel = new JLabel("Add New Fighter");
        titleLabel.setFont(ThemeConstants.HEADING_FONT);
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new CustomTextField(labels[i], 20);
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            
            JLabel label = new JLabel(labels[i] + ":");
            label.setForeground(ThemeConstants.TEXT_WHITE);
            label.setFont(ThemeConstants.BODY_FONT);
            panel.add(label, gbc);
            
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }
        
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        
        CustomButton saveBtn = new CustomButton("Save Fighter");
        saveBtn.addActionListener(e -> {
            try {
                Fighter fighter = new Fighter(
                    fields[0].getText(),
                    fields[1].getText(),
                    fields[2].getText(),
                    fields[3].getText(),
                    Integer.parseInt(fields[4].getText()),
                    Double.parseDouble(fields[5].getText()),
                    Double.parseDouble(fields[6].getText()),
                    Double.parseDouble(fields[7].getText())
                );
                
                fighter.setCreatedBy(currentAdminId);
                
                if (fighterDAO.insertFighter(fighter)) {
                    JOptionPane.showMessageDialog(dialog, "Fighter added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadFighters();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add fighter!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for Age, Height, Weight, and Reach!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(saveBtn, gbc);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getViewport().setBackground(ThemeConstants.BACKGROUND_BLACK);
        scrollPane.setBorder(null);
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    private void showEditFighterDialog() {
        int selectedRow = fightersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a fighter to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int fighterId = (int) tableModel.getValueAt(selectedRow, 0);
        Fighter fighter = fighterDAO.getFighterById(fighterId);
        
        if (fighter == null) {
            JOptionPane.showMessageDialog(this, "Fighter not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "Edit Fighter", true);
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        CustomTextField[] fields = new CustomTextField[8];
        String[] labels = {"Fighter Name", "Nickname", "Weight Class", "Country", "Age", "Height (cm)", "Weight (kg)", "Reach (cm)"};
        Object[] values = {fighter.getFighterName(), fighter.getNickname(), fighter.getWeightClass(), 
                          fighter.getCountry(), fighter.getAge(), fighter.getHeight(), 
                          fighter.getWeight(), fighter.getReach()};
        
        JLabel titleLabel = new JLabel("Edit Fighter");
        titleLabel.setFont(ThemeConstants.HEADING_FONT);
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new CustomTextField(labels[i], 20);
            fields[i].setText(values[i].toString());
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            
            JLabel label = new JLabel(labels[i] + ":");
            label.setForeground(ThemeConstants.TEXT_WHITE);
            label.setFont(ThemeConstants.BODY_FONT);
            panel.add(label, gbc);
            
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }
        
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        
        CustomButton updateBtn = new CustomButton("Update Fighter");
        updateBtn.addActionListener(e -> {
            try {
                fighter.setFighterName(fields[0].getText());
                fighter.setNickname(fields[1].getText());
                fighter.setWeightClass(fields[2].getText());
                fighter.setCountry(fields[3].getText());
                fighter.setAge(Integer.parseInt(fields[4].getText()));
                fighter.setHeight(Double.parseDouble(fields[5].getText()));
                fighter.setWeight(Double.parseDouble(fields[6].getText()));
                fighter.setReach(Double.parseDouble(fields[7].getText()));
                
                if (fighterDAO.updateFighter(fighter)) {
                    JOptionPane.showMessageDialog(dialog, "Fighter updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadFighters();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update fighter!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(updateBtn, gbc);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getViewport().setBackground(ThemeConstants.BACKGROUND_BLACK);
        scrollPane.setBorder(null);
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    private void deleteFighter() {
        int selectedRow = fightersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a fighter to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int fighterId = (int) tableModel.getValueAt(selectedRow, 0);
        String fighterName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete " + fighterName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (fighterDAO.deleteFighter(fighterId)) {
                JOptionPane.showMessageDialog(this, "Fighter deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadFighters();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete fighter!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void retireFighter() {
        int selectedRow = fightersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a fighter to retire!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int fighterId = (int) tableModel.getValueAt(selectedRow, 0);
        String fighterName = (String) tableModel.getValueAt(selectedRow, 1);
        String status = (String) tableModel.getValueAt(selectedRow, 11);
        
        if ("retired".equals(status)) {
            JOptionPane.showMessageDialog(this, "Fighter is already retired!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to retire " + fighterName + "?",
            "Confirm Retirement",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (fighterDAO.retireFighter(fighterId)) {
                JOptionPane.showMessageDialog(this, "Fighter retired successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadFighters();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to retire fighter!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}