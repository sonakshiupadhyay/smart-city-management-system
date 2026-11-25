package com.smartcity.model;

import java.sql.Timestamp;

/**
 * EnvironmentalData - Tracked and simulated environmental monitoring metrics.
 * Implements Monitorable and Reportable (Multiple Interface Implementation).
 * 
 * @author Smart City Management System
 */
public class EnvironmentalData implements Monitorable, Reportable {
    
    private int envId;
    private String location;
    private double latitude;
    private double longitude;
    private int airQualityIndex;
    private double noiseLevel;
    private double pollutionLevel;
    private double temperature;
    private double humidity;
    private double co2Level;
    private String envStatus;       // GOOD, MODERATE, UNHEALTHY, HAZARDOUS
    private String recommendation;
    private Timestamp recordedAt;

    // Constructors
    public EnvironmentalData() {}

    public EnvironmentalData(String location, int airQualityIndex, double noiseLevel, 
                             double pollutionLevel) {
        this.location = location;
        this.airQualityIndex = airQualityIndex;
        this.noiseLevel = noiseLevel;
        this.pollutionLevel = pollutionLevel;
        updateEnvironmentalStatus();
    }

    // ===== Monitorable Interface Implementation =====

    @Override
    public String getStatus() {
        return envStatus;
    }

    @Override
    public boolean hasAlerts() {
        return "UNHEALTHY".equals(envStatus) || "HAZARDOUS".equals(envStatus);
    }

    @Override
    public String getAlertMessage() {
        if ("HAZARDOUS".equals(envStatus)) {
            return "HAZARDOUS: Air quality at " + location + " is dangerous! AQI: " + airQualityIndex;
        } else if ("UNHEALTHY".equals(envStatus)) {
            return "UNHEALTHY: Poor air quality at " + location + ". AQI: " + airQualityIndex;
        }
        return null;
    }

    @Override
    public int getSeverityLevel() {
        switch (envStatus) {
            case "HAZARDOUS": return 5;
            case "UNHEALTHY": return 4;
            case "MODERATE": return 2;
            case "GOOD": return 1;
            default: return 0;
        }
    }

    // ===== Reportable Interface Implementation =====

    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Environmental Impact Report ===\n");
        report.append("Location: ").append(location).append("\n");
        report.append("Air Quality Index (AQI): ").append(airQualityIndex).append("\n");
        report.append("Noise Level: ").append(noiseLevel).append(" dB\n");
        report.append("Pollution Level: ").append(pollutionLevel).append("\n");
        report.append("Temperature: ").append(temperature).append("°C\n");
        report.append("CO2 Level: ").append(co2Level).append(" ppm\n");
        report.append("Status: ").append(envStatus).append("\n");
        report.append("Recommendation: ").append(recommendation).append("\n");
        return report.toString();
    }

    @Override
    public String getReportTitle() {
        return "Environmental Report - " + location;
    }

    @Override
    public String[] getKeyMetrics() {
        return new String[]{
            "AQI: " + airQualityIndex,
            "Noise: " + noiseLevel + " dB",
            "Pollution: " + pollutionLevel,
            "CO2: " + co2Level + " ppm"
        };
    }

    // Helper method
    private void updateEnvironmentalStatus() {
        if (airQualityIndex > 200 || pollutionLevel > 80) {
            this.envStatus = "HAZARDOUS";
        } else if (airQualityIndex > 150 || pollutionLevel > 60) {
            this.envStatus = "UNHEALTHY";
        } else if (airQualityIndex > 100 || pollutionLevel > 40) {
            this.envStatus = "MODERATE";
        } else {
            this.envStatus = "GOOD";
        }
    }

    // Getters and Setters
    public int getEnvId() { return envId; }
    public void setEnvId(int envId) { this.envId = envId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getAirQualityIndex() { return airQualityIndex; }
    public void setAirQualityIndex(int airQualityIndex) { this.airQualityIndex = airQualityIndex; }

    public double getNoiseLevel() { return noiseLevel; }
    public void setNoiseLevel(double noiseLevel) { this.noiseLevel = noiseLevel; }

    public double getPollutionLevel() { return pollutionLevel; }
    public void setPollutionLevel(double pollutionLevel) { this.pollutionLevel = pollutionLevel; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }

    public double getCo2Level() { return co2Level; }
    public void setCo2Level(double co2Level) { this.co2Level = co2Level; }

    public String getEnvStatus() { return envStatus; }
    public void setEnvStatus(String envStatus) { this.envStatus = envStatus; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    public Timestamp getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Timestamp recordedAt) { this.recordedAt = recordedAt; }
}
