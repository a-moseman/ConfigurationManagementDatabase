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

@Path("/cmdb")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigurationResource {
    private final ConfigurationDatabaseAccess configurationDatabaseAccess;
    private final String defaultValue;

    public ConfigurationResource(ConfigurationDatabaseAccess configurationDatabaseAccess, String defaultValue) {
        this.configurationDatabaseAccess = configurationDatabaseAccess;
        this.defaultValue = defaultValue;
    }

    @GET
    @PermitAll
    @Timed
    public ConfigurationValue read(@Auth User user, @QueryParam("label") @NotEmpty String label) {
        String value = configurationDatabaseAccess.getConfigurationValue(user.getName(), label).orElse(defaultValue);
        return new ConfigurationValue(value);
    }

    @POST
    @PermitAll
    @Timed
    public void create(@Auth User user, @QueryParam("label") @NotEmpty String label, @QueryParam("value") @NotEmpty String value) {
        configurationDatabaseAccess.addConfigurationValue(user.getName(), label, value);
    }

    @PUT
    @PermitAll
    @Timed
    public void update(@Auth User user, @QueryParam("label") @NotEmpty String label, @QueryParam("value") @NotEmpty String value) {
        configurationDatabaseAccess.setConfigurationValue(user.getName(), label, value);
    }

    @DELETE
    @PermitAll
    @Timed
    public void delete(@Auth User user, @QueryParam("label") @NotEmpty String label) {
        configurationDatabaseAccess.removeConfigurationValue(user.getName(), label);
    }
}
