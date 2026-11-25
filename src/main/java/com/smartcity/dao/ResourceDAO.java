package com.smartcity.dao;

import com.smartcity.exception.DatabaseException;
import com.smartcity.model.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ResourceDAO - Data Access Object for Resource management.
 * Extends AbstractDAO with Resource type (Generics + Inheritance).
 * 
 * @author Smart City Management System
 */
public class ResourceDAO extends AbstractDAO<Resource> {

    private static final String INSERT = 
        "INSERT INTO resources (resource_type, resource_name, location, total_capacity, current_usage, unit, status, efficiency_score, managed_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = 
        "UPDATE resources SET resource_name = ?, location = ?, total_capacity = ?, current_usage = ?, unit = ?, status = ?, efficiency_score = ? WHERE resource_id = ?";
    private static final String DELETE = "DELETE FROM resources WHERE resource_id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM resources WHERE resource_id = ?";
    private static final String FIND_ALL = "SELECT * FROM resources ORDER BY resource_type, resource_name";
    private static final String FIND_BY_TYPE = "SELECT * FROM resources WHERE resource_type = ? ORDER BY resource_name";
    private static final String COUNT = "SELECT COUNT(*) FROM resources";
    private static final String COUNT_BY_STATUS = "SELECT COUNT(*) FROM resources WHERE status = ?";

    @Override
    public int insert(Resource resource) throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, resource.getResourceType());
            stmt.setString(2, resource.getResourceName());
            stmt.setString(3, resource.getLocation());
            stmt.setDouble(4, resource.getTotalCapacity());
            stmt.setDouble(5, resource.getCurrentUsage());
            stmt.setString(6, resource.getUnit());
            stmt.setString(7, resource.getStatus());
            stmt.setDouble(8, resource.getEfficiencyScore());
            stmt.setInt(9, resource.getManagedBy());
            
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new DatabaseException("Creating resource failed.");
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting resource: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    @Override
    public boolean update(Resource resource) throws DatabaseException {
        return executeUpdate(UPDATE, resource.getResourceName(), resource.getLocation(),
            resource.getTotalCapacity(), resource.getCurrentUsage(), resource.getUnit(),
            resource.getStatus(), resource.getEfficiencyScore(), resource.getResourceId()) > 0;
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        return executeUpdate(DELETE, id) > 0;
    }

    @Override
    public Resource findById(int id) throws DatabaseException {
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
            throw new DatabaseException("Error finding resource: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    @Override
    public List<Resource> findAll() throws DatabaseException {
        List<Resource> resources = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) {
                resources.add(mapResultSet(rs));
            }
            return resources;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching resources: " + e.getMessage(), e);
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

    /**
     * Finds resources by type (ENERGY, WATER, WASTE).
     */
    public List<Resource> findByType(String type) throws DatabaseException {
        List<Resource> resources = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_BY_TYPE);
            stmt.setString(1, type);
            rs = stmt.executeQuery();
            while (rs.next()) {
                resources.add(mapResultSet(rs));
            }
            return resources;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding resources by type: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    /**
     * Counts resources by status.
     */
    public int countByStatus(String status) throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(COUNT_BY_STATUS);
            stmt.setString(1, status);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting resources: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    private Resource mapResultSet(ResultSet rs) throws SQLException {
        Resource resource = new Resource();
        resource.setResourceId(rs.getInt("resource_id"));
        resource.setResourceType(rs.getString("resource_type"));
        resource.setResourceName(rs.getString("resource_name"));
        resource.setLocation(rs.getString("location"));
        resource.setTotalCapacity(rs.getDouble("total_capacity"));
        resource.setCurrentUsage(rs.getDouble("current_usage"));
        resource.setUnit(rs.getString("unit"));
        resource.setStatus(rs.getString("status"));
        resource.setEfficiencyScore(rs.getDouble("efficiency_score"));
        resource.setManagedBy(rs.getInt("managed_by"));
        resource.setLastUpdated(rs.getTimestamp("last_updated"));
        resource.setCreatedAt(rs.getTimestamp("created_at"));
        return resource;
    }
}
