package com.smartcity.model;

/**
 * EnergyResource - Extends Resource (Inheritance)
 * Demonstrates Polymorphism by overriding optimize() method.
 * Specific to energy management in the smart city.
 * 
 * @author Smart City Management System
 */
public class EnergyResource extends Resource {
    
    private String energySource; // Solar, Wind, Thermal, Hydro
    private double peakOutput;
    private double carbonFootprint;

    public EnergyResource() {
        super();
        setResourceType("ENERGY");
    }

    public EnergyResource(String resourceName, String location, double totalCapacity, 
                          double currentUsage, String energySource) {
        super("ENERGY", resourceName, location, totalCapacity, currentUsage, "kWh");
        this.energySource = energySource;
    }

    // Polymorphism - Override optimize() for energy-specific optimization
    @Override
    public String optimize() {
        StringBuilder report = new StringBuilder(super.optimize());
        report.append("Energy Source: ").append(energySource).append("\n");
        report.append("Carbon Footprint: ").append(String.format("%.2f", carbonFootprint)).append(" tons CO2\n");
        
        if ("Solar".equals(energySource) || "Wind".equals(energySource)) {
            report.append("Suggestion: Increase renewable capacity during peak hours\n");
        } else {
            report.append("Suggestion: Consider transitioning to renewable energy sources\n");
        }
        
        return report.toString();
    }

    // Getters and Setters
    public String getEnergySource() { return energySource; }
    public void setEnergySource(String energySource) { this.energySource = energySource; }

    public double getPeakOutput() { return peakOutput; }
    public void setPeakOutput(double peakOutput) { this.peakOutput = peakOutput; }

    public double getCarbonFootprint() { return carbonFootprint; }
    public void setCarbonFootprint(double carbonFootprint) { this.carbonFootprint = carbonFootprint; }
}
