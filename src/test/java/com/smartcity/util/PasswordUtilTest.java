package com.smartcity.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PasswordUtilTest - Verifies SHA-256 password hashing.
 */
public class PasswordUtilTest {

    @Test
    public void testHashAndVerify() {
        String plain = "SecretPassword123";
        String hash = PasswordUtil.hashPassword(plain);

        assertNotNull(hash);
        assertNotEquals(plain, hash);
        assertEquals(64, hash.length()); // SHA-256 produces 64 hex characters

        assertTrue(PasswordUtil.verifyPassword(plain, hash));
        assertFalse(PasswordUtil.verifyPassword("WrongPassword", hash));
    }
}
