package org.amoseman.cmdb.application.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.application.configuration.ConfigurationValue;
import org.amoseman.cmdb.dao.ConfigurationDatabaseAccess;

/**
 * The configuration resource.
 */
@Path("/cmdb")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigurationResource {
    private final ConfigurationDatabaseAccess configurationDatabaseAccess;
    private final ConfigurationValue defaultValue;

    /**
     * Instantiate a ConfigurationResource.
     * @param configurationDatabaseAccess ConfigurationDatabaseAccess The database access object for configurations.
     * @param defaultValue String The default return value for configuration values.
     */
    public ConfigurationResource(ConfigurationDatabaseAccess configurationDatabaseAccess, String defaultValue) {
        this.configurationDatabaseAccess = configurationDatabaseAccess;
        this.defaultValue = new ConfigurationValue(defaultValue, defaultValue, defaultValue);
    }

    /**
     * The GET request to get the value of a configuration.
     * @param user User The authenticated user.
     * @param label String The name of the configuration.
     * @return ConfigurationValue
     */
    @GET
    @PermitAll
    @Timed
    public ConfigurationValue read(@Auth User user, @QueryParam("label") @NotEmpty String label) {
        ConfigurationValue value = configurationDatabaseAccess.getValue(user.getName(), label).orElse(defaultValue);
        return value;
    }

    /**
     * The POST request to add a new configuration.
     * @param user User The authenticated user.
     * @param label String The name of the configuration.
     * @param value String The value of the configuration.
     */
    @POST
    @PermitAll
    @Timed
    public void create(@Auth User user, @QueryParam("label") @NotEmpty String label, @QueryParam("value") @NotEmpty String value) {
        configurationDatabaseAccess.addValue(user.getName(), label, value);
    }

    /**
     * The PUT request to update the value of an existing configuration.
     * @param user User The authenticated user.
     * @param label String The name of the configuration.
     * @param value String The new value of the configuration.
     */
    @PUT
    @PermitAll
    @Timed
    public void update(@Auth User user, @QueryParam("label") @NotEmpty String label, @QueryParam("value") @NotEmpty String value) {
        configurationDatabaseAccess.setValue(user.getName(), label, value);
    }

    /**
     * The DELETE request to remove a configuration.
     * @param user User The authenticated user.
     * @param label String The name of the configuration.
     */
    @DELETE
    @PermitAll
    @Timed
    public void delete(@Auth User user, @QueryParam("label") @NotEmpty String label) {
        configurationDatabaseAccess.removeValue(user.getName(), label);
    }
}
