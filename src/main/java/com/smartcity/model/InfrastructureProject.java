package com.smartcity.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * InfrastructureProject - Model for urban planning projects.
 * Implements Reportable for generating project reports.
 * 
 * @author Smart City Management System
 */
public class InfrastructureProject implements Reportable {
    
    private int projectId;
    private String projectName;
    private String description;
    private String category;     // TRANSPORTATION, HOUSING, UTILITIES, PUBLIC_SPACES, TECHNOLOGY
    private String location;
    private int plannedBy;
    private String plannerName;
    private String status;       // PROPOSED, APPROVED, IN_PROGRESS, COMPLETED, ON_HOLD
    private double budget;
    private double spentAmount;
    private Date startDate;
    private Date endDate;
    private double impactScore;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public InfrastructureProject() {}

    public InfrastructureProject(String projectName, String description, String category,
                                  String location, double budget) {
        this.projectName = projectName;
        this.description = description;
        this.category = category;
        this.location = location;
        this.budget = budget;
        this.status = "PROPOSED";
        this.spentAmount = 0;
    }

    // ===== Reportable Interface Implementation =====

    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Infrastructure Project Report ===\n");
        report.append("Project: ").append(projectName).append("\n");
        report.append("Category: ").append(category).append("\n");
        report.append("Status: ").append(status).append("\n");
        report.append("Budget: ₹").append(String.format("%,.2f", budget)).append("\n");
        report.append("Spent: ₹").append(String.format("%,.2f", spentAmount)).append("\n");
        report.append("Budget Utilization: ").append(String.format("%.1f%%", getBudgetUtilization())).append("\n");
        report.append("Impact Score: ").append(impactScore).append("/10\n");
        return report.toString();
    }

    @Override
    public String getReportTitle() {
        return "Project Report: " + projectName;
    }

    @Override
    public String[] getKeyMetrics() {
        return new String[]{
            "Budget: ₹" + String.format("%,.0f", budget),
            "Spent: ₹" + String.format("%,.0f", spentAmount),
            "Impact Score: " + impactScore + "/10",
            "Status: " + status
        };
    }

    // Helper method
    public double getBudgetUtilization() {
        if (budget == 0) return 0;
        return (spentAmount / budget) * 100;
    }

    // Getters and Setters
    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getPlannedBy() { return plannedBy; }
    public void setPlannedBy(int plannedBy) { this.plannedBy = plannedBy; }

    public String getPlannerName() { return plannerName; }
    public void setPlannerName(String plannerName) { this.plannerName = plannerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public double getSpentAmount() { return spentAmount; }
    public void setSpentAmount(double spentAmount) { this.spentAmount = spentAmount; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public double getImpactScore() { return impactScore; }
    public void setImpactScore(double impactScore) { this.impactScore = impactScore; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
