package org.amoseman.cmdb.application.authentication.accountvalidators;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.amoseman.cmdb.application.authentication.AccountValidator;
import org.amoseman.cmdb.databaseclient.databaseclients.MongoDatabaseClient;
import org.amoseman.cmdb.security.PasswordHasher;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class MongoAccountValidator implements AccountValidator {
    private MongoDatabase database;
    private PasswordHasher passwordHasher;
    private MongoCollection<Document> collection;

    public MongoAccountValidator(MongoDatabaseClient mongoDatabaseClient) {
        this.database = mongoDatabaseClient.getDatabase();
        this.passwordHasher = new PasswordHasher();
        this.collection = database.getCollection("ACCOUNTS");
    }

    @Override
    public boolean validate(String account, String password) {
        Document document = collection.find(eq("account", account)).first();
        if (document == null) {
            return false;
        }
        String hashString = document.getString("hash");
        String saltString = document.getString("salt");
        if (hashString == null || saltString == null) {
            return false;
        }
        return passwordHasher.validate(password, hashString, saltString);
    }
}
