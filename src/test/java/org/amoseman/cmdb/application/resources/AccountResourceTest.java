package org.amoseman.cmdb.application.resources;

import org.amoseman.cmdb.dao.accountdaos.MongoAccountDatabaseAccess;
import org.amoseman.cmdb.dao.accountdaos.RedisAccountDatabaseAccess;
import org.amoseman.cmdb.databaseclient.databaseclients.MongoDatabaseClient;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class AccountResourceTest {
    private MongoDatabaseClient mongoDatabaseClient;
    private MongoAccountDatabaseAccess mongoAccountDatabaseAccess;

    private RedisDatabaseClient redisDatabaseClient;
    private RedisAccountDatabaseAccess redisAccountDatabaseAccess;

    @BeforeEach
    void setUp() {
        mongoDatabaseClient = new MongoDatabaseClient("localhost:27017", "example", "example");
        mongoAccountDatabaseAccess = new MongoAccountDatabaseAccess(mongoDatabaseClient);

        redisDatabaseClient = new RedisDatabaseClient("localhost:6379", "example", "example");
        redisAccountDatabaseAccess = new RedisAccountDatabaseAccess(redisDatabaseClient);
    }

    @AfterEach
    void tearDown() {
        mongoAccountDatabaseAccess.deleteAccount("test-account");
        redisAccountDatabaseAccess.deleteAccount("test-account");
    }

    @Test
    @Order(0)
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
    @Order(1)
    void removeAccount() {
        mongoAccountDatabaseAccess.deleteAccount("test-account");
        assertEquals(false, mongoAccountDatabaseAccess.validate("test-account", "test-password"));

        redisAccountDatabaseAccess.deleteAccount("test-account");
        assertEquals(false, redisAccountDatabaseAccess.validate("test-accouht", "test-password"));
    }
}