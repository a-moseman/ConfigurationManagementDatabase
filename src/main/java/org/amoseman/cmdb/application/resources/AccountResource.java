package org.amoseman.cmdb.application.resources;


import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.application.pojo.Account;
import org.amoseman.cmdb.dao.AccountDAO;

import javax.print.attribute.standard.Media;

/**
 * The account resource.
 */
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    private final AccountDAO accountDAO;
    private final Meter addAccountMeter;
    private final Meter removeAccountMeter;

    public AccountResource(AccountDAO accountDAO, MetricRegistry metrics) {
        this.accountDAO = accountDAO;
        this.addAccountMeter = metrics.meter("add-account");
        this.removeAccountMeter = metrics.meter("remove-account");

    }

    /**
     * The POST request to add a new account.
     * @param account String The account name.
     * @param password String The account's password.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed
    public Response addAccount(@NotNull Account account) {
        addAccountMeter.mark();
        if (accountDAO.addAccount(account.getName(), account.getPass())) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * The DELETE request to remove an account.
     * @param user User The authenticated user with the relevant account to remove.
     */
    @DELETE
    @Timed
    public Response removeAccount(@Auth User user) {
        removeAccountMeter.mark();
        if (accountDAO.deleteAccount(user.getName())) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
