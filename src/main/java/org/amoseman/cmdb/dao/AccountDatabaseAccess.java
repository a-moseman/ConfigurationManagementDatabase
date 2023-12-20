package org.amoseman.cmdb.dao;

import org.amoseman.cmdb.databaseclient.DatabaseClient;

/**
 * Represents a database access object for accounts.
 */
public abstract class AccountDatabaseAccess extends DatabaseAccessObject {
    /**
     * Instantiate an AccountDatabaseAccess.
     * @param client DatabaseClient The connection to the database.
     */
    public AccountDatabaseAccess(DatabaseClient client) {
        super(client);
    }

    /**
     * Add a new account.
     * @param account String The name of the account.
     * @param password String The password of the account.
     */
    public abstract void addAccount(String account, String password);

    /**
     * Remove an account.
     * @param account String The name of the account.
     */
    public abstract void deleteAccount(String account);

    /**
     * Validate that the provided password is the password of the account of the provided account name.
     * @param account String The name of the account.
     * @param password String The password attempt.
     * @return boolean
     */
    public abstract boolean validate(String account, String password);
}
