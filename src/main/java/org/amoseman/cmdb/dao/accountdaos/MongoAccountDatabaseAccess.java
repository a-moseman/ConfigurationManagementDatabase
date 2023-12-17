package org.amoseman.cmdb.dao.accountdaos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.amoseman.cmdb.dao.AccountDatabaseAccess;
import org.amoseman.cmdb.databaseclient.databaseclients.MongoDatabaseClient;
import org.amoseman.cmdb.security.PasswordHasher;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class MongoAccountDatabaseAccess extends AccountDatabaseAccess {
    private static final String COLLECTION_NAME = "PASSWORDS";
    private static final String ACCOUNT_KEY = "account";
    private static final String PASSWORD_KEY = "password";

    private final MongoDatabase database;
    private final PasswordHasher passwordHasher;

    public MongoAccountDatabaseAccess(MongoDatabaseClient client) {
        super(client);
        this.database = client.getDatabase();
        this.passwordHasher = new PasswordHasher();
    }

    @Override
    public void addAccount(String account, String password) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        if (collection.find(eq(ACCOUNT_KEY, account)).first() != null) {
            return;
        }
        byte[] hash = passwordHasher.generate(password);
        Document document = new Document().append(ACCOUNT_KEY, account).append(PASSWORD_KEY, new String(hash));
        collection.insertOne(document);
    }

    @Override
    public void deleteAccount(String account) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        collection.findOneAndDelete(eq(ACCOUNT_KEY, account));
    }

    @Override
    public boolean validate(String account, String password) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        Document search = collection.find(eq(ACCOUNT_KEY, account)).first();
        if (search == null) {
            return false;
        }
        String hash = (String) search.get(PASSWORD_KEY);
        if (hash == null) {
            return false;
        }
        return passwordHasher.validate(password, hash);
    }
}
