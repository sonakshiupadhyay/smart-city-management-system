package com.smartcity.exception;

/**
 * DatabaseException - Exception for database-related errors.
 * Extends SmartCityException (Inheritance in exception hierarchy).
 * 
 * @author Smart City Management System
 */
public class DatabaseException extends SmartCityException {

    public DatabaseException(String message) {
        super(message, "SCM_DB_ERROR", 500);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, "SCM_DB_ERROR", cause);
    }
}
