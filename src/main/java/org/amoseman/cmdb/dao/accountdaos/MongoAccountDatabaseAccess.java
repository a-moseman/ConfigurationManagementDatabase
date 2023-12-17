package org.amoseman.cmdb.dao.accountdaos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.amoseman.cmdb.dao.AccountDatabaseAccess;
import org.amoseman.cmdb.databaseclient.databaseclients.MongoDatabaseClient;
import org.amoseman.cmdb.security.PasswordHasher;
import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.mongodb.client.model.Filters.eq;

public class MongoAccountDatabaseAccess extends AccountDatabaseAccess {
    private static final String COLLECTION_NAME = "ACCOUNTS";
    private static final String ACCOUNT_KEY = "account";
    private static final String HASH_KEY = "hash";
    private static final String SALT_KEY = "salt";

    private final MongoDatabase database;
    private final PasswordHasher passwordHasher;
    private MongoCollection<Document> collection;

    public MongoAccountDatabaseAccess(MongoDatabaseClient client) {
        super(client);
        this.database = client.getDatabase();
        this.passwordHasher = new PasswordHasher();
        this.collection = database.getCollection(COLLECTION_NAME);
    }

    @Override
    public void addAccount(String account, String password) {
        if (collection.find(eq(ACCOUNT_KEY, account)).first() != null) {
            return;
        }
        byte[] salt = passwordHasher.generateSalt();
        String hash64 = passwordHasher.generate(password, salt);
        String salt64 = passwordHasher.asBase64(salt);
        Document hashDocument = new Document()
                .append(ACCOUNT_KEY, account)
                .append(HASH_KEY, hash64)
                .append(SALT_KEY, salt64);
        collection.insertOne(hashDocument);
    }

    @Override
    public void deleteAccount(String account) {
        collection.findOneAndDelete(eq(ACCOUNT_KEY, account));
    }

    @Override
    public boolean validate(String account, String password) {
        Document document = collection.find(eq(ACCOUNT_KEY, account)).first();
        if (document == null) {
            return false;
        }
        String hashString = document.getString(HASH_KEY);
        String saltString = document.getString(SALT_KEY);
        if (hashString == null || saltString == null) {
            return false;
        }
        return passwordHasher.validate(password, hashString, saltString);
    }
}
