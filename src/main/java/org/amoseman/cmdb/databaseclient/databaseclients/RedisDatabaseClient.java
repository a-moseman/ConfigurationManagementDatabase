package org.amoseman.cmdb.databaseclient.databaseclients;

import org.amoseman.cmdb.databaseclient.DatabaseClient;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisDatabaseClient implements DatabaseClient {
    private final RedissonClient redissonClient;

    public RedisDatabaseClient(String address) {
        String connectionString = String.format("redis://%s", address);
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
