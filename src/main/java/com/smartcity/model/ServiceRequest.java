package com.smartcity.model;

import java.sql.Timestamp;

/**
 * ServiceRequest - Resident service requests.
 * 
 * @author Smart City Management System
 */
public class ServiceRequest {
    
    private int requestId;
    private int residentId;
    private String residentName;
    private String serviceType;
    private String title;
    private String description;
    private String location;
    private String priority;     // LOW, MEDIUM, HIGH, URGENT
    private String status;       // SUBMITTED, IN_REVIEW, IN_PROGRESS, RESOLVED, REJECTED
    private int assignedTo;
    private String assignedToName;
    private String resolutionNotes;
    private Timestamp createdAt;
    private Timestamp resolvedAt;

    // Constructors
    public ServiceRequest() {}

    public ServiceRequest(int residentId, String serviceType, String title, 
                          String description, String location, String priority) {
        this.residentId = residentId;
        this.serviceType = serviceType;
        this.title = title;
        this.description = description;
        this.location = location;
        this.priority = priority;
        this.status = "SUBMITTED";
    }

    // Getters and Setters
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getResidentId() { return residentId; }
    public void setResidentId(int residentId) { this.residentId = residentId; }

    public String getResidentName() { return residentName; }
    public void setResidentName(String residentName) { this.residentName = residentName; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getAssignedTo() { return assignedTo; }
    public void setAssignedTo(int assignedTo) { this.assignedTo = assignedTo; }

    public String getAssignedToName() { return assignedToName; }
    public void setAssignedToName(String assignedToName) { this.assignedToName = assignedToName; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Timestamp resolvedAt) { this.resolvedAt = resolvedAt; }
}
