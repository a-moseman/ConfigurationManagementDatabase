package org.amoseman.cmdb.application.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the contents of a configuration.
 */
public class ConfigurationValue {
    private String content;
    private String created;
    private String updated;

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

    public ConfigurationValue() {
        this.content = "none";
        this.created = "none";
        this.updated = "none";
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
