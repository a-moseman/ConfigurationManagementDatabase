package org.amoseman.cmdb.dao.configurationdaos;

import org.amoseman.cmdb.dao.ConfigurationDatabaseAccess;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.Optional;

public class RedisConfigurationDatabaseAccess extends ConfigurationDatabaseAccess {
    private final RedissonClient database;

    public RedisConfigurationDatabaseAccess(RedisDatabaseClient client) {
        super(client);
        database = client.getDatabase();
    }

    @Override
    public Optional<String> getConfigurationValue(String account, String label) {
        RMap<String, String> map = database.getMap(account);
        if (map.containsKey(label)) {
            return Optional.of(map.get(label));
        }
        return Optional.empty();
    }

    @Override
    public void setConfigurationValue(String account, String label, String value) {
        RMap<String, String> map = database.getMap(account);
        if (!map.containsKey((label))) {
            return;
        }
        map.put(label, value);
    }

    @Override
    public void addConfigurationValue(String account, String label, String value) {
        RMap<String, String> map = database.getMap(account);
        if (map.containsKey((label))) {
            return;
        }
        map.put(label, value);
    }

    @Override
    public void removeConfigurationValue(String account, String label) {
        RMap<String, String> map = database.getMap(account);
        if (!map.containsKey(label)) {
            return;
        }
        map.remove(label);
    }
}
