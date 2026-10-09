package com.smartcity.model;

/**
 * WaterResource - Extends Resource (Inheritance)
 * Demonstrates Polymorphism by overriding optimize() method.
 * Specific to water management in the smart city.
 * 
 * @author Smart City Management System
 */
public class WaterResource extends Resource {
    
    private double purityLevel;     // Percentage
    private double leakageRate;     // Percentage
    private String treatmentType;   // Primary, Secondary, Tertiary

    public WaterResource() {
        super();
        setResourceType("WATER");
    }

    public WaterResource(String resourceName, String location, double totalCapacity, 
                         double currentUsage, String treatmentType) {
        super("WATER", resourceName, location, totalCapacity, currentUsage, "Liters");
        this.treatmentType = treatmentType;
        this.purityLevel = 95.0;
        this.leakageRate = 5.0;
    }

    // Polymorphism - Override optimize() for water-specific optimization
    @Override
    public String optimize() {
        StringBuilder report = new StringBuilder(super.optimize());
        report.append("Purity Level: ").append(String.format("%.1f%%", purityLevel)).append("\n");
        report.append("Leakage Rate: ").append(String.format("%.1f%%", leakageRate)).append("\n");
        
        if (leakageRate > 10) {
            report.append("ALERT: High leakage rate detected! Immediate pipeline inspection needed.\n");
        } else if (leakageRate > 5) {
            report.append("Suggestion: Schedule pipeline maintenance to reduce water loss.\n");
        }
        
        if (purityLevel < 90) {
            report.append("WARNING: Water purity below acceptable levels. Upgrade treatment process.\n");
        }
        
        return report.toString();
    }

    // Getters and Setters
    public double getPurityLevel() { return purityLevel; }
    public void setPurityLevel(double purityLevel) { this.purityLevel = purityLevel; }

    public double getLeakageRate() { return leakageRate; }
    public void setLeakageRate(double leakageRate) { this.leakageRate = leakageRate; }

    public String getTreatmentType() { return treatmentType; }
    public void setTreatmentType(String treatmentType) { this.treatmentType = treatmentType; }
}
