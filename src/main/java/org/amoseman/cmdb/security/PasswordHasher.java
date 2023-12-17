package org.amoseman.cmdb.security;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class PasswordHasher {
    private final Argon2PasswordEncoder encoder;

    public PasswordHasher() {
        this.encoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);
    }

    public String hash(String password) {
        return encoder.encode(password);
    }
}
