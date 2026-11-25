package com.smartcity.dao;

import com.smartcity.exception.DatabaseException;
import com.smartcity.model.Feedback;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FeedbackDAO - Data Access Object for resident feedback.
 * 
 * @author Smart City Management System
 */
public class FeedbackDAO extends AbstractDAO<Feedback> {

    private static final String INSERT = 
        "INSERT INTO feedback (resident_id, category, subject, message, rating, status) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_RESPONSE = 
        "UPDATE feedback SET status = ?, admin_response = ?, responded_at = ? WHERE feedback_id = ?";
    private static final String DELETE = "DELETE FROM feedback WHERE feedback_id = ?";
    private static final String FIND_BY_ID = 
        "SELECT f.*, u.full_name as resident_name FROM feedback f JOIN users u ON f.resident_id = u.user_id WHERE f.feedback_id = ?";
    private static final String FIND_ALL = 
        "SELECT f.*, u.full_name as resident_name FROM feedback f JOIN users u ON f.resident_id = u.user_id ORDER BY f.created_at DESC";
    private static final String FIND_BY_RESIDENT = 
        "SELECT f.*, u.full_name as resident_name FROM feedback f JOIN users u ON f.resident_id = u.user_id WHERE f.resident_id = ? ORDER BY f.created_at DESC";
    private static final String COUNT = "SELECT COUNT(*) FROM feedback";
    private static final String AVERAGE_RATING = "SELECT COALESCE(AVG(rating), 0.0) FROM feedback";

    @Override
    public int insert(Feedback feedback) throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, feedback.getResidentId());
            stmt.setString(2, feedback.getCategory());
            stmt.setString(3, feedback.getSubject());
            stmt.setString(4, feedback.getMessage());
            stmt.setInt(5, feedback.getRating());
            stmt.setString(6, feedback.getStatus() != null ? feedback.getStatus() : "SUBMITTED");
            
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new DatabaseException("Creating feedback failed, no ID obtained.");
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting feedback: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    @Override
    public boolean update(Feedback feedback) throws DatabaseException {
        Timestamp respondedAt = feedback.getAdminResponse() != null ? new Timestamp(System.currentTimeMillis()) : null;
        return executeUpdate(UPDATE_RESPONSE, feedback.getStatus(), feedback.getAdminResponse(),
            respondedAt, feedback.getFeedbackId()) > 0;
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        return executeUpdate(DELETE, id) > 0;
    }

    @Override
    public Feedback findById(int id) throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) return mapResultSet(rs);
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding feedback by ID: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    @Override
    public List<Feedback> findAll() throws DatabaseException {
        List<Feedback> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all feedback: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    @Override
    public int count() throws DatabaseException {
        return executeCount(COUNT);
    }

    public List<Feedback> findByResidentId(int residentId) throws DatabaseException {
        List<Feedback> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_BY_RESIDENT);
            stmt.setInt(1, residentId);
            rs = stmt.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching feedback by resident: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    public double getAverageRating() throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(AVERAGE_RATING);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
            return 0.0;
        } catch (SQLException e) {
            throw new DatabaseException("Error calculating average rating: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    public boolean respondToFeedback(int feedbackId, String status, String adminResponse) throws DatabaseException {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return executeUpdate(UPDATE_RESPONSE, status, adminResponse, now, feedbackId) > 0;
    }

    private Feedback mapResultSet(ResultSet rs) throws SQLException {
        Feedback f = new Feedback();
        f.setFeedbackId(rs.getInt("feedback_id"));
        f.setResidentId(rs.getInt("resident_id"));
        try {
            f.setResidentName(rs.getString("resident_name"));
        } catch (SQLException ignored) {}
        f.setCategory(rs.getString("category"));
        f.setSubject(rs.getString("subject"));
        f.setMessage(rs.getString("message"));
        f.setRating(rs.getInt("rating"));
        f.setStatus(rs.getString("status"));
        f.setAdminResponse(rs.getString("admin_response"));
        f.setCreatedAt(rs.getTimestamp("created_at"));
        f.setRespondedAt(rs.getTimestamp("responded_at"));
        return f;
    }
}
