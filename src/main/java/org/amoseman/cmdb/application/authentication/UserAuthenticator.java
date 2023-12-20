package org.amoseman.cmdb.application.authentication;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.amoseman.cmdb.dao.AccountDAO;

import java.util.Optional;

/**
 * Provides functionality for authenticating users.
 */
public class UserAuthenticator implements Authenticator<BasicCredentials, User> {
    private final AccountDAO accountDAO;

    /**
     * Instantiate a UserAuthenticator.
     * @param accountDAO AccountDatabaseAccess The database access object for accounts.
     */
    public UserAuthenticator(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Determine if the provided credentials are valid in authenticating a user.
     * Returns Optional.empty() if invalid.
     * @param credentials BasicCredentials The provided credentials.
     * @return Optional<User>
     * @throws AuthenticationException
     */
    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (accountDAO.validate(credentials.getUsername(), credentials.getPassword())) {
            return Optional.of(new User(credentials.getUsername()));
        }
        return Optional.empty();
    }
}
