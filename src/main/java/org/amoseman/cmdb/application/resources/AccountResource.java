package org.amoseman.cmdb.application.resources;


import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.dao.AccountDatabaseAccess;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    private final AccountDatabaseAccess accountDatabaseAccess;

    public AccountResource(AccountDatabaseAccess accountDatabaseAccess) {
        this.accountDatabaseAccess = accountDatabaseAccess;
    }

    @POST
    @Timed
    public void addAccount(@QueryParam("account") String account, @QueryParam("password") String password) {
        accountDatabaseAccess.addAccount(account, password);
    }

    @DELETE
    @Timed
    public void removeAccount(@Auth User user) {
        accountDatabaseAccess.deleteAccount(user.getName());
    }
}
