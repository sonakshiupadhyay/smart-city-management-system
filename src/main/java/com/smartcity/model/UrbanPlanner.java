package com.smartcity.model;

import java.sql.Timestamp;

/**
 * UrbanPlanner - Extends User (Inheritance)
 * Demonstrates Polymorphism by overriding abstract methods.
 * Implements Reportable interface (Interface implementation).
 * 
 * Responsibilities:
 * - Infrastructure Planning
 * - Environmental Monitoring
 * 
 * @author Smart City Management System
 */
public class UrbanPlanner extends User implements Reportable {
    
    private String specialization;
    private int activeProjects;
    private int completedProjects;

    // Default Constructor
    public UrbanPlanner() {
        super();
        setRole("PLANNER");
    }

    // Parameterized Constructor
    public UrbanPlanner(String username, String password, String email, String fullName) {
        super(username, password, email, fullName, "PLANNER");
    }

    // Full Constructor
    public UrbanPlanner(int userId, String username, String password, String email,
                        String fullName, String phone, String department,
                        String address, boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
        super(userId, username, password, email, fullName, phone, "PLANNER", department, address, isActive, createdAt, updatedAt);
    }

    // ===== Polymorphism - Overriding abstract methods from User =====

    @Override
    public String getDashboardURL() {
        return "/planner/dashboard";
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
            "VIEW_INFRASTRUCTURE", "CREATE_PROJECT", "EDIT_PROJECT",
            "VIEW_ENVIRONMENT", "GENERATE_ENV_REPORT",
            "VIEW_RESOURCES", "VIEW_TRAFFIC",
            "VIEW_REPORTS", "GENERATE_REPORTS"
        };
    }

    @Override
    public String getWelcomeMessage() {
        return "Welcome, Planner " + getFullName() + "! You have " 
               + activeProjects + " active infrastructure projects.";
    }

    // ===== Reportable Interface Implementation =====

    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Urban Planning Report ===\n");
        report.append("Planner: ").append(getFullName()).append("\n");
        report.append("Specialization: ").append(specialization).append("\n");
        report.append("Active Projects: ").append(activeProjects).append("\n");
        report.append("Completed Projects: ").append(completedProjects).append("\n");
        report.append("Department: ").append(getDepartment()).append("\n");
        return report.toString();
    }

    @Override
    public String getReportTitle() {
        return "Urban Planner - " + getFullName() + " - Project Report";
    }

    @Override
    public String[] getKeyMetrics() {
        return new String[]{
            "Active Projects: " + activeProjects,
            "Completed Projects: " + completedProjects,
            "Specialization: " + specialization
        };
    }

    // Getters and Setters
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public int getActiveProjects() { return activeProjects; }
    public void setActiveProjects(int activeProjects) { this.activeProjects = activeProjects; }

    public int getCompletedProjects() { return completedProjects; }
    public void setCompletedProjects(int completedProjects) { this.completedProjects = completedProjects; }

    @Override
    public String toString() {
        return "UrbanPlanner{" + super.toString() + 
               ", specialization='" + specialization + "'}";
    }
}
