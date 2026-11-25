package com.smartcity.model;

import java.sql.Timestamp;

/**
 * SafetyIncident - Represents public safety incidents.
 * Implements Monitorable for status and alert tracking.
 * 
 * @author Smart City Management System
 */
public class SafetyIncident implements Monitorable {
    
    private int incidentId;
    private String incidentType;    // SECURITY, EMERGENCY, FIRE, ACCIDENT, OTHER
    private String location;
    private double latitude;
    private double longitude;
    private String description;
    private String severity;        // LOW, MEDIUM, HIGH, CRITICAL
    private String status;          // REPORTED, IN_PROGRESS, RESOLVED, CLOSED
    private int reportedBy;
    private int assignedTo;
    private Timestamp reportedAt;
    private Timestamp resolvedAt;

    // Constructors
    public SafetyIncident() {}

    public SafetyIncident(String incidentType, String location, String description, String severity) {
        this.incidentType = incidentType;
        this.location = location;
        this.description = description;
        this.severity = severity;
        this.status = "REPORTED";
    }

    // ===== Monitorable Interface Implementation =====

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public boolean hasAlerts() {
        return "REPORTED".equals(status) || "IN_PROGRESS".equals(status);
    }

    @Override
    public String getAlertMessage() {
        if ("REPORTED".equals(status)) {
            return "NEW " + incidentType + " INCIDENT at " + location + " - Severity: " + severity;
        } else if ("IN_PROGRESS".equals(status)) {
            return incidentType + " INCIDENT at " + location + " - Response in progress";
        }
        return null;
    }

    @Override
    public int getSeverityLevel() {
        switch (severity) {
            case "CRITICAL": return 5;
            case "HIGH": return 4;
            case "MEDIUM": return 3;
            case "LOW": return 1;
            default: return 0;
        }
    }

    // Getters and Setters
    public int getIncidentId() { return incidentId; }
    public void setIncidentId(int incidentId) { this.incidentId = incidentId; }

    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public void setStatus(String status) { this.status = status; }

    public int getReportedBy() { return reportedBy; }
    public void setReportedBy(int reportedBy) { this.reportedBy = reportedBy; }

    public int getAssignedTo() { return assignedTo; }
    public void setAssignedTo(int assignedTo) { this.assignedTo = assignedTo; }

    public Timestamp getReportedAt() { return reportedAt; }
    public void setReportedAt(Timestamp reportedAt) { this.reportedAt = reportedAt; }

    public Timestamp getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Timestamp resolvedAt) { this.resolvedAt = resolvedAt; }
}
