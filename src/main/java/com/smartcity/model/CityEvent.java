package com.smartcity.model;

import java.sql.Timestamp;

/**
 * CityEvent - Real-time city events and alerts.
 * 
 * @author Smart City Management System
 */
public class CityEvent {
    
    private int eventId;
    private String title;
    private String description;
    private String eventType;    // TRAFFIC, WEATHER, MAINTENANCE, FESTIVAL, EMERGENCY, GENERAL
    private String location;
    private double latitude;
    private double longitude;
    private String severity;     // INFO, WARNING, ALERT, CRITICAL
    private Timestamp startTime;
    private Timestamp endTime;
    private boolean isActive;
    private int createdBy;
    private Timestamp createdAt;

    // Constructors
    public CityEvent() {}

    public CityEvent(String title, String description, String eventType, 
                     String location, String severity) {
        this.title = title;
        this.description = description;
        this.eventType = eventType;
        this.location = location;
        this.severity = severity;
        this.isActive = true;
    }

    // Getters and Setters
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
