package org.amoseman.cmdb.dao;

import org.amoseman.cmdb.application.configuration.ConfigurationValue;
import org.amoseman.cmdb.databaseclient.DatabaseClient;

import java.util.Optional;

/**
 * Represents a data access object for configuration values.
 */
public interface ConfigurationDAO {
    /**
     * Get the value of a configuration.
     * @param account String The name of the account with the configuration.
     * @param label String The name of the configuration.
     * @return Optional<ConfigurationValue>
     */
    Optional<ConfigurationValue> getValue(String account, String label);

    /**
     * Set the value of an existing configuration.
     * @param account String The name of the account with the configuration.
     * @param label String The name of the configuration.
     * @param value String The new value of the configuration.
     */
    boolean setValue(String account, String label, String value);

    /**
     * Add a new configuration.
     * @param account String The name of the account with the configuration.
     * @param label String The name of the configuration.
     * @param value String The value of the configuration.
     */
    boolean addValue(String account, String label, String value);

    /**
     * Remove a configuration.
     * @param account String The name of the account with the configuration.
     * @param label String The name of the configuration.
     */
    boolean removeValue(String account, String label);
}
