package org.amoseman.cmdb.databaseclient;

import java.util.Optional;

public interface DatabaseClient {
    Optional<String> read(String collection, String label);
    void write(String collection, String label, String value);
}