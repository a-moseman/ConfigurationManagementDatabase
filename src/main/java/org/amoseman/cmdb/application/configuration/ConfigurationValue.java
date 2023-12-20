package org.amoseman.cmdb.application.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the contents of a configuration.
 */
public class ConfigurationValue {
    private final String content;
    private final String created;
    private final String updated;

    /**
     * Instantiate a ConfigurationValue.
     * @param content String The value of the configuration.
     * @param created String The date and time the configuration was created in ISO format.
     * @param updated String The date and time the configuration was last updated in ISO format.
     */
    public ConfigurationValue(String content, String created, String updated) {
        this.content = content;
        this.created = created;
        this.updated = updated;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }

    @JsonProperty
    public String getCreated() {
        return created;
    }

    @JsonProperty
    public String getUpdated() {
        return updated;
    }
}
