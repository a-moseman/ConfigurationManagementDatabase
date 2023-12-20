package org.amoseman.cmdb.application.resources;


import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.dao.AccountDatabaseAccess;

/**
 * The account resource.
 */
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    private final AccountDatabaseAccess accountDatabaseAccess;

    public AccountResource(AccountDatabaseAccess accountDatabaseAccess) {
        this.accountDatabaseAccess = accountDatabaseAccess;
    }

    /**
     * The POST request to add a new account.
     * @param account String The account name.
     * @param password String The account's password.
     */
    @POST
    @Timed
    public void addAccount(@QueryParam("account") String account, @QueryParam("password") String password) {
        accountDatabaseAccess.addAccount(account, password);
    }

    /**
     * The DELETE request to remove an account.
     * @param user User The authenticated user with the relevant account to remove.
     */
    @DELETE
    @Timed
    public void removeAccount(@Auth User user) {
        accountDatabaseAccess.deleteAccount(user.getName());
    }
}
