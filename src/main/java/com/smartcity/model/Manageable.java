package com.smartcity.model;

/**
 * Interface: Manageable
 * Demonstrates Interface concept in Java OOP.
 * Any city resource that can be managed implements this interface.
 * 
 * @author Smart City Management System
 */
public interface Manageable {
    
    /**
     * Allocates the specified amount of resource.
     * @param amount the amount to allocate
     * @return true if allocation is successful
     */
    boolean allocate(double amount);
    
    /**
     * Deallocates the specified amount of resource.
     * @param amount the amount to deallocate
     * @return true if deallocation is successful
     */
    boolean deallocate(double amount);
    
    /**
     * Gets the current utilization percentage.
     * @return utilization as a percentage (0-100)
     */
    double getUtilization();
    
    /**
     * Optimizes the resource usage.
     * @return optimization report as a String
     */
    String optimize();
}
