package com.smartcity.dao;

import com.smartcity.exception.DatabaseException;
import com.smartcity.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO - Data Access Object for User operations.
 * Extends AbstractDAO with User type (Generics + Inheritance).
 * Demonstrates JDBC CRUD operations with PreparedStatement.
 * 
 * Uses Factory pattern to create appropriate User subclass based on role.
 * 
 * @author Smart City Management System
 */
public class UserDAO extends AbstractDAO<User> {

    // ===== SQL Queries as Constants =====
    private static final String INSERT_USER = 
        "INSERT INTO users (username, password, email, full_name, phone, role, department, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER = 
        "UPDATE users SET email = ?, full_name = ?, phone = ?, department = ?, address = ? WHERE user_id = ?";
    private static final String DELETE_USER = 
        "DELETE FROM users WHERE user_id = ?";
    private static final String FIND_BY_ID = 
        "SELECT * FROM users WHERE user_id = ?";
    private static final String FIND_ALL = 
        "SELECT * FROM users ORDER BY created_at DESC";
    private static final String FIND_BY_USERNAME = 
        "SELECT * FROM users WHERE username = ?";
    private static final String AUTHENTICATE = 
        "SELECT * FROM users WHERE username = ? AND password = ? AND is_active = TRUE";
    private static final String COUNT_USERS = 
        "SELECT COUNT(*) FROM users";
    private static final String FIND_BY_ROLE = 
        "SELECT * FROM users WHERE role = ? ORDER BY full_name";

    @Override
    public int insert(User user) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getRole());
            stmt.setString(7, user.getDepartment());
            stmt.setString(8, user.getAddress());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating user failed, no rows affected.");
            }

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new DatabaseException("Creating user failed, no ID obtained.");
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting user: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public boolean update(User user) throws DatabaseException {
        try {
            int rows = executeUpdate(UPDATE_USER,
                user.getEmail(), user.getFullName(), user.getPhone(),
                user.getDepartment(), user.getAddress(), user.getUserId());
            return rows > 0;
        } catch (DatabaseException e) {
            throw new DatabaseException("Error updating user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        try {
            int rows = executeUpdate(DELETE_USER, id);
            return rows > 0;
        } catch (DatabaseException e) {
            throw new DatabaseException("Error deleting user: " + e.getMessage(), e);
        }
    }

    @Override
    public User findById(int id) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding user: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public List<User> findAll() throws DatabaseException {
        List<User> users = new ArrayList<>();  // Using Collections
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching users: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public int count() throws DatabaseException {
        return executeCount(COUNT_USERS);
    }

    /**
     * Finds a user by username.
     */
    public User findByUsername(String username) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_BY_USERNAME);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding user by username: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    /**
     * Authenticates a user with username and password.
     * Returns the User object if credentials are valid, null otherwise.
     */
    public User authenticate(String username, String password) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(AUTHENTICATE);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Authentication error: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    /**
     * Finds all users with a specific role.
     * Uses Collections (ArrayList) - demonstrates Collections concept.
     */
    public List<User> findByRole(String role) throws DatabaseException {
        List<User> users = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_BY_ROLE);
            stmt.setString(1, role);
            rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding users by role: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    /**
     * Checks if a username already exists in the database.
     */
    public boolean usernameExists(String username) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error checking username: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    /**
     * Checks if an email already exists in the database.
     */
    public boolean emailExists(String email) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?");
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error checking email: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    /**
     * Maps a ResultSet row to the appropriate User subclass.
     * Factory pattern - creates CityAdministrator, UrbanPlanner, or Resident
     * based on the role column (Polymorphism).
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        String role = rs.getString("role");
        User user;
        
        // Factory pattern - creating appropriate subclass based on role
        switch (role) {
            case "ADMIN":
                user = new CityAdministrator(
                    rs.getInt("user_id"), rs.getString("username"), rs.getString("password"),
                    rs.getString("email"), rs.getString("full_name"), rs.getString("phone"),
                    rs.getString("department"), rs.getString("address"),
                    rs.getBoolean("is_active"), rs.getTimestamp("created_at"), rs.getTimestamp("updated_at")
                );
                break;
            case "PLANNER":
                user = new UrbanPlanner(
                    rs.getInt("user_id"), rs.getString("username"), rs.getString("password"),
                    rs.getString("email"), rs.getString("full_name"), rs.getString("phone"),
                    rs.getString("department"), rs.getString("address"),
                    rs.getBoolean("is_active"), rs.getTimestamp("created_at"), rs.getTimestamp("updated_at")
                );
                break;
            case "RESIDENT":
            default:
                user = new Resident(
                    rs.getInt("user_id"), rs.getString("username"), rs.getString("password"),
                    rs.getString("email"), rs.getString("full_name"), rs.getString("phone"),
                    rs.getString("department"), rs.getString("address"),
                    rs.getBoolean("is_active"), rs.getTimestamp("created_at"), rs.getTimestamp("updated_at")
                );
                break;
        }
        
        return user;
    }
}
