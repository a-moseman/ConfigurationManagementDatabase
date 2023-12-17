package org.amoseman.cmdb.dao;

import org.amoseman.cmdb.databaseclient.DatabaseClient;

import java.util.Optional;

public abstract class ConfigurationDatabaseAccess extends DatabaseAccessObject {

    public ConfigurationDatabaseAccess(DatabaseClient client) {
        super(client);
    }

    public abstract Optional<String> getConfigurationValue(String account, String label);
    public abstract void setConfigurationValue(String account, String label, String value);
    public abstract void addConfigurationValue(String account, String label, String value);
    public abstract void removeConfigurationValue(String account, String label);
}
