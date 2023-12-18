package org.amoseman.cmdb.dao;

import org.amoseman.cmdb.application.configuration.ConfigurationValue;
import org.amoseman.cmdb.databaseclient.DatabaseClient;

import java.util.Optional;

public abstract class ConfigurationDatabaseAccess extends DatabaseAccessObject {
    public ConfigurationDatabaseAccess(DatabaseClient client) {
        super(client);
    }

    public abstract Optional<ConfigurationValue> getValue(String account, String label);
    public abstract void setValue(String account, String label, String value);
    public abstract void addValue(String account, String label, String value);
    public abstract void removeValue(String account, String label);
}
