package ui.Pages;

import ui.components.*;
import ui.themes.ThemeConstants;
import dao.RankingDAO;
import dao.FighterDAO;
import models.Ranking;
import models.Fighter;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class RankingPage extends JFrame {
    private NavigationBar navBar;
    private String username;
    private boolean isAdmin;
    private RankingDAO rankingDAO;
    private FighterDAO fighterDAO;
    
    private JComboBox<String> weightClassComboBox;
    private JTable rankingsTable;
    private DefaultTableModel tableModel;
    private CustomButton refreshBtn;
    private CustomButton recalculateBtn;
    
    private String[] weightClasses = {
        "Heavyweight", "Light Heavyweight", "Middleweight", "Welterweight",
        "Lightweight", "Featherweight", "Bantamweight", "Flyweight"
    };

    public RankingPage(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
        this.rankingDAO = new RankingDAO();
        this.fighterDAO = new FighterDAO();
        
        setTitle("P4P Rankings - UFC Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        setupLayout();
        loadRankings(weightClasses[0]);
        addListeners();
        
        getContentPane().setBackground(ThemeConstants.BACKGROUND_BLACK);
        setVisible(true);
    }

    private void initComponents() {
        navBar = new NavigationBar(true, true);
        
        weightClassComboBox = new JComboBox<>(weightClasses);
        weightClassComboBox.setBackground(ThemeConstants.SECONDARY_BLACK);
        weightClassComboBox.setForeground(ThemeConstants.TEXT_WHITE);
        weightClassComboBox.setSelectedIndex(0);
        
        String[] columnNames = {"Rank", "Fighter Name", "Nickname", "Record", "Win Ratio %", "Points", "Last Updated"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        rankingsTable = new JTable(tableModel);
        rankingsTable.setFont(ThemeConstants.BODY_FONT);
        rankingsTable.setBackground(ThemeConstants.SECONDARY_BLACK);
        rankingsTable.setForeground(ThemeConstants.TEXT_WHITE);
        rankingsTable.setSelectionBackground(ThemeConstants.PRIMARY_RED);
        rankingsTable.setSelectionForeground(ThemeConstants.TEXT_WHITE);
        rankingsTable.setGridColor(ThemeConstants.TEXT_GRAY);
        rankingsTable.setRowHeight(40);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(ThemeConstants.SECONDARY_BLACK);
        centerRenderer.setForeground(ThemeConstants.TEXT_WHITE);
        centerRenderer.setFont(ThemeConstants.BODY_FONT);
        
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            rankingsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Color rank cells with red gradient
        DefaultTableCellRenderer rankRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                
                if (column == 0) {
                    int rank = Integer.parseInt(value.toString());
                    if (rank == 1) {
                        cell.setBackground(ThemeConstants.PRIMARY_RED);
                    } else if (rank <= 3) {
                        cell.setBackground(ThemeConstants.DARK_RED);
                    } else {
                        cell.setBackground(ThemeConstants.SECONDARY_BLACK);
                    }
                    cell.setForeground(ThemeConstants.TEXT_WHITE);
                }
                
                return cell;
            }
        };
        rankRenderer.setHorizontalAlignment(JLabel.CENTER);
        rankingsTable.getColumnModel().getColumn(0).setCellRenderer(rankRenderer);
        
        refreshBtn = new CustomButton("Refresh Rankings");
        recalculateBtn = new CustomButton("Recalculate Rankings");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(navBar, BorderLayout.NORTH);
        
        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel titleLabel = new JLabel("P4P (Pound-for-Pound) Rankings");
        titleLabel.setFont(ThemeConstants.TITLE_FONT);
        titleLabel.setForeground(ThemeConstants.PRIMARY_RED);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        filterPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        
        JLabel filterLabel = new JLabel("Weight Class:");
        filterLabel.setForeground(ThemeConstants.TEXT_WHITE);
        filterLabel.setFont(ThemeConstants.BODY_FONT);
        filterPanel.add(filterLabel);
        filterPanel.add(weightClassComboBox);
        filterPanel.add(refreshBtn);
        
        topPanel.add(filterPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JScrollPane scrollPane = new JScrollPane(rankingsTable);
        scrollPane.getViewport().setBackground(ThemeConstants.SECONDARY_BLACK);
        scrollPane.setBorder(BorderFactory.createLineBorder(ThemeConstants.PRIMARY_RED, 2));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        bottomPanel.setBackground(ThemeConstants.BACKGROUND_BLACK);
        if (isAdmin) {
            bottomPanel.add(recalculateBtn);
        }
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadRankings(String weightClass) {
        tableModel.setRowCount(0);
        
        List<Ranking> rankings = rankingDAO.getRankingsByWeightClass(weightClass);
        
        if (rankings != null) {
            for (Ranking ranking : rankings) {
                Fighter fighter = fighterDAO.getFighterById(ranking.getFighterId());
                
                if (fighter != null) {
                    String record = fighter.getWins() + "-" + fighter.getLosses() + "-" + fighter.getDraws();
                    double winRatio = (fighter.getTotalFights() > 0) ? 
                        (double) fighter.getWins() / fighter.getTotalFights() * 100 : 0.0;
                    
                    Object[] row = {
                        ranking.getRankPosition(),
                        fighter.getFighterName(),
                        fighter.getNickname(),
                        record,
                        String.format("%.2f%%", winRatio),
                        String.format("%.2f", ranking.getPoints()),
                        ranking.getLastUpdated().toString()
                    };
                    tableModel.addRow(row);
                }
            }
        }
        
        if (tableModel.getRowCount() == 0) {
            Object[] emptyRow = {"1", "No Rankings Available", "-", "-", "-", "-", "-"};
            tableModel.addRow(emptyRow);
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
        
        weightClassComboBox.addActionListener(e -> {
            String selected = (String) weightClassComboBox.getSelectedItem();
            loadRankings(selected);
        });
        
        refreshBtn.addActionListener(e -> {
            String selected = (String) weightClassComboBox.getSelectedItem();
            loadRankings(selected);
        });
        
        recalculateBtn.addActionListener(e -> recalculateRankings());
    }

    private void recalculateRankings() {
        if (!isAdmin) {
            JOptionPane.showMessageDialog(this,
                "Only admins can recalculate rankings!",
                "Permission Denied",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "This will recalculate all P4P rankings based on current fighter records.\n\nContinue?",
            "Recalculate Rankings",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                List<Fighter> fighters = fighterDAO.getAllActiveFighters();
                Map<String, Map<Integer, Ranking>> rankingsByWeightClass = new LinkedHashMap<>();
                
                // Initialize weight class maps
                for (String wc : weightClasses) {
                    rankingsByWeightClass.put(wc, new LinkedHashMap<>());
                }
                
                // Calculate points for each fighter
                if (fighters != null) {
                    for (Fighter fighter : fighters) {
                        String wc = fighter.getWeightClass();
                        
                        double points = 0.0;
                        if (fighter.getTotalFights() > 0) {
                            double winRatio = (double) fighter.getWins() / fighter.getTotalFights();
                            int totalWins = fighter.getWins();
                            points = (winRatio * 100) + (totalWins * 0.5);
                        }
                        
                        Ranking ranking = new Ranking(
                            fighter.getFighterId(),
                            wc,
                            0,
                            points
                        );
                        
                        rankingsByWeightClass.get(wc).put(fighter.getFighterId(), ranking);
                    }
                }
                
                // Assign ranks based on points
                int successCount = 0;
                for (String wc : rankingsByWeightClass.keySet()) {
                    Map<Integer, Ranking> classRankings = rankingsByWeightClass.get(wc);
                    
                    classRankings.values().stream()
                        .sorted((a, b) -> Double.compare(b.getPoints(), a.getPoints()))
                        .forEach(ranking -> {
                            if (ranking.getRankPosition() == 0) {
                                int index = 1;
                                for (Ranking r : classRankings.values()) {
                                    r.setRankPosition(index++);
                                }
                            }
                        });
                    
                    for (Ranking ranking : classRankings.values()) {
                        if (ranking.getRankPosition() > 0) {
                            if (rankingDAO.insertOrUpdateRanking(ranking)) {
                                successCount++;
                            }
                        }
                    }
                }
                
                JOptionPane.showMessageDialog(this,
                    "Rankings recalculated successfully!\nUpdated: " + successCount + " rankings",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                String selected = (String) weightClassComboBox.getSelectedItem();
                loadRankings(selected);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error recalculating rankings: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}