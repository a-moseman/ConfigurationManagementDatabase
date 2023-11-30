package org.amoseman.cmdb.application;

import com.codahale.metrics.annotation.Timed;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.amoseman.cmdb.databaseclient.DatabaseClient;

@Path("/cmdb")
@Produces(MediaType.APPLICATION_JSON)
public class ApplicationResource {
    private final DatabaseClient databaseClient;
    private final String defaultValue;

    public ApplicationResource(DatabaseClient databaseClient, String defaultValue) {
        this.databaseClient = databaseClient;
        this.defaultValue = defaultValue;
    }

    @GET
    @Timed
    public Response read(@QueryParam("collection") @NotEmpty String collection, @QueryParam("label") @NotEmpty String label) {
        String value = databaseClient.read(collection, label).orElse(defaultValue);
        return new Response(value);
    }

    @POST
    @Timed
    public void write(@QueryParam("collection") @NotEmpty String collection, @QueryParam("label") @NotEmpty String label, @QueryParam("value") @NotEmpty String value) {
        databaseClient.write(collection, label, value);
    }
}
