package com.smartcity.model;

/**
 * Interface: Monitorable
 * Demonstrates Interface concept in Java OOP.
 * Any entity whose operational status and alerts can be monitored implements this interface.
 * 
 * @author Smart City Management System
 */
public interface Monitorable {
    
    /**
     * Gets the current status of the monitored entity.
     * @return status string
     */
    String getStatus();
    
    /**
     * Checks if the monitored entity has any active alerts.
     * @return true if alerts are present
     */
    boolean hasAlerts();
    
    /**
     * Gets the alert message if any.
     * @return alert message or null
     */
    String getAlertMessage();
    
    /**
     * Gets the severity level of the current state.
     * @return severity level (1-5, where 5 is most severe)
     */
    int getSeverityLevel();
}
