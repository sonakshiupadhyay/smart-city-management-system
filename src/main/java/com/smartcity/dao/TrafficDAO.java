package com.smartcity.dao;

import com.smartcity.exception.DatabaseException;
import com.smartcity.model.TrafficData;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TrafficDAO - Data Access Object for traffic monitoring data.
 * 
 * @author Smart City Management System
 */
public class TrafficDAO extends AbstractDAO<TrafficData> {

    private static final String INSERT = 
        "INSERT INTO traffic_data (location, latitude, longitude, congestion_level, average_speed, vehicle_count, alert_type, suggestion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = 
        "UPDATE traffic_data SET congestion_level = ?, average_speed = ?, vehicle_count = ?, alert_type = ?, suggestion = ? WHERE traffic_id = ?";
    private static final String DELETE = "DELETE FROM traffic_data WHERE traffic_id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM traffic_data WHERE traffic_id = ?";
    private static final String FIND_ALL = "SELECT * FROM traffic_data ORDER BY recorded_at DESC";
    private static final String COUNT = "SELECT COUNT(*) FROM traffic_data";
    private static final String FIND_ALERTS = "SELECT * FROM traffic_data WHERE congestion_level IN ('HIGH', 'SEVERE') OR alert_type IS NOT NULL ORDER BY recorded_at DESC";
    private static final String COUNT_BY_CONGESTION = "SELECT congestion_level, COUNT(*) as cnt FROM traffic_data GROUP BY congestion_level";

    @Override
    public int insert(TrafficData data) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, data.getLocation());
            stmt.setDouble(2, data.getLatitude());
            stmt.setDouble(3, data.getLongitude());
            stmt.setString(4, data.getCongestionLevel());
            stmt.setDouble(5, data.getAverageSpeed());
            stmt.setInt(6, data.getVehicleCount());
            stmt.setString(7, data.getAlertType());
            stmt.setString(8, data.getSuggestion());
            
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new DatabaseException("Creating traffic data failed.");
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting traffic data: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public boolean update(TrafficData data) throws DatabaseException {
        return executeUpdate(UPDATE, data.getCongestionLevel(), data.getAverageSpeed(),
            data.getVehicleCount(), data.getAlertType(), data.getSuggestion(), data.getTrafficId()) > 0;
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        return executeUpdate(DELETE, id) > 0;
    }

    @Override
    public TrafficData findById(int id) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) return mapResultSet(rs);
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding traffic data: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public List<TrafficData> findAll() throws DatabaseException {
        List<TrafficData> dataList = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) dataList.add(mapResultSet(rs));
            return dataList;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching traffic data: " + e.getMessage(), e);
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
     * Finds all traffic alerts (HIGH/SEVERE congestion or active alerts).
     */
    public List<TrafficData> findAlerts() throws DatabaseException {
        List<TrafficData> alerts = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_ALERTS);
            rs = stmt.executeQuery();
            while (rs.next()) alerts.add(mapResultSet(rs));
            return alerts;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching traffic alerts: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    /**
     * Finds congested traffic zones (HIGH or SEVERE).
     */
    public List<TrafficData> findCongestedAreas() throws DatabaseException {
        return findAlerts();
    }

    /**
     * Updates congestion metrics for a traffic monitoring point.
     */
    public boolean updateCongestion(int trafficId, String congestionLevel, double averageSpeed, int vehicleCount) throws DatabaseException {
        String sql = "UPDATE traffic_data SET congestion_level = ?, average_speed = ?, vehicle_count = ? WHERE traffic_id = ?";
        return executeUpdate(sql, congestionLevel, averageSpeed, vehicleCount, trafficId) > 0;
    }

    private TrafficData mapResultSet(ResultSet rs) throws SQLException {
        TrafficData data = new TrafficData();
        data.setTrafficId(rs.getInt("traffic_id"));
        data.setLocation(rs.getString("location"));
        data.setLatitude(rs.getDouble("latitude"));
        data.setLongitude(rs.getDouble("longitude"));
        data.setCongestionLevel(rs.getString("congestion_level"));
        data.setAverageSpeed(rs.getDouble("average_speed"));
        data.setVehicleCount(rs.getInt("vehicle_count"));
        data.setAlertType(rs.getString("alert_type"));
        data.setSuggestion(rs.getString("suggestion"));
        data.setRecordedAt(rs.getTimestamp("recorded_at"));
        return data;
    }
}
