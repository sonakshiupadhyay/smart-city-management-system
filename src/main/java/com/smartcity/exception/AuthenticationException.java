package com.smartcity.exception;

/**
 * AuthenticationException - Exception for authentication failures.
 * Extends SmartCityException (Inheritance in exception hierarchy).
 * 
 * @author Smart City Management System
 */
public class AuthenticationException extends SmartCityException {

    public AuthenticationException(String message) {
        super(message, "SCM_AUTH_ERROR", 401);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, "SCM_AUTH_ERROR", cause);
    }
}
