package org.amoseman.cmdb.security.accountvalidators;

import org.amoseman.cmdb.security.AccountValidator;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;
import org.amoseman.cmdb.security.PasswordHasher;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

public class RedisAccountValidator implements AccountValidator {
    private RedissonClient database;
    private PasswordHasher passwordHasher;

    public RedisAccountValidator(RedisDatabaseClient client) {
        this.database = client.getDatabase();
        this.passwordHasher = new PasswordHasher();
    }


    @Override
    public boolean validate(String account, String password) {
        RMap<String, String> hashes = database.getMap("HASHES");
        RMap<String, String> salts = database.getMap("SALTS");
        String hashString = hashes.get(account);
        String saltString = salts.get(account);
        if (hashString == null || saltString == null) {
            return false;
        }
        return passwordHasher.validate(password, hashes.get(account), salts.get(account));    }
}
