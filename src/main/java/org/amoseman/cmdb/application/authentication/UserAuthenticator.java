package org.amoseman.cmdb.application.authentication;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.amoseman.cmdb.dao.AccountDatabaseAccess;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UserAuthenticator implements Authenticator<BasicCredentials, User> {
    private final AccountDatabaseAccess accountDatabaseAccess;

    public UserAuthenticator(AccountDatabaseAccess accountDatabaseAccess) {
        this.accountDatabaseAccess = accountDatabaseAccess;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (accountDatabaseAccess.validate(credentials.getUsername(), credentials.getPassword())) {
            return Optional.of(new User(credentials.getUsername()));
        }
        return Optional.empty();
    }
}
