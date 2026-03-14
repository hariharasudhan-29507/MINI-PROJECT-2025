package services;

import utils.DatabaseConnection;
import models.User;
import dao.UserDAO;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {
    private Connection connection;
    private UserDAO userDAO;

    public AuthService() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.userDAO = new UserDAO();
    }

    /**
     * Authenticate user and return user type if successful
     */
    public String login(String username, String password) {
        String query = "SELECT user_type, status FROM users WHERE username = ? AND password = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);  // In production, hash the password first
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String status = rs.getString("status");
                String userType = rs.getString("user_type");
                
                // Admin can always login
                if ("admin".equalsIgnoreCase(userType)) {
                    return userType;
                }
                
                // Regular users must be active
                if ("active".equalsIgnoreCase(status)) {
                    return userType;
                }
                
                // Blocked or pending users cannot login
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Register new user with pending status
     */
    public boolean register(String username, String password, String email) {
        // Check if username already exists
        if (usernameExists(username)) {
            return false;
        }
        
        String query = "INSERT INTO users (user_id, username, password, email, user_type, status) " +
                      "VALUES (users_seq.NEXTVAL, ?, ?, ?, 'user', 'pending')";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);  // In production, hash the password
            pstmt.setString(3, email);
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if username already exists
     */
    private boolean usernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Get user status
     */
    public String getUserStatus(String username) {
        String query = "SELECT status FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Get user details by username
     */
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    /**
     * Change user password
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // Verify old password
        String verifyQuery = "SELECT user_id FROM users WHERE username = ? AND password = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(verifyQuery)) {
            pstmt.setString(1, username);
            pstmt.setString(2, oldPassword);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                
                // Update password
                String updateQuery = "UPDATE users SET password = ? WHERE user_id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, newPassword);
                    updateStmt.setInt(2, userId);
                    
                    return updateStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Hash password using SHA-256 (for production use)
     * Currently not used, but available for implementation
     */
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;  // Return plain password if hashing fails
        }
    }
}