package com.smartcity.dao;

import com.smartcity.exception.DatabaseException;
import com.smartcity.model.ServiceRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceRequestDAO - Data Access Object for resident service requests.
 * 
 * @author Smart City Management System
 */
public class ServiceRequestDAO extends AbstractDAO<ServiceRequest> {

    private static final String INSERT = 
        "INSERT INTO service_requests (resident_id, service_type, title, description, location, priority) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = 
        "UPDATE service_requests SET status = ?, assigned_to = ?, resolution_notes = ?, resolved_at = ? WHERE request_id = ?";
    private static final String DELETE = "DELETE FROM service_requests WHERE request_id = ?";
    private static final String FIND_BY_ID = 
        "SELECT sr.*, u.full_name as resident_name FROM service_requests sr JOIN users u ON sr.resident_id = u.user_id WHERE sr.request_id = ?";
    private static final String FIND_ALL = 
        "SELECT sr.*, u.full_name as resident_name FROM service_requests sr JOIN users u ON sr.resident_id = u.user_id ORDER BY sr.created_at DESC";
    private static final String COUNT = "SELECT COUNT(*) FROM service_requests";
    private static final String FIND_BY_RESIDENT = 
        "SELECT sr.*, u.full_name as resident_name FROM service_requests sr JOIN users u ON sr.resident_id = u.user_id WHERE sr.resident_id = ? ORDER BY sr.created_at DESC";
    private static final String COUNT_BY_STATUS = "SELECT COUNT(*) FROM service_requests WHERE status = ?";
    private static final String COUNT_BY_RESIDENT_STATUS = "SELECT COUNT(*) FROM service_requests WHERE resident_id = ? AND status NOT IN ('RESOLVED', 'REJECTED')";

    @Override
    public int insert(ServiceRequest request) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, request.getResidentId());
            stmt.setString(2, request.getServiceType());
            stmt.setString(3, request.getTitle());
            stmt.setString(4, request.getDescription());
            stmt.setString(5, request.getLocation());
            stmt.setString(6, request.getPriority());
            
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new DatabaseException("Creating service request failed.");
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting service request: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public boolean update(ServiceRequest request) throws DatabaseException {
        Timestamp resolvedAt = "RESOLVED".equals(request.getStatus()) ? new Timestamp(System.currentTimeMillis()) : null;
        return executeUpdate(UPDATE, request.getStatus(), request.getAssignedTo(),
            request.getResolutionNotes(), resolvedAt, request.getRequestId()) > 0;
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        return executeUpdate(DELETE, id) > 0;
    }

    @Override
    public ServiceRequest findById(int id) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) return mapResultSet(rs);
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding service request: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public List<ServiceRequest> findAll() throws DatabaseException {
        List<ServiceRequest> requests = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) requests.add(mapResultSet(rs));
            return requests;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching service requests: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public int count() throws DatabaseException {
        return executeCount(COUNT);
    }

    public List<ServiceRequest> findByResident(int residentId) throws DatabaseException {
        List<ServiceRequest> requests = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_BY_RESIDENT);
            stmt.setInt(1, residentId);
            rs = stmt.executeQuery();
            while (rs.next()) requests.add(mapResultSet(rs));
            return requests;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding requests by resident: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    public int countPendingByResident(int residentId) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(COUNT_BY_RESIDENT_STATUS);
            stmt.setInt(1, residentId);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting pending requests: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    public List<ServiceRequest> findByResidentId(int residentId) throws DatabaseException {
        return findByResident(residentId);
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
            throw new DatabaseException("Error counting requests by status: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    private ServiceRequest mapResultSet(ResultSet rs) throws SQLException {
        ServiceRequest request = new ServiceRequest();
        request.setRequestId(rs.getInt("request_id"));
        request.setResidentId(rs.getInt("resident_id"));
        request.setServiceType(rs.getString("service_type"));
        request.setTitle(rs.getString("title"));
        request.setDescription(rs.getString("description"));
        request.setLocation(rs.getString("location"));
        request.setPriority(rs.getString("priority"));
        request.setStatus(rs.getString("status"));
        request.setAssignedTo(rs.getInt("assigned_to"));
        request.setResolutionNotes(rs.getString("resolution_notes"));
        request.setCreatedAt(rs.getTimestamp("created_at"));
        request.setResolvedAt(rs.getTimestamp("resolved_at"));
        try { request.setResidentName(rs.getString("resident_name")); } catch (SQLException e) { /* column may not exist */ }
        return request;
    }
}
