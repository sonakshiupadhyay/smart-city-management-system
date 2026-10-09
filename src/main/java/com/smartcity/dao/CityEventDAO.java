package com.smartcity.dao;

import com.smartcity.exception.DatabaseException;
import com.smartcity.model.CityEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CityEventDAO - Data Access Object for city events and real-time alerts.
 * 
 * @author Smart City Management System
 */
public class CityEventDAO extends AbstractDAO<CityEvent> {

    private static final String INSERT = 
        "INSERT INTO city_events (title, description, event_type, location, latitude, longitude, severity, start_time, end_time, is_active, created_by) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = 
        "UPDATE city_events SET title = ?, description = ?, event_type = ?, location = ?, latitude = ?, longitude = ?, " +
        "severity = ?, start_time = ?, end_time = ?, is_active = ? WHERE event_id = ?";
    private static final String DELETE = "DELETE FROM city_events WHERE event_id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM city_events WHERE event_id = ?";
    private static final String FIND_ALL = "SELECT * FROM city_events ORDER BY created_at DESC";
    private static final String FIND_ACTIVE = "SELECT * FROM city_events WHERE is_active = TRUE ORDER BY severity DESC, start_time ASC";
    private static final String COUNT = "SELECT COUNT(*) FROM city_events";
    private static final String COUNT_ACTIVE = "SELECT COUNT(*) FROM city_events WHERE is_active = TRUE";

    @Override
    public int insert(CityEvent event) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setString(3, event.getEventType());
            stmt.setString(4, event.getLocation());
            stmt.setDouble(5, event.getLatitude());
            stmt.setDouble(6, event.getLongitude());
            stmt.setString(7, event.getSeverity());
            stmt.setTimestamp(8, event.getStartTime() != null ? event.getStartTime() : new Timestamp(System.currentTimeMillis()));
            stmt.setTimestamp(9, event.getEndTime());
            stmt.setBoolean(10, event.isActive());
            if (event.getCreatedBy() > 0) {
                stmt.setInt(11, event.getCreatedBy());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }
            
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new DatabaseException("Creating city event failed, no ID obtained.");
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting city event: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public boolean update(CityEvent event) throws DatabaseException {
        return executeUpdate(UPDATE, event.getTitle(), event.getDescription(), event.getEventType(),
            event.getLocation(), event.getLatitude(), event.getLongitude(), event.getSeverity(),
            event.getStartTime(), event.getEndTime(), event.isActive(), event.getEventId()) > 0;
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        return executeUpdate(DELETE, id) > 0;
    }

    @Override
    public CityEvent findById(int id) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) return mapResultSet(rs);
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding city event by ID: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public List<CityEvent> findAll() throws DatabaseException {
        List<CityEvent> list = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all city events: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    public List<CityEvent> findActiveEvents() throws DatabaseException {
        List<CityEvent> list = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_ACTIVE);
            rs = stmt.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching active city events: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public int count() throws DatabaseException {
        return executeCount(COUNT);
    }

    public int countActive() throws DatabaseException {
        return executeCount(COUNT_ACTIVE);
    }

    private CityEvent mapResultSet(ResultSet rs) throws SQLException {
        CityEvent e = new CityEvent();
        e.setEventId(rs.getInt("event_id"));
        e.setTitle(rs.getString("title"));
        e.setDescription(rs.getString("description"));
        e.setEventType(rs.getString("event_type"));
        e.setLocation(rs.getString("location"));
        e.setLatitude(rs.getDouble("latitude"));
        e.setLongitude(rs.getDouble("longitude"));
        e.setSeverity(rs.getString("severity"));
        e.setStartTime(rs.getTimestamp("start_time"));
        e.setEndTime(rs.getTimestamp("end_time"));
        e.setActive(rs.getBoolean("is_active"));
        e.setCreatedBy(rs.getInt("created_by"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        return e;
    }
}
