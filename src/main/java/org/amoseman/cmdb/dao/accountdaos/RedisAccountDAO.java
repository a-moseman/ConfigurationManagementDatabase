package org.amoseman.cmdb.dao.accountdaos;

import org.amoseman.cmdb.dao.AccountDAO;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;
import org.amoseman.cmdb.security.PasswordHasher;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

public class RedisAccountDAO implements AccountDAO {
    private final RedissonClient database;
    private final PasswordHasher passwordHasher;

    public RedisAccountDAO(RedisDatabaseClient client) {
        this.database = client.getDatabase();
        this.passwordHasher = new PasswordHasher();
    }

    @Override
    public boolean addAccount(String account, String password) {
        RMap<String, String> hashes = database.getMap("HASHES");
        RMap<String, String> salts = database.getMap("SALTS");
        if (hashes.get(account) != null || salts.get(account) != null) {
            return false;
        }
        byte[] salt = passwordHasher.generateSalt();
        String hash64 = passwordHasher.generate(password, salt);
        String salt64 = passwordHasher.asBase64(salt);
        hashes.put(account, hash64);
        salts.put(account, salt64);
        return true;
    }

    @Override
    public boolean deleteAccount(String account) {
        RMap<String, String> hashes = database.getMap("HASHES");
        RMap<String, String> salts = database.getMap("SALTS");
        if (!hashes.containsKey(account) || !salts.containsKey(account)) {
            return false;
        }
        hashes.remove(account);
        salts.remove(account);
        return true;
    }
}
