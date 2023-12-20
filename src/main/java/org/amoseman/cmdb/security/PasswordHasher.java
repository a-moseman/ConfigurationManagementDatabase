package org.amoseman.cmdb.security;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Provides functionality relating to password hashing.
 */
public class PasswordHasher {
    private static final int ITERATIONS = 2;
    private static final int MEMORY_LIMIT = 66536;
    private static final int HASH_LENGTH = 32;
    private static final int PARALLELISM = 1;
    private static final int SALT_LENGTH = 16;
    private final SecureRandom random;


    /**
     * Construct an instance of a PasswordHasher.
     */
    public PasswordHasher() {
        this.random = new SecureRandom();
    }

    /**
     * Generate 16 bytes of salt for the hash.
     * @return byte[]
     */
    public byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Get the builder for a Argon2Parameters instance.
     * @param salt byte[] The 16 bytes of salt.
     * @return Argon2Parameters.Builder
     */
    private Argon2Parameters.Builder getBuilder(byte[] salt) {
        return new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(ITERATIONS)
                .withMemoryAsKB(MEMORY_LIMIT)
                .withParallelism(PARALLELISM)
                .withSalt(salt);
    }

    /**
     * Generate a hash of the provided password using the provided 16 bytes of salt.
     * @param password String The password.
     * @param salt byte[] The 16 bytes of salt.
     * @return String
     */
    public String generate(String password, byte[] salt) {
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(getBuilder(salt).build());
        byte[] hash = new byte[HASH_LENGTH];
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), hash, 0, hash.length);
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Validate that the provided password with provided salt has parity with the provided hash.
     * @param password String The password.
     * @param hashString String The base 64 string representation of the hash.
     * @param saltString String The base 64 string representation of the 16 bytes of salt.
     * @return boolean
     */
    public boolean validate(String password, String hashString, String saltString) {
        byte[] salt = Base64.getDecoder().decode(saltString);
        String testHash = generate(password, salt);
        return testHash.equals(hashString);
    }

    /**
     * Convert an array of bytes into a base 64 string.
     * @param bytes byte[] The bytes.
     * @return String
     */
    public String asBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
