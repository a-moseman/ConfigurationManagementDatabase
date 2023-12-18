package org.amoseman.cmdb.application.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigurationValue {
    private final String content;
    private final String created;
    private final String updated;

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
