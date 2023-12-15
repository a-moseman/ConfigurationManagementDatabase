package org.amoseman.cmdb.application.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;
import java.util.Set;

public class ConfigurationManagementDatabaseConfiguration extends Configuration {
    @NotEmpty
    private String defaultValue = "NULL";

    @NotEmpty
    private String databaseAddress;

    @NotEmpty
    private String accountFilePath;

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
    public String getDatabaseAddress() {
        return databaseAddress;
    }

    @JsonProperty
    public void setDatabaseAddress(String databaseAddress) {
        this.databaseAddress = databaseAddress;
    }

    @JsonProperty
    public String getAccountFilePath() {
        return accountFilePath;
    }

    @JsonProperty
    public void setAccountFilePath(String accountFilePath) {
        this.accountFilePath = accountFilePath;
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
