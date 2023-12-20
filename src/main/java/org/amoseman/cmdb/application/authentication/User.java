package org.amoseman.cmdb.application.authentication;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * Represents a authenticated user.
 */
public class User implements Principal {
    private final String name;

    /**
     * Instiate a User.
     * @param name String The name of the user.
     */
    public User(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
