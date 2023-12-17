package org.amoseman.cmdb.databaseclient.databaseclients;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.amoseman.cmdb.databaseclient.DatabaseClient;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;

public class MongoDatabaseClient implements DatabaseClient {
    private static final String MONGO_DATABASE_NAME = "cmdb";
    private MongoClient client;
    private MongoDatabase database;

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
}
