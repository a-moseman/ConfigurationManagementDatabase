package org.amoseman.cmdb.dao;

import org.amoseman.cmdb.application.pojo.ConfigurationValue;

import java.util.Optional;

/**
 * Represents a data access object for configuration values.
 */
public interface ConfigurationDAO {
    Optional<ConfigurationValue> getValue(String account, String label);
    boolean setValue(String account, String label, String value);
    boolean addValue(String account, String label, String value);
    boolean removeValue(String account, String label);
}
