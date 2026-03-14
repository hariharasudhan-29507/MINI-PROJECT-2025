package main;

import ui.Pages.LandingPage;
import utils.DatabaseConnection;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class MainApp {
    
    public static void main(String[] args) {
        try {
            System.out.println("\n╔═══════════════════════════════════════╗");
            System.out.println("║  UFC MANAGEMENT SYSTEM - Starting     ║");
            System.out.println("╚═══════════════════════════════════════╝\n");
            
            // Initialize database
            System.out.println("Connecting to database...");
            DatabaseConnection db = DatabaseConnection.getInstance();
            
            if (db.testConnection()) {
                System.out.println("✓ Database connection successful!\n");
                db.printDatabaseInfo();
            } else {
                System.err.println("✗ Database connection failed!");
                System.err.println("Please check your database configuration.");
                System.exit(1);
            }
            
            // Add shutdown hook
            DatabaseConnection.addShutdownHook();
            
            // Launch UI on EDT
            System.out.println("Launching application...\n");
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new LandingPage();
                        System.out.println("✓ Application launched successfully!");
                    } catch (Exception e) {
                        System.err.println("✗ Error launching UI: " + e.getMessage());
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, 
                            "Error launching application:\n" + e.getMessage(),
                            "UFC Management System - Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            
        } catch (Exception e) {
            System.err.println("✗ Fatal Error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Fatal Error:\n" + e.getMessage() + "\n\nApplication will exit.",
                "UFC Management System - Fatal Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}