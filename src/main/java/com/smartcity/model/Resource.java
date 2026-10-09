package com.smartcity.model;

import java.sql.Timestamp;

/**
 * Resource - Base class for city resources (Inheritance base).
 * Implements Manageable and Monitorable interfaces (Multiple Interface Implementation).
 * Demonstrates Encapsulation with private fields and public getters/setters.
 * 
 * Subclasses: EnergyResource, WaterResource, WasteResource
 * 
 * @author Smart City Management System
 */
public class Resource implements Manageable, Monitorable {
    
    private int resourceId;
    private String resourceType;
    private String resourceName;
    private String location;
    private double totalCapacity;
    private double currentUsage;
    private String unit;
    private String status;
    private double efficiencyScore;
    private int managedBy;
    private Timestamp lastUpdated;
    private Timestamp createdAt;

    // Default Constructor
    public Resource() {}

    // Parameterized Constructor
    public Resource(String resourceType, String resourceName, String location,
                    double totalCapacity, double currentUsage, String unit) {
        this.resourceType = resourceType;
        this.resourceName = resourceName;
        this.location = location;
        this.totalCapacity = totalCapacity;
        this.currentUsage = currentUsage;
        this.unit = unit;
        this.status = "OPTIMAL";
        this.efficiencyScore = 100.0;
    }

    // ===== Manageable Interface Implementation =====

    @Override
    public boolean allocate(double amount) {
        if (currentUsage + amount <= totalCapacity) {
            currentUsage += amount;
            updateStatus();
            return true;
        }
        return false;
    }

    @Override
    public boolean deallocate(double amount) {
        if (currentUsage - amount >= 0) {
            currentUsage -= amount;
            updateStatus();
            return true;
        }
        return false;
    }

    @Override
    public double getUtilization() {
        if (totalCapacity == 0) return 0;
        return (currentUsage / totalCapacity) * 100;
    }

    @Override
    public String optimize() {
        double utilization = getUtilization();
        StringBuilder report = new StringBuilder();
        report.append("Resource: ").append(resourceName).append("\n");
        report.append("Current Utilization: ").append(String.format("%.1f%%", utilization)).append("\n");
        
        if (utilization > 90) {
            report.append("Recommendation: CRITICAL - Reduce usage or increase capacity\n");
        } else if (utilization > 75) {
            report.append("Recommendation: WARNING - Monitor closely, consider scaling\n");
        } else if (utilization > 50) {
            report.append("Recommendation: OPTIMAL - Resource usage is balanced\n");
        } else {
            report.append("Recommendation: UNDERUTILIZED - Consider reallocating resources\n");
        }
        
        return report.toString();
    }

    // ===== Monitorable Interface Implementation =====

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public boolean hasAlerts() {
        return "WARNING".equals(status) || "CRITICAL".equals(status);
    }

    @Override
    public String getAlertMessage() {
        if ("CRITICAL".equals(status)) {
            return "CRITICAL: " + resourceName + " usage at " + String.format("%.1f%%", getUtilization()) + "!";
        } else if ("WARNING".equals(status)) {
            return "WARNING: " + resourceName + " usage at " + String.format("%.1f%%", getUtilization());
        }
        return null;
    }

    @Override
    public int getSeverityLevel() {
        switch (status) {
            case "CRITICAL": return 5;
            case "WARNING": return 3;
            case "OPTIMAL": return 1;
            default: return 0;
        }
    }

    // Helper method to update status based on utilization
    protected void updateStatus() {
        double utilization = getUtilization();
        if (utilization >= 85) {
            this.status = "CRITICAL";
        } else if (utilization >= 70) {
            this.status = "WARNING";
        } else {
            this.status = "OPTIMAL";
        }
    }

    // Getters and Setters (Encapsulation)
    public int getResourceId() { return resourceId; }
    public void setResourceId(int resourceId) { this.resourceId = resourceId; }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(double totalCapacity) { this.totalCapacity = totalCapacity; }

    public double getCurrentUsage() { return currentUsage; }
    public void setCurrentUsage(double currentUsage) { this.currentUsage = currentUsage; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public void setStatus(String status) { this.status = status; }

    public double getEfficiencyScore() { return efficiencyScore; }
    public void setEfficiencyScore(double efficiencyScore) { this.efficiencyScore = efficiencyScore; }

    public int getManagedBy() { return managedBy; }
    public void setManagedBy(int managedBy) { this.managedBy = managedBy; }

    public Timestamp getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceId=" + resourceId +
                ", type='" + resourceType + '\'' +
                ", name='" + resourceName + '\'' +
                ", utilization=" + String.format("%.1f%%", getUtilization()) +
                ", status='" + status + '\'' +
                '}';
    }
}
