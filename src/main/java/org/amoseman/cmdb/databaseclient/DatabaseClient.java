package org.amoseman.cmdb.databaseclient;

import java.util.Optional;

public interface DatabaseClient {
    Optional<String> read(String account, String label);
    void create(String account, String label, String value);
    void update(String account, String label, String value);
    void delete(String account, String label);
}