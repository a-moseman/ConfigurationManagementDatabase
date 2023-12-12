package org.amoseman.cmdb.application.authentication;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Set;

public class User implements Principal {
    private final String name;
    private final Set<String> roles;

    public User(String name) {
        this.name = name;
        this.roles = null;
    }

    @Override
    public String getName() {
        return name;
    }

    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
