package com.smartcity.dao;

import com.smartcity.exception.DatabaseException;
import com.smartcity.util.DatabaseConnection;
import java.sql.*;

/**
 * AbstractDAO - Abstract base class for all DAO implementations.
 * Demonstrates Abstract class and provides common JDBC utility methods.
 * Uses Generics for type safety.
 *
 * <p>All database operations obtain a connection from the HikariCP pool
 * and return it after use via try-with-resources, ensuring concurrent
 * servlet requests each get their own connection.</p>
 *
 * @param <T> the entity type
 * @author Smart City Management System
 */
public abstract class AbstractDAO<T> implements GenericDAO<T> {
    
    /**
     * Gets a database connection from the HikariCP connection pool.
     * <p>
     * <strong>IMPORTANT:</strong> Every connection obtained from this method
     * MUST be closed by the caller (preferably via try-with-resources) so it
     * is returned to the pool for reuse by other threads.
     * 
     * @return a pooled database Connection
     * @throws DatabaseException if connection cannot be obtained
     */
    protected Connection getConnection() throws DatabaseException {
        return DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Safely closes a ResultSet.
     * @param rs the ResultSet to close
     */
    protected void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { /* Ignored */ }
        }
    }

    /**
     * Safely closes a PreparedStatement.
     * @param stmt the PreparedStatement to close
     */
    protected void closeStatement(PreparedStatement stmt) {
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { /* Ignored */ }
        }
    }

    /**
     * Safely closes a Connection (returns it to the pool).
     * @param conn the Connection to close
     */
    protected void closeConnection(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { /* Ignored */ }
        }
    }

    /**
     * Executes a count query.
     * @param sql the SQL count query
     * @return the count result
     * @throws DatabaseException if query fails
     */
    protected int executeCount(String sql) throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Count query failed: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    /**
     * Executes an update/delete query and returns affected rows.
     * @param sql the SQL update/delete query
     * @param params the query parameters
     * @return number of affected rows
     * @throws DatabaseException if query fails
     */
    protected int executeUpdate(String sql, Object... params) throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, params);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Update query failed: " + e.getMessage(), e);
        } finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    /**
     * Sets parameters on a PreparedStatement.
     * @param stmt the PreparedStatement
     * @param params the parameters to set
     * @throws SQLException if setting parameters fails
     */
    protected void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                stmt.setNull(i + 1, Types.VARCHAR);
            } else if (params[i] instanceof String) {
                stmt.setString(i + 1, (String) params[i]);
            } else if (params[i] instanceof Integer) {
                stmt.setInt(i + 1, (Integer) params[i]);
            } else if (params[i] instanceof Double) {
                stmt.setDouble(i + 1, (Double) params[i]);
            } else if (params[i] instanceof Boolean) {
                stmt.setBoolean(i + 1, (Boolean) params[i]);
            } else if (params[i] instanceof Date) {
                stmt.setDate(i + 1, (Date) params[i]);
            } else if (params[i] instanceof Timestamp) {
                stmt.setTimestamp(i + 1, (Timestamp) params[i]);
            } else {
                stmt.setObject(i + 1, params[i]);
            }
        }
    }
}
