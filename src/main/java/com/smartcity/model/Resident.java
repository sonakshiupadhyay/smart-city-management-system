package com.smartcity.model;

import java.sql.Timestamp;

/**
 * Resident - Extends User (Inheritance)
 * Demonstrates Polymorphism by overriding abstract methods.
 * 
 * Responsibilities:
 * - Service Access
 * - Feedback Submission
 * - Real-time Information
 * 
 * @author Smart City Management System
 */
public class Resident extends User {
    
    private int pendingRequests;
    private int totalFeedback;
    private String ward;

    // Default Constructor
    public Resident() {
        super();
        setRole("RESIDENT");
    }

    // Parameterized Constructor
    public Resident(String username, String password, String email, String fullName) {
        super(username, password, email, fullName, "RESIDENT");
    }

    // Full Constructor
    public Resident(int userId, String username, String password, String email,
                    String fullName, String phone, String department,
                    String address, boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
        super(userId, username, password, email, fullName, phone, "RESIDENT", department, address, isActive, createdAt, updatedAt);
    }

    // ===== Polymorphism - Overriding abstract methods from User =====

    @Override
    public String getDashboardURL() {
        return "/resident/dashboard";
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
            "VIEW_SERVICES", "CREATE_SERVICE_REQUEST",
            "SUBMIT_FEEDBACK", "VIEW_FEEDBACK_STATUS",
            "VIEW_REALTIME_INFO", "VIEW_EVENTS",
            "VIEW_PROFILE", "EDIT_PROFILE"
        };
    }

    @Override
    public String getWelcomeMessage() {
        return "Welcome, " + getFullName() + "! You have " 
               + pendingRequests + " pending service requests.";
    }

    // Getters and Setters
    public int getPendingRequests() { return pendingRequests; }
    public void setPendingRequests(int pendingRequests) { this.pendingRequests = pendingRequests; }

    public int getTotalFeedback() { return totalFeedback; }
    public void setTotalFeedback(int totalFeedback) { this.totalFeedback = totalFeedback; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    @Override
    public String toString() {
        return "Resident{" + super.toString() + 
               ", ward='" + ward + "', pendingRequests=" + pendingRequests + "}";
    }
}
