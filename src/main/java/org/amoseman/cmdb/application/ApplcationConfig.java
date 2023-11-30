package org.amoseman.cmdb.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotEmpty;

public class ApplcationConfig extends Configuration {
    @NotEmpty
    private String defaultValue = "NULL";

    @NotEmpty
    private String databaseAddress;


    @JsonProperty
    public String getDefaultValue() {
        return defaultValue;
    }

    @JsonProperty
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @JsonProperty
    public String getDatabaseAddress() {
        return databaseAddress;
    }

    @JsonProperty
    public void setDatabaseAddress(String databaseAddress) {
        this.databaseAddress = databaseAddress;
    }
}
