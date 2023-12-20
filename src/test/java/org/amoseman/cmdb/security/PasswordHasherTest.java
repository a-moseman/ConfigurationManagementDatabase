package org.amoseman.cmdb.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {
    private PasswordHasher passwordHasher;
    private SecureRandom random;

    @BeforeEach
    void setUp() {
        passwordHasher = new PasswordHasher();
        random = new SecureRandom();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validate() {
        byte[] passwordBytes = new byte[32];
        random.nextBytes(passwordBytes);
        String password = new String(passwordBytes);
        byte[] salt = passwordHasher.generateSalt();
        String hash = passwordHasher.generate(password, salt);
        String salt64 = passwordHasher.asBase64(salt);
        assertTrue(passwordHasher.validate(password, hash, salt64));
    }
}