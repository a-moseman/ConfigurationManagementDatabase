package org.amoseman.cmdb.application.authentication;

public interface AccountValidator {
    boolean validate(String account, String password);
}
