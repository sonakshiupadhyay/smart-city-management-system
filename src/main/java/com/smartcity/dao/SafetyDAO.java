package com.smartcity.dao;

import com.smartcity.exception.DatabaseException;
import com.smartcity.model.SafetyIncident;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SafetyDAO - Data Access Object for safety incidents.
 * 
 * @author Smart City Management System
 */
public class SafetyDAO extends AbstractDAO<SafetyIncident> {

    private static final String INSERT = 
        "INSERT INTO safety_incidents (incident_type, location, latitude, longitude, description, severity, status, reported_by) VALUES (?, ?, ?, ?, ?, ?, 'REPORTED', ?)";
    private static final String UPDATE = 
        "UPDATE safety_incidents SET status = ?, assigned_to = ?, resolved_at = ? WHERE incident_id = ?";
    private static final String DELETE = "DELETE FROM safety_incidents WHERE incident_id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM safety_incidents WHERE incident_id = ?";
    private static final String FIND_ALL = "SELECT * FROM safety_incidents ORDER BY reported_at DESC";
    private static final String COUNT = "SELECT COUNT(*) FROM safety_incidents";
    private static final String FIND_ACTIVE = "SELECT * FROM safety_incidents WHERE status IN ('REPORTED', 'IN_PROGRESS') ORDER BY severity DESC, reported_at DESC";
    private static final String COUNT_BY_STATUS = "SELECT COUNT(*) FROM safety_incidents WHERE status = ?";

    @Override
    public int insert(SafetyIncident incident) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, incident.getIncidentType());
            stmt.setString(2, incident.getLocation());
            stmt.setDouble(3, incident.getLatitude());
            stmt.setDouble(4, incident.getLongitude());
            stmt.setString(5, incident.getDescription());
            stmt.setString(6, incident.getSeverity());
            stmt.setInt(7, incident.getReportedBy());
            
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new DatabaseException("Creating incident failed.");
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting incident: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public boolean update(SafetyIncident incident) throws DatabaseException {
        return executeUpdate(UPDATE, incident.getStatus(), incident.getAssignedTo(),
            incident.getResolvedAt(), incident.getIncidentId()) > 0;
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        return executeUpdate(DELETE, id) > 0;
    }

    @Override
    public SafetyIncident findById(int id) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) return mapResultSet(rs);
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding incident: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public List<SafetyIncident> findAll() throws DatabaseException {
        List<SafetyIncident> incidents = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) incidents.add(mapResultSet(rs));
            return incidents;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching incidents: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public int count() throws DatabaseException {
        return executeCount(COUNT);
    }

    /**
     * Finds all active (unresolved) incidents.
     */
    public List<SafetyIncident> findActive() throws DatabaseException {
        List<SafetyIncident> incidents = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_ACTIVE);
            rs = stmt.executeQuery();
            while (rs.next()) incidents.add(mapResultSet(rs));
            return incidents;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching active incidents: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    public int countByStatus(String status) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(COUNT_BY_STATUS);
            stmt.setString(1, status);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting incidents: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    public List<SafetyIncident> findBySeverity(String severity) throws DatabaseException {
        List<SafetyIncident> list = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement("SELECT * FROM safety_incidents WHERE severity = ? ORDER BY reported_at DESC");
            stmt.setString(1, severity);
            rs = stmt.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding incidents by severity: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    public int countBySeverity(String severity) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement("SELECT COUNT(*) FROM safety_incidents WHERE severity = ?");
            stmt.setString(1, severity);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting incidents by severity: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    public boolean updateStatus(int incidentId, String status, int assignedTo) throws DatabaseException {
        Timestamp resolvedAt = "RESOLVED".equalsIgnoreCase(status) || "CLOSED".equalsIgnoreCase(status) ? 
            new Timestamp(System.currentTimeMillis()) : null;
        String sql = "UPDATE safety_incidents SET status = ?, assigned_to = ?, resolved_at = ? WHERE incident_id = ?";
        return executeUpdate(sql, status, assignedTo, resolvedAt, incidentId) > 0;
    }

    private SafetyIncident mapResultSet(ResultSet rs) throws SQLException {
        SafetyIncident incident = new SafetyIncident();
        incident.setIncidentId(rs.getInt("incident_id"));
        incident.setIncidentType(rs.getString("incident_type"));
        incident.setLocation(rs.getString("location"));
        incident.setLatitude(rs.getDouble("latitude"));
        incident.setLongitude(rs.getDouble("longitude"));
        incident.setDescription(rs.getString("description"));
        incident.setSeverity(rs.getString("severity"));
        incident.setStatus(rs.getString("status"));
        incident.setReportedBy(rs.getInt("reported_by"));
        incident.setAssignedTo(rs.getInt("assigned_to"));
        incident.setReportedAt(rs.getTimestamp("reported_at"));
        incident.setResolvedAt(rs.getTimestamp("resolved_at"));
        return incident;
    }
}
