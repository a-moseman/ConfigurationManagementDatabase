package org.amoseman.cmdb.dao.configurationdaos;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.amoseman.cmdb.dao.ConfigurationDatabaseAccess;
import org.amoseman.cmdb.databaseclient.databaseclients.MongoDatabaseClient;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class MongoConfigurationDatabaseAccess extends ConfigurationDatabaseAccess {
    private final MongoDatabase database;

    public MongoConfigurationDatabaseAccess(MongoDatabaseClient client) {
        super(client);
        this.database = client.getDatabase();
    }

    @Override
    public Optional<String> getConfigurationValue(String account, String label) {
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
    public void setConfigurationValue(String account, String label, String value) {
        BasicDBObject search = new BasicDBObject().append("label", label);
        BasicDBObject update = new BasicDBObject().append("$set",
                new BasicDBObject("value", value)
        );
        database.getCollection(account).updateOne(search, update);
    }

    @Override
    public void addConfigurationValue(String account, String label, String value) {
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
    public void removeConfigurationValue(String account, String label) {
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
