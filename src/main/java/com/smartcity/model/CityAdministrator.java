package com.smartcity.model;

import java.sql.Timestamp;

/**
 * CityAdministrator - Extends User (Inheritance)
 * Demonstrates Polymorphism by overriding abstract methods.
 * Implements Reportable interface (Interface implementation).
 * 
 * Responsibilities:
 * - Resource Management
 * - Traffic Monitoring
 * - Public Safety Management
 * 
 * @author Smart City Management System
 */
public class CityAdministrator extends User implements Reportable {
    
    private String jurisdiction;
    private int managedResources;
    private int activeAlerts;

    // Default Constructor
    public CityAdministrator() {
        super();
        setRole("ADMIN");
    }

    // Parameterized Constructor
    public CityAdministrator(String username, String password, String email, String fullName) {
        super(username, password, email, fullName, "ADMIN");
    }

    // Full Constructor
    public CityAdministrator(int userId, String username, String password, String email,
                             String fullName, String phone, String department,
                             String address, boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
        super(userId, username, password, email, fullName, phone, "ADMIN", department, address, isActive, createdAt, updatedAt);
    }

    // ===== Polymorphism - Overriding abstract methods from User =====

    @Override
    public String getDashboardURL() {
        return "/admin/dashboard";
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
            "VIEW_RESOURCES", "MANAGE_RESOURCES", "ALLOCATE_RESOURCES",
            "VIEW_TRAFFIC", "MANAGE_TRAFFIC", "SET_TRAFFIC_ALERTS",
            "VIEW_SAFETY", "MANAGE_SAFETY", "RESPOND_INCIDENTS",
            "VIEW_FEEDBACK", "RESPOND_FEEDBACK",
            "MANAGE_USERS", "VIEW_REPORTS", "GENERATE_REPORTS"
        };
    }

    @Override
    public String getWelcomeMessage() {
        return "Welcome, Administrator " + getFullName() + "! You have " 
               + activeAlerts + " active alerts requiring attention.";
    }

    // ===== Reportable Interface Implementation =====

    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== City Administration Report ===\n");
        report.append("Administrator: ").append(getFullName()).append("\n");
        report.append("Jurisdiction: ").append(jurisdiction).append("\n");
        report.append("Managed Resources: ").append(managedResources).append("\n");
        report.append("Active Alerts: ").append(activeAlerts).append("\n");
        report.append("Department: ").append(getDepartment()).append("\n");
        return report.toString();
    }

    @Override
    public String getReportTitle() {
        return "City Administrator - " + getFullName() + " - Activity Report";
    }

    @Override
    public String[] getKeyMetrics() {
        return new String[]{
            "Managed Resources: " + managedResources,
            "Active Alerts: " + activeAlerts,
            "Jurisdiction: " + jurisdiction
        };
    }

    // Getters and Setters
    public String getJurisdiction() { return jurisdiction; }
    public void setJurisdiction(String jurisdiction) { this.jurisdiction = jurisdiction; }

    public int getManagedResources() { return managedResources; }
    public void setManagedResources(int managedResources) { this.managedResources = managedResources; }

    public int getActiveAlerts() { return activeAlerts; }
    public void setActiveAlerts(int activeAlerts) { this.activeAlerts = activeAlerts; }

    @Override
    public String toString() {
        return "CityAdministrator{" + super.toString() + 
               ", jurisdiction='" + jurisdiction + "'}";
    }
}
