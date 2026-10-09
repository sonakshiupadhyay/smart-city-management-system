package com.smartcity.model;

import java.sql.Timestamp;

/**
 * TrafficData - Implements Monitorable interface.
 * Represents real-time traffic monitoring data from IoT sensors.
 * 
 * @author Smart City Management System
 */
public class TrafficData implements Monitorable {
    
    private int trafficId;
    private String location;
    private double latitude;
    private double longitude;
    private String congestionLevel;  // LOW, MODERATE, HIGH, SEVERE
    private double averageSpeed;
    private int vehicleCount;
    private String alertType;
    private String suggestion;
    private Timestamp recordedAt;

    // Constructors
    public TrafficData() {}

    public TrafficData(String location, String congestionLevel, double averageSpeed, int vehicleCount) {
        this.location = location;
        this.congestionLevel = congestionLevel;
        this.averageSpeed = averageSpeed;
        this.vehicleCount = vehicleCount;
    }

    // ===== Monitorable Interface Implementation =====

    @Override
    public String getStatus() {
        return congestionLevel;
    }

    @Override
    public boolean hasAlerts() {
        return "HIGH".equals(congestionLevel) || "SEVERE".equals(congestionLevel) 
               || alertType != null;
    }

    @Override
    public String getAlertMessage() {
        if ("SEVERE".equals(congestionLevel)) {
            return "SEVERE TRAFFIC: " + location + " - Speed: " + averageSpeed + " km/h, Vehicles: " + vehicleCount;
        } else if ("HIGH".equals(congestionLevel)) {
            return "HIGH TRAFFIC: " + location + " - Speed: " + averageSpeed + " km/h";
        } else if (alertType != null) {
            return alertType + " at " + location;
        }
        return null;
    }

    @Override
    public int getSeverityLevel() {
        switch (congestionLevel) {
            case "SEVERE": return 5;
            case "HIGH": return 4;
            case "MODERATE": return 2;
            case "LOW": return 1;
            default: return 0;
        }
    }

    // Getters and Setters
    public int getTrafficId() { return trafficId; }
    public void setTrafficId(int trafficId) { this.trafficId = trafficId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getCongestionLevel() { return congestionLevel; }
    public void setCongestionLevel(String congestionLevel) { this.congestionLevel = congestionLevel; }

    public double getAverageSpeed() { return averageSpeed; }
    public void setAverageSpeed(double averageSpeed) { this.averageSpeed = averageSpeed; }

    public int getVehicleCount() { return vehicleCount; }
    public void setVehicleCount(int vehicleCount) { this.vehicleCount = vehicleCount; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }

    public Timestamp getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Timestamp recordedAt) { this.recordedAt = recordedAt; }

    @Override
    public String toString() {
        return "TrafficData{location='" + location + "', congestion='" + congestionLevel + 
               "', speed=" + averageSpeed + ", vehicles=" + vehicleCount + "}";
    }
}
