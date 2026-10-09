package com.smartcity.util;

import com.smartcity.exception.DatabaseException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseConnection - Singleton pattern for database connectivity.
 * Demonstrates JDBC Database Connectivity and Singleton Design Pattern.
 * 
 * Supports primary MySQL database connection with seamless in-memory fallback
 * for local testing and zero-configuration demonstration.
 * 
 * @author Smart City Management System
 */
public class DatabaseConnection {

    private static final String DEFAULT_MYSQL_URL = "jdbc:mysql://localhost:3306/smart_city_db";
    private static final String DEFAULT_H2_URL = "jdbc:h2:mem:smart_city_db;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1";
    
    private static DatabaseConnection instance;
    private Connection connection;
    private boolean isFallbackMode = false;
    private boolean schemaInitialized = false;

    private DatabaseConnection() {
        // Pre-load JDBC drivers
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {}
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ignored) {}
    }

    public static synchronized DatabaseConnection getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public synchronized Connection getConnection() throws DatabaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            // Read connection params
            String dbUrl = System.getProperty("db.url", 
                System.getenv("DB_URL") != null ? System.getenv("DB_URL") : DEFAULT_MYSQL_URL);
            String dbUser = System.getProperty("db.user", 
                System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root");
            String dbPassword = System.getProperty("db.password", 
                System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "");

            try {
                // Attempt primary MySQL connection
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                System.out.println("[DatabaseConnection] Connected to MySQL database successfully.");
                isFallbackMode = false;
            } catch (SQLException ex) {
                // Graceful fallback to H2 in-memory MySQL mode for local testing
                System.out.println("[DatabaseConnection] Primary MySQL connection unavailable (" + ex.getMessage() + ").");
                System.out.println("[DatabaseConnection] Activating in-memory local testing database...");

                connection = DriverManager.getConnection(DEFAULT_H2_URL, "sa", "");
                isFallbackMode = true;

                if (!schemaInitialized) {
                    initializeFallbackSchema(connection);
                    schemaInitialized = true;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to establish database connection: " + e.getMessage(), e);
        }
        return connection;
    }

    private void initializeFallbackSchema(Connection conn) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("schema.sql");
            if (is == null) {
                File f = new File("sql/schema.sql");
                if (f.exists()) is = new FileInputStream(f);
            }

            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder sqlBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("--") || line.isEmpty()) continue;
                    // Ignore MySQL-specific DB switching
                    if (line.toUpperCase().startsWith("CREATE DATABASE") || line.toUpperCase().startsWith("USE ")) {
                        continue;
                    }
                    sqlBuilder.append(line).append(" ");
                }
                reader.close();

                String[] statements = sqlBuilder.toString().split(";");
                Statement stmt = conn.createStatement();
                for (String sql : statements) {
                    sql = sql.trim();
                    if (!sql.isEmpty()) {
                        try {
                            stmt.execute(sql);
                        } catch (SQLException se) {
                            // Non-fatal statement skip
                        }
                    }
                }
                stmt.close();
                System.out.println("[DatabaseConnection] Local database schema and sample data loaded successfully.");
            }
        } catch (Exception e) {
            System.err.println("[DatabaseConnection] Notice during local schema initialization: " + e.getMessage());
        }
    }

    public boolean isFallbackMode() {
        return isFallbackMode;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("[DatabaseConnection] Error closing connection: " + e.getMessage());
        }
    }

    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
