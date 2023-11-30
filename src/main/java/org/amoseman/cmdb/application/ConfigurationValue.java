package org.amoseman.cmdb.application;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigurationValue {
    private final String content;

    public ConfigurationValue(String content) {
        this.content = content;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }
}
