package utils;

import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final Object lock = new Object();
    
    // Oracle Configuration
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USERNAME = "system";  // Change to your Oracle username
    private static final String PASSWORD = "hari";  // Change to your Oracle password
    private static final int CONNECTION_TIMEOUT = 30000;  // 30 seconds
    private static final int MAX_RETRIES = 3;

    /**
     * Private constructor for singleton pattern
     */
    private DatabaseConnection() {
        try {
            initializeConnection();
        } catch (SQLException e) {
            System.err.println("Failed to initialize database connection");
            e.printStackTrace();
        }
    }

    /**
     * Get singleton instance of DatabaseConnection
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Initialize database connection with retry logic
     */
    private void initializeConnection() throws SQLException {
        int attempts = 0;
        SQLException lastException = null;

        while (attempts < MAX_RETRIES) {
            try {
                // Load Oracle JDBC Driver
                Class.forName(DRIVER);
                System.out.println("✓ Oracle JDBC Driver loaded successfully");

                // Create connection properties
                Properties props = new Properties();
                props.setProperty("user", USERNAME);
                props.setProperty("password", PASSWORD);
                props.setProperty("defaultRowFetch", "100");
                props.setProperty("stringDefaultValue", "");

                // Establish connection
                this.connection = DriverManager.getConnection(URL, props);
                
                // Verify connection
                if (connection != null && !connection.isClosed()) {
                    System.out.println("✓ Connected to Oracle Database successfully!");
                    System.out.println("✓ Database URL: " + URL);
                    System.out.println("✓ Connection established at: " + new java.util.Date());
                    return;
                }
            } catch (ClassNotFoundException e) {
                System.err.println("✗ Oracle JDBC Driver not found!");
                System.err.println("  Please add ojdbc8.jar to your project classpath");
                throw new SQLException("Oracle JDBC Driver not found", e);
            } catch (SQLException e) {
                lastException = e;
                attempts++;
                System.err.println("✗ Attempt " + attempts + " to connect failed: " + e.getMessage());
                
                if (attempts < MAX_RETRIES) {
                    try {
                        Thread.sleep(2000);  // Wait 2 seconds before retry
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        // All retries exhausted
        if (lastException != null) {
            System.err.println("✗ Failed to connect after " + MAX_RETRIES + " attempts");
            System.err.println("✗ Error: " + lastException.getMessage());
            System.err.println("\n✗ Connection Failed!");
            System.err.println("Troubleshooting:");
            System.err.println("1. Verify Oracle Database is running");
            System.err.println("2. Check Oracle Listener: lsnrctl status");
            System.err.println("3. Verify USERNAME and PASSWORD in DatabaseConnection.java");
            System.err.println("4. Verify Oracle URL: " + URL);
            System.err.println("5. Ensure ojdbc8.jar is in classpath");
            throw lastException;
        }
    }

    /**
     * Get active database connection
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Connection lost. Attempting to reconnect...");
                initializeConnection();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Test if connection is alive
     */
    public boolean testConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                // Execute a simple query to verify connection
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT 1 FROM DUAL")) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed successfully");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connection = null;
            instance = null;
        }
    }

    /**
     * Get connection status
     */
    public String getConnectionStatus() {
        try {
            if (connection != null && !connection.isClosed()) {
                DatabaseMetaData metaData = connection.getMetaData();
                return "✓ Connected to " + metaData.getDatabaseProductName() + 
                       " " + metaData.getDatabaseProductVersion();
            }
        } catch (SQLException e) {
            return "✗ Connection error: " + e.getMessage();
        }
        return "✗ No connection available";
    }

    /**
     * Execute update query (INSERT, UPDATE, DELETE)
     */
    public int executeUpdate(String query, Object... params) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            setParameters(pstmt, params);
            return pstmt.executeUpdate();
        }
    }

    /**
     * Execute query and get result set
     */
    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(query);
        setParameters(pstmt, params);
        return pstmt.executeQuery();
    }

    /**
     * Set parameters for prepared statement
     */
    private void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param == null) {
                pstmt.setNull(i + 1, Types.NULL);
            } else if (param instanceof String) {
                pstmt.setString(i + 1, (String) param);
            } else if (param instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) param);
            } else if (param instanceof Double) {
                pstmt.setDouble(i + 1, (Double) param);
            } else if (param instanceof Float) {
                pstmt.setFloat(i + 1, (Float) param);
            } else if (param instanceof Long) {
                pstmt.setLong(i + 1, (Long) param);
            } else if (param instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) param);
            } else if (param instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) param);
            } else if (param instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) param);
            } else {
                pstmt.setObject(i + 1, param);
            }
        }
    }

    /**
     * Commit transaction
     */
    public void commit() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit();
            System.out.println("✓ Transaction committed");
        }
    }

    /**
     * Rollback transaction
     */
    public void rollback() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
            System.out.println("✓ Transaction rolled back");
        }
    }

    /**
     * Set auto-commit mode
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.setAutoCommit(autoCommit);
        }
    }

    /**
     * Get database metadata
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection.getMetaData();
        }
        return null;
    }

    /**
     * Execute batch update
     */
    public int[] executeBatch(String query, Object[]... paramsBatch) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (Object[] params : paramsBatch) {
                setParameters(pstmt, params);
                pstmt.addBatch();
            }
            return pstmt.executeBatch();
        }
    }

    /**
     * Print database information
     */
    public void printDatabaseInfo() {
        try {
            if (testConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                System.out.println("\n========== DATABASE INFO ==========");
                System.out.println("Database: " + metaData.getDatabaseProductName());
                System.out.println("Version: " + metaData.getDatabaseProductVersion());
                System.out.println("Driver: " + metaData.getDriverName());
                System.out.println("Driver Version: " + metaData.getDriverVersion());
                System.out.println("URL: " + metaData.getURL());
                System.out.println("Username: " + metaData.getUserName());
                System.out.println("====================================\n");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving database info: " + e.getMessage());
        }
    }

    /**
     * Shutdown hook to close connection on application exit
     */
    public static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseConnection db = getInstance();
            db.closeConnection();
            System.out.println("Database connection closed on application exit");
        }));
    }
}