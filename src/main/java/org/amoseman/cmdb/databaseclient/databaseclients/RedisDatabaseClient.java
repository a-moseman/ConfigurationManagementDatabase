package org.amoseman.cmdb.databaseclient.databaseclients;

import org.amoseman.cmdb.databaseclient.DatabaseClient;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class RedisDatabaseClient implements DatabaseClient {
    private final RedissonClient redis;

    public RedisDatabaseClient(String address) {
        Config config = new Config();
        config.useSingleServer().setAddress(address);
        this.redis = Redisson.create(config);
    }

    @Override
    public Optional<String> read(String collection, String label) {
        RMap<String, String> map = redis.getMap(collection);
        if (map.containsKey(label)) {
            return Optional.of(map.get(label));
        }
        return Optional.empty();
    }

    @Override
    public void write(String collection, String label, String value) {
        RMap<String, String> map = redis.getMap(collection);
        map.put(label, value);
    }
}
