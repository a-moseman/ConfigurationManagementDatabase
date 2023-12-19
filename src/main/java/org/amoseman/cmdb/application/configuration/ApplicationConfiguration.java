package org.amoseman.cmdb.application.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotEmpty;

public class ApplicationConfiguration extends Configuration {
    @NotEmpty
    private String defaultValue = "NULL";

    @NotEmpty
    private String databaseConnectionString;

    @NotEmpty
    private String databaseType;

    @JsonProperty
    public String getDefaultValue() {
        return defaultValue;
    }

    @JsonProperty
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @JsonProperty
    public String getDatabaseConnectionString() {
        return databaseConnectionString;
    }

    @JsonProperty
    public void setDatabaseConnectionString(String databaseConnectionString) {
        this.databaseConnectionString = databaseConnectionString;
    }

    @JsonProperty
    public String getDatabaseType() {
        return databaseType;
    }

    @JsonProperty
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }
}
