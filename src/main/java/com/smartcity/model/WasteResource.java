package com.smartcity.model;

/**
 * WasteResource - Extends Resource (Inheritance)
 * Demonstrates Polymorphism by overriding optimize() method.
 * Specific to waste management in the smart city.
 * 
 * @author Smart City Management System
 */
public class WasteResource extends Resource {
    
    private double recyclingRate;    // Percentage
    private String wasteCategory;   // Recyclable, Organic, Hazardous, General
    private double processingRate;  // Tons per day

    public WasteResource() {
        super();
        setResourceType("WASTE");
    }

    public WasteResource(String resourceName, String location, double totalCapacity, 
                         double currentUsage, String wasteCategory) {
        super("WASTE", resourceName, location, totalCapacity, currentUsage, "Tons");
        this.wasteCategory = wasteCategory;
        this.recyclingRate = 40.0;
    }

    // Polymorphism - Override optimize() for waste-specific optimization
    @Override
    public String optimize() {
        StringBuilder report = new StringBuilder(super.optimize());
        report.append("Waste Category: ").append(wasteCategory).append("\n");
        report.append("Recycling Rate: ").append(String.format("%.1f%%", recyclingRate)).append("\n");
        
        if (recyclingRate < 30) {
            report.append("CRITICAL: Low recycling rate! Implement better waste segregation.\n");
        } else if (recyclingRate < 50) {
            report.append("Suggestion: Improve community awareness for better recycling.\n");
        } else {
            report.append("Good: Recycling rate is above target. Continue current practices.\n");
        }
        
        if (getUtilization() > 80) {
            report.append("WARNING: Approaching capacity. Schedule additional waste collection.\n");
        }
        
        return report.toString();
    }

    // Getters and Setters
    public double getRecyclingRate() { return recyclingRate; }
    public void setRecyclingRate(double recyclingRate) { this.recyclingRate = recyclingRate; }

    public String getWasteCategory() { return wasteCategory; }
    public void setWasteCategory(String wasteCategory) { this.wasteCategory = wasteCategory; }

    public double getProcessingRate() { return processingRate; }
    public void setProcessingRate(double processingRate) { this.processingRate = processingRate; }
}
