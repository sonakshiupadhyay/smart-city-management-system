package com.smartcity.model;

import java.sql.Timestamp;

/**
 * Abstract Class: User
 * Demonstrates Abstraction and serves as the base class for all user types.
 * Uses Inheritance - CityAdministrator, UrbanPlanner, and Resident extend this class.
 * 
 * @author Smart City Management System
 */
public abstract class User {
    
    // Encapsulated fields (Encapsulation)
    private int userId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private String role;
    private String department;
    private String address;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default Constructor
    public User() {
        this.isActive = true;
    }

    // Parameterized Constructor
    public User(String username, String password, String email, String fullName, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.isActive = true;
    }

    // Full Parameterized Constructor
    public User(int userId, String username, String password, String email, 
                String fullName, String phone, String role, String department,
                String address, boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.department = department;
        this.address = address;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Abstract method - must be implemented by subclasses (Polymorphism)
    /**
     * Returns the dashboard URL for this user type.
     * Each user type has a different dashboard.
     */
    public abstract String getDashboardURL();

    /**
     * Returns the permissions available to this user type.
     * Demonstrates polymorphism - each user type returns different permissions.
     */
    public abstract String[] getPermissions();

    /**
     * Returns a welcome message specific to this user type.
     */
    public abstract String getWelcomeMessage();

    // Concrete method - shared by all user types
    /**
     * Validates user credentials.
     * @return true if the user has valid data
     */
    public boolean isValid() {
        return username != null && !username.isEmpty() 
            && email != null && !email.isEmpty()
            && password != null && !password.isEmpty();
    }

    // toString() override (Polymorphism)
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    // equals() and hashCode() override
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId == user.userId && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return 31 * userId + (username != null ? username.hashCode() : 0);
    }

    // Getters and Setters (Encapsulation)
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
