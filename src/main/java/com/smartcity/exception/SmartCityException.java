package com.smartcity.exception;

/**
 * SmartCityException - Base custom exception for the Smart City system.
 * Demonstrates Exception Handling in Java.
 * All custom exceptions extend this class (Inheritance in exceptions).
 * 
 * @author Smart City Management System
 */
public class SmartCityException extends Exception {
    
    private final String errorCode;
    private final int httpStatus;

    public SmartCityException(String message) {
        super(message);
        this.errorCode = "SCM_GENERAL_ERROR";
        this.httpStatus = 500;
    }

    public SmartCityException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = 500;
    }

    public SmartCityException(String message, String errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public SmartCityException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "SCM_GENERAL_ERROR";
        this.httpStatus = 500;
    }

    public SmartCityException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = 500;
    }

    public String getErrorCode() { return errorCode; }
    public int getHttpStatus() { return httpStatus; }

    @Override
    public String toString() {
        return "SmartCityException{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + getMessage() + '\'' +
                ", httpStatus=" + httpStatus +
                '}';
    }
}
