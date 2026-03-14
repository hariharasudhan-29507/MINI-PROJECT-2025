package ui.Pages;

import ui.components.*;
import ui.themes.ThemeConstants;
import dao.UserDAO;
import models.User;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class UserVerificationPage extends JFrame {
    private NavigationBar navBar;
    private String adminUsername;
    private UserDAO userDAO;
    private JTable pendingUsersTable;
    private DefaultTableModel tableModel;
    private CustomButton approveBtn;
    private CustomButton rejectBtn;
    private CustomButton blockBtn;
    private CustomButton unblockBtn;
    private JLabel pendingCountLabel;

    public UserVerificationPage(String username) {
        this.adminUsername = username;
        this.userDAO = new UserDAO();
        
        setTitle("User Verification - UFC Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        setupLayout();
        loadPendingUsers();
        addListeners();
        
        getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
        setVisible(true);
    }

    private void initComponents() {
        navBar = new NavigationBar(true, true);
        
        String[] columnNames = {"User ID", "Username", "Email", "Status", "Created Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        pendingUsersTable = new JTable(tableModel);
        pendingUsersTable.setFont(ThemeConstants.BODY_FONT);
        pendingUsersTable.setBackground(ThemeConstants.SECONDARY_BLACK);
        pendingUsersTable.setForeground(ThemeConstants.TEXT_WHITE);
        pendingUsersTable.setSelectionBackground(ThemeConstants.PRIMARY_RED);
        pendingUsersTable.setSelectionForeground(ThemeConstants.TEXT_WHITE);
        pendingUsersTable.setGridColor(ThemeConstants.TEXT_GRAY);
        pendingUsersTable.setRowHeight(35);
        
        // Center align table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(ThemeConstants.SECONDARY_BLACK);
        centerRenderer.setForeground(ThemeConstants.TEXT_WHITE);
        centerRenderer.setFont(ThemeConstants.BODY_FONT);
        
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            pendingUsersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        pendingCountLabel = new JLabel("Pending Users: 0");
        pendingCountLabel.setFont(ThemeConstants.SUBHEADING_FONT);
        pendingCountLabel.setForeground(ThemeConstants.PRIMARY_RED);
        
        approveBtn = new CustomButton("Approve User");
        rejectBtn = new CustomButton("Reject User");
        blockBtn = new CustomButton("Block User");
        unblockBtn = new CustomButton("Unblock User");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(navBar, BorderLayout.NORTH);
        
        // Top Panel with Title and Count
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel titleLabel = new JLabel("User Verification & Management");
        titleLabel.setFont(ThemeConstants.TITLE_FONT);
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        topPanel.add(pendingCountLabel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center Panel - Table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JScrollPane scrollPane = new JScrollPane(pendingUsersTable);
        scrollPane.getViewport().setBackground(ThemeConstants.SECONDARY_BLACK);
        scrollPane.setBorder(BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom Panel - Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        bottomPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        bottomPanel.add(approveBtn);
        bottomPanel.add(rejectBtn);
        bottomPanel.add(blockBtn);
        bottomPanel.add(unblockBtn);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadPendingUsers() {
        tableModel.setRowCount(0);
        
        List<User> pendingUsers = userDAO.getPendingUsers();
        int pendingCount = 0;
        
        if (pendingUsers != null) {
            for (User user : pendingUsers) {
                Object[] row = {
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getStatus(),
                    user.getCreatedDate().toString()
                };
                tableModel.addRow(row);
                if ("pending".equals(user.getStatus())) {
                    pendingCount++;
                }
            }
        }
        
        // Also load active and blocked users
        List<User> allUsers = userDAO.getAllNonAdminUsers();
        if (allUsers != null) {
            for (User user : allUsers) {
                if (!"pending".equals(user.getStatus())) {
                    Object[] row = {
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getStatus(),
                        user.getCreatedDate().toString()
                    };
                    tableModel.addRow(row);
                }
            }
        }
        
        pendingCountLabel.setText("Pending Users: " + pendingCount);
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
        
        approveBtn.addActionListener(e -> approveUser());
        rejectBtn.addActionListener(e -> rejectUser());
        blockBtn.addActionListener(e -> blockUser());
        unblockBtn.addActionListener(e -> unblockUser());
    }

    private void approveUser() {
        int selectedRow = pendingUsersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to approve!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 3);
        
        if ("active".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this,
                "User is already active!",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (userDAO.updateUserStatus(userId, "active")) {
            JOptionPane.showMessageDialog(this,
                "User '" + username + "' has been approved!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            loadPendingUsers();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to approve user!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rejectUser() {
        int selectedRow = pendingUsersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to reject!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to reject user '" + username + "'?",
            "Confirm Rejection",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (userDAO.deleteUser(userId)) {
                JOptionPane.showMessageDialog(this,
                    "User '" + username + "' has been rejected and deleted!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                loadPendingUsers();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to reject user!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void blockUser() {
        int selectedRow = pendingUsersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to block!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 3);
        
        if ("blocked".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this,
                "User is already blocked!",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to block user '" + username + "'?",
            "Confirm Block",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (userDAO.updateUserStatus(userId, "blocked")) {
                JOptionPane.showMessageDialog(this,
                    "User '" + username + "' has been blocked!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                loadPendingUsers();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to block user!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void unblockUser() {
        int selectedRow = pendingUsersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to unblock!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 3);
        
        if (!"blocked".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this,
                "User is not blocked!",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (userDAO.updateUserStatus(userId, "active")) {
            JOptionPane.showMessageDialog(this,
                "User '" + username + "' has been unblocked!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            loadPendingUsers();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to unblock user!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}