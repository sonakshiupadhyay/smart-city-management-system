package com.smartcity.model;

import java.sql.Timestamp;

/**
 * Feedback - Resident feedback on city services.
 * 
 * @author Smart City Management System
 */
public class Feedback {
    
    private int feedbackId;
    private int residentId;
    private String residentName;
    private String category;     // INFRASTRUCTURE, SERVICES, SAFETY, ENVIRONMENT, TRANSPORT, GENERAL
    private String subject;
    private String message;
    private int rating;          // 1-5
    private String status;       // SUBMITTED, ACKNOWLEDGED, IN_REVIEW, ADDRESSED
    private String adminResponse;
    private Timestamp createdAt;
    private Timestamp respondedAt;

    // Constructors
    public Feedback() {}

    public Feedback(int residentId, String category, String subject, String message, int rating) {
        this.residentId = residentId;
        this.category = category;
        this.subject = subject;
        this.message = message;
        this.rating = rating;
        this.status = "SUBMITTED";
    }

    // Getters and Setters
    public int getFeedbackId() { return feedbackId; }
    public void setFeedbackId(int feedbackId) { this.feedbackId = feedbackId; }

    public int getResidentId() { return residentId; }
    public void setResidentId(int residentId) { this.residentId = residentId; }

    public String getResidentName() { return residentName; }
    public void setResidentName(String residentName) { this.residentName = residentName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAdminResponse() { return adminResponse; }
    public void setAdminResponse(String adminResponse) { this.adminResponse = adminResponse; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getRespondedAt() { return respondedAt; }
    public void setRespondedAt(Timestamp respondedAt) { this.respondedAt = respondedAt; }
}
