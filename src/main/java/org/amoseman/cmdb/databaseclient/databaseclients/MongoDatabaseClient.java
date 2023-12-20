package org.amoseman.cmdb.databaseclient.databaseclients;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.amoseman.cmdb.databaseclient.DatabaseClient;
import java.util.concurrent.TimeUnit;

/**
 * Provides a connection to a Mongo database.
 */
public class MongoDatabaseClient implements DatabaseClient {
    private static final String MONGO_DATABASE_NAME = "cmdb";
    private final MongoClient client;
    private final MongoDatabase database;

    /**
     * Instantiate an instance of MongoDatabaseClient and establish a connection to the Mongo database using the provided connection string.
     * @param connectionString String
     */
    public MongoDatabaseClient(String connectionString) {
        this.client = MongoClients.create(MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(connectionString))
                        .applyToConnectionPoolSettings(builder ->
                            builder.maxWaitTime(10, TimeUnit.SECONDS)
                                    .maxSize(10)
                                    .maxConnectionIdleTime(60, TimeUnit.SECONDS)
                                    .maxConnectionLifeTime(60, TimeUnit.SECONDS)
                        )
                .build());
        this.database = client.getDatabase(MONGO_DATABASE_NAME);
    }

    @Override
    public MongoDatabase getDatabase() {
        return database;
    }

    @Override
    public void close() {
        this.client.close();
    }
}
