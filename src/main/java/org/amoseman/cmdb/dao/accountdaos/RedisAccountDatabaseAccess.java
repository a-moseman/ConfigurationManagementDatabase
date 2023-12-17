package org.amoseman.cmdb.dao.accountdaos;

import org.amoseman.cmdb.dao.AccountDatabaseAccess;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;
import org.amoseman.cmdb.security.PasswordHasher;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

public class RedisAccountDatabaseAccess extends AccountDatabaseAccess {
    private final RedissonClient database;
    private final PasswordHasher passwordHasher;

    public RedisAccountDatabaseAccess(RedisDatabaseClient client) {
        super(client);
        this.database = client.getDatabase();
        this.passwordHasher = new PasswordHasher();
    }

    @Override
    public void addAccount(String account, String password) {
        RMap<String, String> hashes = database.getMap("PASSWORDS");
        RMap<String, String> salts = database.getMap("SALTS");
        byte[] salt = passwordHasher.generateSalt();
        String hash64 = passwordHasher.generate(password, salt);
        String salt64 = passwordHasher.asBase64(salt);
        if (hashes.get(account) != null || salts.get(account) != null) {
            return;
        }
        hashes.put(account, hash64);
        salts.put(account, salt64);
    }

    @Override
    public void deleteAccount(String account) {
        RMap<String, String> hashes = database.getMap("PASSWORDS");
        RMap<String, String> salts = database.getMap("SALTS");
        if (!hashes.containsKey(account) || !salts.containsKey(account)) {
            return;
        }
        hashes.remove(account);
        salts.remove(account);
    }

    @Override
    public boolean validate(String account, String password) {
        RMap<String, String> hashes = database.getMap("PASSWORDS");
        RMap<String, String> salts = database.getMap("SALTS");
        String hashString = hashes.get(account);
        String saltString = salts.get(account);
        if (hashString == null || saltString == null) {
            return false;
        }
        return passwordHasher.validate(password, hashes.get(account), salts.get(account));
    }
}
