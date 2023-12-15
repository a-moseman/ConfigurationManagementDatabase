package org.amoseman.cmdb.databaseclient.databaseclients;

import org.amoseman.cmdb.databaseclient.DatabaseClient;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Optional;

public class RedisDatabaseClient implements DatabaseClient {
    private final RedissonClient redis;

    public RedisDatabaseClient(String address) {
        String connectionString = String.format("redis://%s", address);
        Config config = new Config();
        config.useSingleServer().setAddress(connectionString);
        this.redis = Redisson.create(config);
    }

    @Override
    public Optional<String> read(String account, String label) {
        RMap<String, String> map = redis.getMap(account);
        if (map.containsKey(label)) {
            return Optional.of(map.get(label));
        }
        return Optional.empty();
    }

    @Override
    public void create(String account, String label, String value) {
        RMap<String, String> map = redis.getMap(account);
        if (map.containsKey((label))) {
            return;
        }
        map.put(label, value);
    }

    @Override
    public void update(String account, String label, String value) {
        RMap<String, String> map = redis.getMap(account);
        if (!map.containsKey((label))) {
            return;
        }
        map.put(label, value);
    }

    @Override
    public void delete(String account, String label) {
        RMap<String, String> map = redis.getMap(account);
        if (!map.containsKey(label)) {
            return;
        }
        map.remove(label);
    }
}
