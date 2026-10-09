package com.smartcity.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordUtil - Utility class for password hashing.
 * Uses SHA-256 hashing for password security.
 * 
 * @author Smart City Management System
 */
public class PasswordUtil {

    /**
     * Hashes a password using SHA-256.
     * 
     * @param password the plain text password
     * @return hashed password as hex string
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Fallback: return plain text (for demo purposes)
            System.err.println("[PasswordUtil] SHA-256 not available, using plain text.");
            return password;
        }
    }

    /**
     * Verifies a password against its hash.
     * 
     * @param password the plain text password
     * @param hashedPassword the stored hashed password
     * @return true if passwords match
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        String hash = hashPassword(password);
        return hash.equals(hashedPassword);
    }
}
