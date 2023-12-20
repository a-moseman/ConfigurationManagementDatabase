package org.amoseman.cmdb.databaseclient.databaseclients;

import org.amoseman.cmdb.databaseclient.DatabaseClient;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * Provides a connection to a Redis database.
 */
public class RedisDatabaseClient implements DatabaseClient {
    private final RedissonClient redissonClient;

    /**
     * Instantiate an instance of RedisDatabaseClient and establish a connection to the Redis database using the provided connection string.
     * @param connectionString String
     */
    public RedisDatabaseClient(String connectionString) {
        Config config = new Config();
        config.useSingleServer().setAddress(connectionString);
        this.redissonClient = Redisson.create(config);
    }

    @Override
    public RedissonClient getDatabase() {
        return redissonClient;
    }

    @Override
    public void close() {
        this.redissonClient.shutdown();
    }
}
