package org.amoseman.cmdb.application;

import com.codahale.metrics.annotation.Timed;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.amoseman.cmdb.databaseclient.DatabaseClient;

@Path("/cmdb")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigurationResource {
    private final DatabaseClient databaseClient;
    private final String defaultValue;

    public ConfigurationResource(DatabaseClient databaseClient, String defaultValue) {
        this.databaseClient = databaseClient;
        this.defaultValue = defaultValue;
    }

    @GET
    @Timed
    public ConfigurationValue read(@QueryParam("account") @NotEmpty String account, @QueryParam("label") @NotEmpty String label) {
        String value = databaseClient.read(account, label).orElse(defaultValue);
        return new ConfigurationValue(value);
    }

    @POST
    @Timed
    public void write(@QueryParam("account") @NotEmpty String account, @QueryParam("label") @NotEmpty String label, @QueryParam("value") @NotEmpty String value) {
        databaseClient.write(account, label, value);
    }
}
