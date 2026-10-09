package com.smartcity.dao;

import com.smartcity.exception.DatabaseException;
import com.smartcity.model.EnvironmentalData;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EnvironmentalDAO - Data Access Object for environmental monitoring data.
 * 
 * @author Smart City Management System
 */
public class EnvironmentalDAO extends AbstractDAO<EnvironmentalData> {

    private static final String INSERT = 
        "INSERT INTO environmental_data (location, latitude, longitude, air_quality_index, noise_level, pollution_level, temperature, humidity, co2_level, status, recommendation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = 
        "UPDATE environmental_data SET air_quality_index = ?, noise_level = ?, pollution_level = ?, temperature = ?, humidity = ?, co2_level = ?, status = ?, recommendation = ? WHERE env_id = ?";
    private static final String DELETE = "DELETE FROM environmental_data WHERE env_id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM environmental_data WHERE env_id = ?";
    private static final String FIND_ALL = "SELECT * FROM environmental_data ORDER BY recorded_at DESC";
    private static final String COUNT = "SELECT COUNT(*) FROM environmental_data";
    private static final String FIND_ALERTS = "SELECT * FROM environmental_data WHERE status IN ('UNHEALTHY', 'HAZARDOUS') ORDER BY recorded_at DESC";

    @Override
    public int insert(EnvironmentalData data) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, data.getLocation());
            stmt.setDouble(2, data.getLatitude());
            stmt.setDouble(3, data.getLongitude());
            stmt.setInt(4, data.getAirQualityIndex());
            stmt.setDouble(5, data.getNoiseLevel());
            stmt.setDouble(6, data.getPollutionLevel());
            stmt.setDouble(7, data.getTemperature());
            stmt.setDouble(8, data.getHumidity());
            stmt.setDouble(9, data.getCo2Level());
            stmt.setString(10, data.getEnvStatus());
            stmt.setString(11, data.getRecommendation());
            
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new DatabaseException("Creating environmental data failed.");
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting environmental data: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public boolean update(EnvironmentalData data) throws DatabaseException {
        return executeUpdate(UPDATE, data.getAirQualityIndex(), data.getNoiseLevel(),
            data.getPollutionLevel(), data.getTemperature(), data.getHumidity(),
            data.getCo2Level(), data.getEnvStatus(), data.getRecommendation(), data.getEnvId()) > 0;
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        return executeUpdate(DELETE, id) > 0;
    }

    @Override
    public EnvironmentalData findById(int id) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) return mapResultSet(rs);
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding environmental data: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public List<EnvironmentalData> findAll() throws DatabaseException {
        List<EnvironmentalData> dataList = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) dataList.add(mapResultSet(rs));
            return dataList;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching environmental data: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    @Override
    public int count() throws DatabaseException {
        return executeCount(COUNT);
    }

    public List<EnvironmentalData> findAlerts() throws DatabaseException {
        List<EnvironmentalData> alerts = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConnection().prepareStatement(FIND_ALERTS);
            rs = stmt.executeQuery();
            while (rs.next()) alerts.add(mapResultSet(rs));
            return alerts;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching environmental alerts: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
    }

    public List<EnvironmentalData> findHighPollutionAlerts() throws DatabaseException {
        return findAlerts();
    }

    private EnvironmentalData mapResultSet(ResultSet rs) throws SQLException {
        EnvironmentalData data = new EnvironmentalData();
        data.setEnvId(rs.getInt("env_id"));
        data.setLocation(rs.getString("location"));
        data.setLatitude(rs.getDouble("latitude"));
        data.setLongitude(rs.getDouble("longitude"));
        data.setAirQualityIndex(rs.getInt("air_quality_index"));
        data.setNoiseLevel(rs.getDouble("noise_level"));
        data.setPollutionLevel(rs.getDouble("pollution_level"));
        data.setTemperature(rs.getDouble("temperature"));
        data.setHumidity(rs.getDouble("humidity"));
        data.setCo2Level(rs.getDouble("co2_level"));
        data.setEnvStatus(rs.getString("status"));
        data.setRecommendation(rs.getString("recommendation"));
        data.setRecordedAt(rs.getTimestamp("recorded_at"));
        return data;
    }
}
