package org.amoseman.cmdb.application.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the contents of a configuration.
 */
public class ConfigurationValue extends LabelValuePair {
    private String created;
    private String updated;

    public ConfigurationValue(String label, String value, String created, String updated) {
        super(label, value);
        this.created = created;
        this.updated = updated;
    }

    public ConfigurationValue() {

    }

    @JsonProperty
    public String getCreated() {
        return created;
    }

    @JsonProperty
    public String getUpdated() {
        return updated;
    }

    @JsonProperty
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty
    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
