package org.amoseman.cmdb.application.authentication;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UserAuthenticator implements Authenticator<BasicCredentials, User> {
    private final Map<String, String> PASSWORDS;

    public UserAuthenticator(Map<String, String> passwords) {
        this.PASSWORDS = passwords;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (isUser(credentials) && validPassword(credentials)) {
            return Optional.of(new User(credentials.getUsername()));
        }
        return Optional.empty();
    }

    private boolean isUser(BasicCredentials credentials) {
        return PASSWORDS.containsKey(credentials.getUsername());
    }

    private boolean validPassword(BasicCredentials credentials) {
        return PASSWORDS.get(credentials.getUsername()).equals(credentials.getPassword());
    }
}
