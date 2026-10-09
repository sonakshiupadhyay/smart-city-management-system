package com.smartcity.model;

import java.sql.Timestamp;

/**
 * IoTSensor - Represents an IoT sensor deployed in the smart city.
 * Demonstrates OOP Interface implementation (Monitorable).
 * 
 * @author Smart City Management System
 */
public class IoTSensor implements Monitorable {

    private int sensorId;
    private String sensorName;
    private String sensorType; // TRAFFIC, ENVIRONMENT, ENERGY, WATER, SAFETY
    private String location;
    private double latitude;
    private double longitude;
    private String status;     // ACTIVE, INACTIVE, MAINTENANCE
    private double lastReading;
    private Timestamp lastReadingTime;
    private Timestamp installedAt;

    public IoTSensor() {}

    public IoTSensor(String sensorName, String sensorType, String location, 
                     double latitude, double longitude, String status) {
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.lastReading = 0.0;
        this.lastReadingTime = new Timestamp(System.currentTimeMillis());
    }

    // Monitorable implementation
    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public boolean hasAlerts() {
        if ("MAINTENANCE".equalsIgnoreCase(status) || "INACTIVE".equalsIgnoreCase(status)) {
            return true;
        }
        // Reading threshold alerts depending on type
        if ("ENVIRONMENT".equalsIgnoreCase(sensorType) && lastReading > 150) {
            return true;
        }
        if ("TRAFFIC".equalsIgnoreCase(sensorType) && lastReading > 500) {
            return true;
        }
        return false;
    }

    @Override
    public String getAlertMessage() {
        if ("MAINTENANCE".equalsIgnoreCase(status)) {
            return "Sensor " + sensorName + " is under maintenance";
        }
        if ("INACTIVE".equalsIgnoreCase(status)) {
            return "Sensor " + sensorName + " is offline";
        }
        if ("ENVIRONMENT".equalsIgnoreCase(sensorType) && lastReading > 150) {
            return "High AQI reading: " + lastReading;
        }
        if ("TRAFFIC".equalsIgnoreCase(sensorType) && lastReading > 500) {
            return "High traffic congestion: " + (int)lastReading + " vehicles/hr";
        }
        return null;
    }

    @Override
    public int getSeverityLevel() {
        if ("INACTIVE".equalsIgnoreCase(status)) return 4;
        if ("MAINTENANCE".equalsIgnoreCase(status)) return 3;
        if (hasAlerts()) return 4;
        return 1;
    }

    // Getters and Setters
    public int getSensorId() { return sensorId; }
    public void setSensorId(int sensorId) { this.sensorId = sensorId; }

    public String getSensorName() { return sensorName; }
    public void setSensorName(String sensorName) { this.sensorName = sensorName; }

    public String getSensorType() { return sensorType; }
    public void setSensorType(String sensorType) { this.sensorType = sensorType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public void setStatus(String status) { this.status = status; }

    public double getLastReading() { return lastReading; }
    public void setLastReading(double lastReading) { this.lastReading = lastReading; }

    public Timestamp getLastReadingTime() { return lastReadingTime; }
    public void setLastReadingTime(Timestamp lastReadingTime) { this.lastReadingTime = lastReadingTime; }

    public Timestamp getInstalledAt() { return installedAt; }
    public void setInstalledAt(Timestamp installedAt) { this.installedAt = installedAt; }
}
