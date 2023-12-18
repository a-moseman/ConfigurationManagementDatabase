package org.amoseman.cmdb.application.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotEmpty;

public class ApplicationConfiguration extends Configuration {
    @NotEmpty
    private String defaultValue = "NULL";

    @NotEmpty
    private String databaseAddress;

    @NotEmpty
    private String databaseType;

    @NotEmpty
    private String databaseUsername;

    @NotEmpty
    private String databasePassword;

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

    @JsonProperty
    public String getDatabaseType() {
        return databaseType;
    }

    @JsonProperty
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    @JsonProperty
    public String getDatabaseUsername() {
        return databaseUsername;
    }

    @JsonProperty
    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    @JsonProperty
    public String getDatabasePassword() {
        return databasePassword;
    }

    @JsonProperty
    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
}
