package org.amoseman.cmdb.application.resources;

import org.amoseman.cmdb.dao.accountdaos.MongoAccountDatabaseAccess;
import org.amoseman.cmdb.dao.accountdaos.RedisAccountDatabaseAccess;
import org.amoseman.cmdb.databaseclient.databaseclients.MongoDatabaseClient;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountResourceTest {
    private MongoDatabaseClient mongoDatabaseClient;
    private MongoAccountDatabaseAccess mongoAccountDatabaseAccess;

    private RedisDatabaseClient redisDatabaseClient;
    private RedisAccountDatabaseAccess redisAccountDatabaseAccess;

    @BeforeEach
    void setUp() {
        mongoDatabaseClient = new MongoDatabaseClient("localhost:27017");
        mongoAccountDatabaseAccess = new MongoAccountDatabaseAccess(mongoDatabaseClient);

        redisDatabaseClient = new RedisDatabaseClient("localhost:6379");
        redisAccountDatabaseAccess = new RedisAccountDatabaseAccess(redisDatabaseClient);
    }

    @AfterEach
    void tearDown() {
        mongoAccountDatabaseAccess.deleteAccount("test-account");
        redisAccountDatabaseAccess.deleteAccount("test-account");
    }

    @Test
    void addAccount() {
        mongoAccountDatabaseAccess.addAccount("test-account", "test-password");
        assertEquals(true, mongoAccountDatabaseAccess.validate("test-account", "test-password"));
        assertEquals(false, mongoAccountDatabaseAccess.validate("test-account", "nonesense"));
        assertEquals(false, mongoAccountDatabaseAccess.validate("nonesense", "test-password"));

        redisAccountDatabaseAccess.addAccount("test-account", "test-password");
        assertEquals(true, redisAccountDatabaseAccess.validate("test-account", "test-password"));
        assertEquals(false, redisAccountDatabaseAccess.validate("test-account", "nonesense"));
        assertEquals(false, redisAccountDatabaseAccess.validate("nonesense", "test-password"));
    }

    @Test
    void removeAccount() {
        mongoAccountDatabaseAccess.addAccount("test-account", "test-password");
        mongoAccountDatabaseAccess.deleteAccount("test-account");
        assertEquals(false, mongoAccountDatabaseAccess.validate("test-account", "test-password"));

        redisAccountDatabaseAccess.addAccount("test-account", "test-password");
        redisAccountDatabaseAccess.deleteAccount("test-account");
        assertEquals(false, redisAccountDatabaseAccess.validate("test-accouht", "test-password"));
    }
}