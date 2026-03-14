
package ui.Pages;

import ui.components.*;
import ui.themes.ThemeConstants;
import dao.FighterDAO;
import models.Fighter;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class SearchPlayerPage extends JFrame {
    private NavigationBar navBar;
    private String username;
    private boolean isAdmin;
    private FighterDAO fighterDAO;
    private CustomTextField searchField;
    private CustomButton searchBtn;
    private CustomButton showAllBtn;
    private JComboBox<String> weightClassFilter;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private List<Fighter> currentResults;

    public SearchPlayerPage(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
        this.fighterDAO = new FighterDAO();
        
        setTitle("Search Players - UFC Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        setupLayout();
        loadAllFighters();
        addListeners();
        
        getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
        setVisible(true);
    }

    private void initComponents() {
        navBar = new NavigationBar(true, true);
        
        searchField = new CustomTextField("Search by name or nickname...", 30);
        searchBtn = new CustomButton("Search");
        showAllBtn = new CustomButton("Show All");
        
        weightClassFilter = new JComboBox<>(new String[]{
            "All Weight Classes",
            "Heavyweight", "Light Heavyweight", "Middleweight", "Welterweight",
            "Lightweight", "Featherweight", "Bantamweight", "Flyweight"
        });
        weightClassFilter.setBackground(ThemeConstants.SECONDARY_BLACK);
        weightClassFilter.setForeground(ThemeConstants.TEXT_WHITE);
        
        String[] columnNames = {"ID", "Name", "Nickname", "Weight Class", "Country", "Age", "Height", "Weight", "Reach", "Record", "Win%", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resultsTable = new JTable(tableModel);
        resultsTable.setFont(ThemeConstants.BODY_FONT);
        resultsTable.setBackground(ThemeConstants.SECONDARY_BLACK);
        resultsTable.setForeground(ThemeConstants.TEXT_WHITE);
        resultsTable.setSelectionBackground(ThemeConstants.PRIMARY_RED);
        resultsTable.setSelectionForeground(ThemeConstants.TEXT_WHITE);
        resultsTable.setGridColor(ThemeConstants.TEXT_GRAY);
        resultsTable.setRowHeight(35);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(ThemeConstants.SECONDARY_BLACK);
        centerRenderer.setForeground(ThemeConstants.TEXT_WHITE);
        centerRenderer.setFont(ThemeConstants.BODY_FONT);
        
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            resultsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(navBar, BorderLayout.NORTH);
        
        // Top Panel - Search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel titleLabel = new JLabel("Search Fighters");
        titleLabel.setFont(ThemeConstants.TITLE_FONT);
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        searchPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        JLabel filterLabel = new JLabel("Weight Class:");
        filterLabel.setForeground(ThemeConstants.TEXT_WHITE);
        filterLabel.setFont(ThemeConstants.BODY_FONT);
        searchPanel.add(filterLabel);
        searchPanel.add(weightClassFilter);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(showAllBtn);
        
        topPanel.add(searchPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center Panel - Table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.getViewport().setBackground(ThemeConstants.SECONDARY_BLACK);
        scrollPane.setBorder(BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Status Bar
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        JLabel statusLabel = new JLabel("Showing: 0 fighters");
        statusLabel.setForeground(ThemeConstants.TEXT_GRAY);
        statusLabel.setFont(ThemeConstants.BODY_FONT);
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void loadAllFighters() {
        tableModel.setRowCount(0);
        currentResults = fighterDAO.getAllFighters();
        displayFighters(currentResults);
    }

    private void displayFighters(List<Fighter> fighters) {
        tableModel.setRowCount(0);
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
        
        searchBtn.addActionListener(e -> performSearch());
        showAllBtn.addActionListener(e -> {
            searchField.setText("");
            weightClassFilter.setSelectedIndex(0);
            loadAllFighters();
        });
        
        weightClassFilter.addActionListener(e -> filterByWeightClass());
        searchField.addActionListener(e -> performSearch());
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadAllFighters();
            return;
        }
        
        List<Fighter> results = fighterDAO.searchFighters(searchTerm);
        displayFighters(results);
    }

    private void filterByWeightClass() {
        String selected = (String) weightClassFilter.getSelectedItem();
        
        if ("All Weight Classes".equals(selected)) {
            loadAllFighters();
        } else {
            List<Fighter> results = fighterDAO.getFightersByWeightClass(selected);
            displayFighters(results);
        }
    }
}