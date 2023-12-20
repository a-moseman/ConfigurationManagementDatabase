package org.amoseman.cmdb.dao.accountdaos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.amoseman.cmdb.dao.AccountDAO;
import org.amoseman.cmdb.databaseclient.databaseclients.MongoDatabaseClient;
import org.amoseman.cmdb.security.PasswordHasher;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class MongoAccountDAO implements AccountDAO {
    private static final String COLLECTION_NAME = "ACCOUNTS";
    private static final String ACCOUNT_KEY = "account";
    private static final String HASH_KEY = "hash";
    private static final String SALT_KEY = "salt";

    private final MongoDatabase database;
    private final PasswordHasher passwordHasher;
    private final MongoCollection<Document> collection;

    public MongoAccountDAO(MongoDatabaseClient client) {
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
}
