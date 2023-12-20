package org.amoseman.cmdb.databaseclient;

public interface DatabaseClient {
    /**
     * Get the database client provided by the underlying database driver.
     * @return Object
     */
    Object getDatabase();

    /**
     * Close the connection to the database.
     */
    void close();
}