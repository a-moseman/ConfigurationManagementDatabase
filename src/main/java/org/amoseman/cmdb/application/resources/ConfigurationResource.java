package org.amoseman.cmdb.application.resources;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.application.pojo.ConfigurationValue;
import org.amoseman.cmdb.application.pojo.LabelValuePair;
import org.amoseman.cmdb.dao.ConfigurationDAO;

import java.util.Optional;

/**
 * The configuration resource.
 */
@Path("/cmdb")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigurationResource {
    private final ConfigurationDAO configurationDAO;
    private final Meter readConfigurationMeter;
    private final Meter createConfigurationMeter;
    private final Meter updateConfigurationMeter;
    private final Meter deleteConfigurationMeter;

    /**
     * Instantiate a ConfigurationResource.
     * @param configurationDAO ConfigurationDatabaseAccess The database access object for configurations.
     */
    public ConfigurationResource(ConfigurationDAO configurationDAO, MetricRegistry metrics) {
        this.configurationDAO = configurationDAO;
        this.readConfigurationMeter = metrics.meter("read-configuration");
        this.createConfigurationMeter = metrics.meter("create-configuration");
        this.updateConfigurationMeter = metrics.meter("update-configuration");
        this.deleteConfigurationMeter = metrics.meter("delete-configuration");
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
    public Response read(@Auth User user, @QueryParam("label") @NotEmpty String label) {
        Optional<ConfigurationValue> maybe = configurationDAO.getValue(user.getName(), label);
        if (maybe.isPresent()) {
            return Response.ok(maybe.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Timed
    public Response create(@Auth User user, @NotNull LabelValuePair labelValuePair) {
        if (configurationDAO.addValue(user.getName(), labelValuePair.getLabel(), labelValuePair.getValue())) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Timed
    public Response update(@Auth User user, @NotNull LabelValuePair labelValuePair) {
        if (configurationDAO.setValue(user.getName(), labelValuePair.getLabel(), labelValuePair.getValue())) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @PermitAll
    @Timed
    public Response delete(@Auth User user, @QueryParam("label") @NotEmpty String label) {
        if (configurationDAO.removeValue(user.getName(), label)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
