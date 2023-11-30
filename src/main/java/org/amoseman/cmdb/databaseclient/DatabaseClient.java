package org.amoseman.cmdb.databaseclient;

import java.util.Optional;

public interface DatabaseClient {
    Optional<String> read(String account, String label);
    void write(String account, String label, String value);
}