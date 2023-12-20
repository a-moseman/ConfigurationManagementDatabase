package org.amoseman.cmdb.dao;

/**
 * Interface for a data access object for accounts.
 */
public interface AccountDAO {
    /**
     * Add a new account.
     * @param account String The name of the account.
     * @param password String The password of the account.
     */
    void addAccount(String account, String password);

    /**
     * Remove an account.
     * @param account String The name of the account.
     */
    void deleteAccount(String account);
}
