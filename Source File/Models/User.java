package models;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String userType;  // 'admin' or 'user'
    private String status;    // 'active', 'blocked', 'pending'
    private Timestamp createdDate;

    // Constructors
    public User() {}

    public User(int userId, String username, String email, String userType, String status) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.status = status;
    }

    public User(String username, String password, String email, String userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.status = "pending";
    }

    // Getters and Setters
    public int getUserId() { 
        return userId; 
    }
    
    public void setUserId(int userId) { 
        this.userId = userId; 
    }

    public String getUsername() { 
        return username; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }

    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getUserType() { 
        return userType; 
    }
    
    public void setUserType(String userType) { 
        this.userType = userType; 
    }

    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }

    public Timestamp getCreatedDate() { 
        return createdDate; 
    }
    
    public void setCreatedDate(Timestamp createdDate) { 
        this.createdDate = createdDate; 
    }

    // Utility Methods
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(userType);
    }

    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }

    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }

    public boolean isBlocked() {
        return "blocked".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                ", status='" + status + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(userId);
    }
}