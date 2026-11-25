package com.smartcity.util;

import com.smartcity.exception.DatabaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseConnection - Thread-safe connection pool for database connectivity.
 * Uses HikariCP connection pooling to support concurrent servlet requests reliably.
 *
 * Supports primary MySQL database connection with seamless in-memory H2 fallback
 * for local testing and zero-configuration demonstration.
 *
 * Each call to {@link #getConnection()} returns a separate pooled connection
 * that MUST be closed by the caller (returned to the pool) after use.
 *
 * @author Smart City Management System
 */
public class DatabaseConnection {

    private static final String DEFAULT_MYSQL_URL = "jdbc:mysql://localhost:3306/smart_city_db";
    private static final String DEFAULT_H2_URL = "jdbc:h2:mem:smart_city_db;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1";

    private static volatile DatabaseConnection instance;
    private HikariDataSource dataSource;
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

        initializePool();
    }

    /**
     * Initializes the HikariCP connection pool.
     * Attempts MySQL first; falls back to H2 in-memory if MySQL is unavailable.
     */
    private void initializePool() {
        String dbUrl = System.getProperty("db.url",
            System.getenv("DB_URL") != null ? System.getenv("DB_URL") : DEFAULT_MYSQL_URL);
        String dbUser = System.getProperty("db.user",
            System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root");
        String dbPassword = System.getProperty("db.password",
            System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "");

        // Try MySQL first
        try {
            HikariConfig config = createPoolConfig(dbUrl, dbUser, dbPassword);
            config.setConnectionTestQuery("SELECT 1");
            dataSource = new HikariDataSource(config);
            // Validate the pool can actually connect
            Connection test = dataSource.getConnection();
            test.close();
            isFallbackMode = false;
            System.out.println("[DatabaseConnection] HikariCP pool initialized with MySQL database successfully.");
            return;
        } catch (Exception ex) {
            System.out.println("[DatabaseConnection] Primary MySQL connection unavailable (" + ex.getMessage() + ").");
            System.out.println("[DatabaseConnection] Activating in-memory local testing database with connection pool...");
            if (dataSource != null) {
                try { dataSource.close(); } catch (Exception ignored) {}
            }
        }

        // Fallback to H2 in-memory with pooling
        try {
            HikariConfig h2Config = createPoolConfig(DEFAULT_H2_URL, "sa", "");
            dataSource = new HikariDataSource(h2Config);
            isFallbackMode = true;

            if (!schemaInitialized) {
                Connection conn = dataSource.getConnection();
                try {
                    initializeFallbackSchema(conn);
                    schemaInitialized = true;
                } finally {
                    conn.close();
                }
            }
            System.out.println("[DatabaseConnection] HikariCP pool initialized with H2 in-memory database.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize any database connection pool: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a HikariCP pool configuration with sensible defaults for a servlet container.
     */
    private HikariConfig createPoolConfig(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        // Pool sizing: allow concurrent servlet threads to each hold a connection
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);

        // Connection lifecycle settings
        config.setConnectionTimeout(10_000);   // 10s max wait for a connection from the pool
        config.setIdleTimeout(300_000);         // 5 min idle before connection is retired
        config.setMaxLifetime(600_000);         // 10 min max lifetime per connection
        config.setLeakDetectionThreshold(30_000); // Log warning if connection not returned within 30s

        config.setPoolName("SmartCityPool");
        config.setAutoCommit(true);

        return config;
    }

    /**
     * Returns the singleton instance. Thread-safe via double-checked locking.
     */
    public static DatabaseConnection getInstance() throws DatabaseException {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Returns a pooled database connection.
     * <p>
     * <strong>IMPORTANT:</strong> The caller MUST close the returned connection
     * when finished (this returns it to the pool, it does not destroy it).
     * Use try-with-resources for automatic cleanup.
     *
     * @return a pooled Connection from HikariCP
     * @throws DatabaseException if no connection can be obtained from the pool
     */
    public Connection getConnection() throws DatabaseException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to obtain connection from pool: " + e.getMessage(), e);
        }
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

    /**
     * Shuts down the pool only if an instance was already initialized, without triggering lazy setup.
     */
    public static void shutdownIfInitialized() {
        synchronized (DatabaseConnection.class) {
            if (instance != null) {
                instance.shutdown();
            }
        }
    }

    /**
     * Shuts down the connection pool. Call during application shutdown (e.g., ServletContextListener).
     */
    public void shutdown() {
        synchronized (DatabaseConnection.class) {
            if (dataSource != null && !dataSource.isClosed()) {
                dataSource.close();
                System.out.println("[DatabaseConnection] Connection pool shut down.");
            }
            instance = null;
        }
    }

    /**
     * @deprecated Use {@link #shutdown()} instead. Individual connections are
     *             managed by the pool and should be closed by the caller after use.
     */
    @Deprecated
    public void closeConnection() {
        shutdown();
    }

    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            boolean valid = conn != null && !conn.isClosed();
            conn.close(); // return to pool
            return valid;
        } catch (Exception e) {
            return false;
        }
    }
}
