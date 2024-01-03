package org.amoseman.cmdb.application.resources;


import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.dao.AccountDAO;

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
    @Timed
    public boolean addAccount(@QueryParam("account") String account, @QueryParam("password") String password) {
        addAccountMeter.mark();
        return accountDAO.addAccount(account, password);
    }

    /**
     * The DELETE request to remove an account.
     * @param user User The authenticated user with the relevant account to remove.
     */
    @DELETE
    @Timed
    public boolean removeAccount(@Auth User user) {
        removeAccountMeter.mark();
        return accountDAO.deleteAccount(user.getName());
    }
}
