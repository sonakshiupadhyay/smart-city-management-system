package com.smartcity.model;

/**
 * Interface: Reportable
 * Demonstrates Interface concept in Java OOP.
 * Any entity that can generate reports implements this interface.
 * 
 * @author Smart City Management System
 */
public interface Reportable {
    
    /**
     * Generates a summary report of the entity.
     * @return formatted report string
     */
    String generateReport();
    
    /**
     * Gets the report title.
     * @return title of the report
     */
    String getReportTitle();
    
    /**
     * Gets key metrics for the report.
     * @return array of key metric values
     */
    String[] getKeyMetrics();
}
