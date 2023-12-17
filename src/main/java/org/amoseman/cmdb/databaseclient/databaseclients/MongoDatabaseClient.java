package org.amoseman.cmdb.databaseclient.databaseclients;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.amoseman.cmdb.databaseclient.DatabaseClient;
import java.util.concurrent.TimeUnit;

public class MongoDatabaseClient implements DatabaseClient {
    private static final String MONGO_DATABASE_NAME = "cmdb";
    private final MongoClient client;
    private final MongoDatabase database;

    public MongoDatabaseClient(String address) {
        String connectionString = String.format("mongodb://%s", address);
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
