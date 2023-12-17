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
        byte[] hash = passwordHasher.generate(password);
        if (hashes.get(account) != null) {
            return;
        }
        hashes.put(account, new String(hash));
    }

    @Override
    public void deleteAccount(String account) {
        RMap<String, String> hashes = database.getMap("PASSWORDS");
        hashes.remove(account);
    }

    @Override
    public boolean validate(String account, String password) {
        RMap<String, String> hashes = database.getMap("PASSWORDS");
        return passwordHasher.validate(password, hashes.get(account));
    }
}
