package org.amoseman.cmdb.application.resources;

import org.amoseman.cmdb.dao.accountdaos.MongoAccountDatabaseAccess;
import org.amoseman.cmdb.dao.accountdaos.RedisAccountDatabaseAccess;
import org.amoseman.cmdb.dao.configurationdaos.MongoConfigurationDatabaseAccess;
import org.amoseman.cmdb.dao.configurationdaos.RedisConfigurationDatabaseAccess;
import org.amoseman.cmdb.databaseclient.databaseclients.MongoDatabaseClient;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConfigurationResourceTest {
    private MongoDatabaseClient mongoDatabaseClient;
    private MongoConfigurationDatabaseAccess mongoConfigurationDatabaseAccess;
    private MongoAccountDatabaseAccess mongoAccountDatabaseAccess;
    private RedisDatabaseClient redisDatabaseClient;
    private RedisConfigurationDatabaseAccess redisConfigurationDatabaseAccess;
    private RedisAccountDatabaseAccess redisAccountDatabaseAccess;

    @BeforeEach
    void setUp() {
        mongoDatabaseClient = new MongoDatabaseClient("localhost:27017");
        mongoAccountDatabaseAccess = new MongoAccountDatabaseAccess(mongoDatabaseClient);
        mongoConfigurationDatabaseAccess = new MongoConfigurationDatabaseAccess(mongoDatabaseClient);
        mongoAccountDatabaseAccess.addAccount("test-account", "test-password");

        redisDatabaseClient = new RedisDatabaseClient("localhost:6379");
        redisAccountDatabaseAccess = new RedisAccountDatabaseAccess(redisDatabaseClient);
        redisConfigurationDatabaseAccess = new RedisConfigurationDatabaseAccess(redisDatabaseClient);
        redisAccountDatabaseAccess.addAccount("test-account", "test-password");
    }

    @AfterEach
    void tearDown() {
        mongoAccountDatabaseAccess.deleteAccount("test-account");
        redisAccountDatabaseAccess.deleteAccount("test-account");
    }

    @Test
    @Order(2)
    void read() {
        assertEquals("changed", mongoConfigurationDatabaseAccess.getValue("test-account", "test-conf").get().getContent());
        assertEquals("changed", redisConfigurationDatabaseAccess.getValue("test-account", "test-conf").get().getContent());
    }

    @Test
    @Order(0)
    void create() {
        mongoConfigurationDatabaseAccess.addValue("test-account", "test-conf", "test-val");
        redisConfigurationDatabaseAccess.addValue("test-account", "test-conf", "test-val");
        assertEquals("test-val", mongoConfigurationDatabaseAccess.getValue("test-account", "test-conf").get().getContent());
        assertEquals("test-val", redisConfigurationDatabaseAccess.getValue("test-account", "test-conf").get().getContent());
    }

    @Test
    @Order(1)
    void update() {
        mongoConfigurationDatabaseAccess.setValue("test-account", "test-conf", "changed");
        redisConfigurationDatabaseAccess.setValue("test-account", "test-conf", "changed");
    }

    @Test
    @Order(3)
    void delete() {
        mongoConfigurationDatabaseAccess.removeValue("test-account", "test-conf");
        redisConfigurationDatabaseAccess.removeValue("test-account", "test-conf");

        assertEquals(Optional.empty(), mongoConfigurationDatabaseAccess.getValue("test-account", "test-conf"));
        assertEquals(Optional.empty(), redisConfigurationDatabaseAccess.getValue("test-account", "test-conf"));
    }
}