package org.amoseman.cmdb.dao;

import org.amoseman.cmdb.databaseclient.DatabaseClient;

public abstract class AccountDatabaseAccess extends DatabaseAccessObject {
    public AccountDatabaseAccess(DatabaseClient client) {
        super(client);
    }

    public abstract void addAccount(String account, String password);
    public abstract void deleteAccount(String account);
    public abstract boolean validate(String account, String password);
}
