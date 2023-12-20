package org.amoseman.cmdb.application.authentication;

/**
 * Interface for account validation.
 */
public interface AccountValidator {
    /**
     * Determine if the password attempt is valid for the account.
     * @param account String The name of the account.
     * @param password String The password attempt.
     * @return boolean
     */
    boolean validate(String account, String password);
}
