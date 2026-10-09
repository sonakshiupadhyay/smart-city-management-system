package com.smartcity.exception;

/**
 * ResourceNotFoundException - Exception for resource not found errors.
 * Extends SmartCityException (Inheritance in exception hierarchy).
 * 
 * @author Smart City Management System
 */
public class ResourceNotFoundException extends SmartCityException {

    public ResourceNotFoundException(String message) {
        super(message, "SCM_NOT_FOUND", 404);
    }

    public ResourceNotFoundException(String resourceType, int id) {
        super(resourceType + " with ID " + id + " not found", "SCM_NOT_FOUND", 404);
    }
}
