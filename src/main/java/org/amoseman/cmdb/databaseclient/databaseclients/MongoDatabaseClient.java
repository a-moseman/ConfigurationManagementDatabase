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
    public Optional<String> read(String account, String label) {
        Optional<MongoCollection<Document>> maybe = getCollection(account);
        if (maybe.isEmpty()) {
            return Optional.empty();
        }
        MongoCollection<Document> collection = maybe.get();
        Document document = collection.find(eq("label", label)).first();
        if (document == null || document.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of((String) document.get("value"));
    }

    @Override
    public void create(String account, String label, String value) {
        Optional<MongoCollection<Document>> maybe = getCollection(account);
        MongoCollection<Document> collection;
        if (maybe.isEmpty()) {
            database.createCollection(account);
            collection = database.getCollection(account);
        }
        else {
            collection = maybe.get();
        }
        Document existing = collection.find(eq("label", label)).first();
        if (existing != null) {
            return;
        }
        Document document = new Document();
        document.append("label", label);
        document.append("value", value);
        collection.insertOne(document);
    }

    @Override
    public void update(String account, String label, String value) {
        BasicDBObject search = new BasicDBObject().append("label", label);
        BasicDBObject update = new BasicDBObject().append("$set",
                new BasicDBObject("value", value)
                );
        database.getCollection(account).updateOne(search, update);
    }

    @Override
    public void delete(String account, String label) {
        if (collectionMissing(account)) {
            return;
        }
        MongoCollection<Document> collection = database.getCollection(account);
        Document document = collection.find(eq("label", label)).first();
        if (document == null) {
            return;
        }
        collection.deleteOne(document);
    }

    private Optional<MongoCollection<Document>> getCollection(String account) {
        if (collectionMissing(account)) {
            return Optional.empty();
        }
        return Optional.of(database.getCollection(account));
    }

    private boolean collectionMissing(String collectionName) {
        return !database.listCollectionNames().into(new ArrayList<>()).contains(collectionName);
    }
}
