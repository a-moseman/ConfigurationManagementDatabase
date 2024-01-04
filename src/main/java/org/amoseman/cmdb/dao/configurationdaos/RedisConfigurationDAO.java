package org.amoseman.cmdb.dao.configurationdaos;

import org.amoseman.cmdb.application.pojo.ConfigurationValue;
import org.amoseman.cmdb.application.pojo.LabelValuePair;
import org.amoseman.cmdb.dao.ConfigurationDAO;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class RedisConfigurationDAO implements ConfigurationDAO {
    private final RedissonClient database;

    public RedisConfigurationDAO(RedisDatabaseClient client) {
        database = client.getDatabase();
    }

    @Override
    public Optional<ConfigurationValue> getValue(String account, String label) {
        RMap<String, String> configurations = getConfigurationMap(account);
        RMap<String, String> createds = getCreatedMap(account);
        RMap<String, String> updateds = getUpdatedMap(account);
        if (!configurations.containsKey(label)) {
            return Optional.empty();
        }
        String value = configurations.get(label);
        String created = createds.get(label);
        String updated = updateds.get(label);
        return Optional.of(new ConfigurationValue(label, value, created, updated));
    }

    @Override
    public boolean setValue(String account, String label, String value) {
        RMap<String, String> configurations = getConfigurationMap(account);
        RMap<String, String> updateds = getUpdatedMap(account);
        if (!configurations.containsKey((label))) {
            return false;
        }
        LocalDateTime dateTime = LocalDateTime.now();
        configurations.put(label, value);
        updateds.put(label, dateTime.format(DateTimeFormatter.ISO_DATE_TIME));
        return true;
    }

    @Override
    public boolean addValue(String account, String label, String value) {
        RMap<String, String> configurations = getConfigurationMap(account);
        RMap<String, String> createds = getCreatedMap(account);
        RMap<String, String> updateds = getUpdatedMap(account);
        if (configurations.containsKey((label))) {
            return false;
        }
        configurations.put(label, value);
        LocalDateTime dateTime = LocalDateTime.now();
        String dateTimeString = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        createds.put(label, dateTimeString);
        updateds.put(label, dateTimeString);
        return true;
    }

    @Override
    public boolean removeValue(String account, String label) {
        RMap<String, String> configurations = getConfigurationMap(account);
        RMap<String, String> createds = getCreatedMap(account);
        RMap<String, String> updateds = getUpdatedMap(account);
        if (!configurations.containsKey(label)) {
            return false;
        }
        configurations.remove(label);
        createds.remove(label);
        updateds.remove(label);
        return true;
    }

    private RMap<String, String> getConfigurationMap(String account) {
        return database.getMap(String.format("CONFIGURATION-%s", account));
    }

    private RMap<String, String> getCreatedMap(String account) {
        return database.getMap(String.format("CREATED-%s", account));
    }

    private RMap<String, String> getUpdatedMap(String account) {
        return database.getMap(String.format("UPDATED-%s", account));
    }
}
