package org.amoseman.cmdb.dao.configurationdaos;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.amoseman.cmdb.application.configuration.ConfigurationValue;
import org.amoseman.cmdb.dao.ConfigurationDatabaseAccess;
import org.amoseman.cmdb.databaseclient.databaseclients.MongoDatabaseClient;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class MongoConfigurationDatabaseAccess extends ConfigurationDatabaseAccess {
    private static final String COLLECTION_PREFIX = "CONFIGURATIONS";
    private final MongoDatabase database;

    public MongoConfigurationDatabaseAccess(MongoDatabaseClient client) {
        super(client);
        this.database = client.getDatabase();
    }

    @Override
    public Optional<ConfigurationValue> getValue(String account, String label) {
        MongoCollection<Document> collection = getCollection(account);
        Document document = collection.find(eq("label", label)).first();
        if (document == null || document.isEmpty()) {
            return Optional.empty();
        }
        String value = document.getString("value");
        String created = document.getString("created");
        String updated = document.getString("updated");
        return Optional.of(new ConfigurationValue(value, created, updated));
    }

    @Override
    public void setValue(String account, String label, String value) {
        BasicDBObject search = new BasicDBObject().append("label", label);
        LocalDateTime dateTime = LocalDateTime.now();
        BasicDBObject update = new BasicDBObject().append("$set",
                new BasicDBObject().append("value", value).append("updated", dateTime.format(DateTimeFormatter.ISO_DATE_TIME))
        );
        if (collectionMissing(account)) {
            return;
        }
        getCollection(account).updateOne(search, update);
    }

    @Override
    public void addValue(String account, String label, String value) {
        MongoCollection<Document> collection = getCollection(account);
        Document existing = collection.find(eq("label", label)).first();
        if (existing != null) {
            return;
        }
        Document document = new Document();
        document.append("label", label);
        document.append("value", value);
        LocalDateTime dateTime = LocalDateTime.now();
        String dateTimeString = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        document.append("created", dateTimeString);
        document.append("updated", dateTimeString);
        collection.insertOne(document);
    }

    @Override
    public void removeValue(String account, String label) {
        if (collectionMissing(account)) {
            return;
        }
        MongoCollection<Document> collection = getCollection(account);
        Document document = collection.find(eq("label", label)).first();
        if (document == null) {
            return;
        }
        collection.deleteOne(document);
    }

    private MongoCollection<Document> getCollection(String account) {
        return database.getCollection(String.format("%s-%s", COLLECTION_PREFIX, account));
    }

    private boolean collectionMissing(String collectionName) {
        return !database.listCollectionNames().into(new ArrayList<>()).contains(String.format("%s-%s", COLLECTION_PREFIX, collectionName));
    }
}
