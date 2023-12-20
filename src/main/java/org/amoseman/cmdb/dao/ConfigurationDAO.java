package org.amoseman.cmdb.dao;

import org.amoseman.cmdb.application.configuration.ConfigurationValue;
import org.amoseman.cmdb.databaseclient.DatabaseClient;

import java.util.Optional;

/**
 * Represents a database access object for configuration values.
 */
public abstract class ConfigurationDAO extends DAO {
    /**
     * Instantiate the ConfigurationDatabaseAccess.
     * @param client DatabaseClient The connection to the database.
     */
    public ConfigurationDAO(DatabaseClient client) {
        super(client);
    }

    /**
     * Get the value of a configuration.
     * @param account String The name of the account with the configuration.
     * @param label String The name of the configuration.
     * @return Optional<ConfigurationValue>
     */
    public abstract Optional<ConfigurationValue> getValue(String account, String label);

    /**
     * Set the value of an existing configuration.
     * @param account String The name of the account with the configuration.
     * @param label String The name of the configuration.
     * @param value String The new value of the configuration.
     */
    public abstract void setValue(String account, String label, String value);

    /**
     * Add a new configuration.
     * @param account String The name of the account with the configuration.
     * @param label String The name of the configuration.
     * @param value String The value of the configuration.
     */
    public abstract void addValue(String account, String label, String value);

    /**
     * Remove a configuration.
     * @param account String The name of the account with the configuration.
     * @param label String The name of the configuration.
     */
    public abstract void removeValue(String account, String label);
}
