package org.amoseman.cmdb.application;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.application.authentication.UserAuthenticator;
import org.amoseman.cmdb.application.configuration.ConfigurationManagementDatabaseConfiguration;
import org.amoseman.cmdb.application.resources.AccountResource;
import org.amoseman.cmdb.application.resources.ConfigurationResource;
import org.amoseman.cmdb.dao.AccountDatabaseAccess;
import org.amoseman.cmdb.dao.ConfigurationDatabaseAccess;
import org.amoseman.cmdb.dao.accountdaos.MongoAccountDatabaseAccess;
import org.amoseman.cmdb.dao.accountdaos.RedisAccountDatabaseAccess;
import org.amoseman.cmdb.dao.configurationdaos.MongoConfigurationDatabaseAccess;
import org.amoseman.cmdb.dao.configurationdaos.RedisConfigurationDatabaseAccess;
import org.amoseman.cmdb.databaseclient.databaseclients.MongoDatabaseClient;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;

public class ConfigurationManagementDatabase extends Application<ConfigurationManagementDatabaseConfiguration>  {
    @Override
    public String getName() {
        return "cmdb";
    }

    @Override
    public void initialize(Bootstrap<ConfigurationManagementDatabaseConfiguration> bootstrap) {
        // todo?
    }

    @Override
    public void run(ConfigurationManagementDatabaseConfiguration configuration, Environment environment) {
        ConfigurationDatabaseAccess configurationDatabaseAccess = getConfigurationDatabaseAccess(configuration);
        ConfigurationResource configurationResource = new ConfigurationResource(configurationDatabaseAccess, configuration.getDefaultValue());
        environment.jersey().register(configurationResource);

        AccountDatabaseAccess accountDatabaseAccess = getAccountDatabaseAccess(configuration);
        AccountResource accountResource = new AccountResource(accountDatabaseAccess);
        environment.jersey().register(accountResource);

        // security
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new UserAuthenticator(accountDatabaseAccess))
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()
        ));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

    private ConfigurationDatabaseAccess getConfigurationDatabaseAccess(ConfigurationManagementDatabaseConfiguration configuration) {
        String databaseType = configuration.getDatabaseType();
        String databaseAddress = configuration.getDatabaseAddress();
        return switch (databaseType) {
            case "REDIS" -> {
                RedisDatabaseClient redisClient = new RedisDatabaseClient(databaseAddress);
                yield new RedisConfigurationDatabaseAccess(redisClient);
            }
            case "MONGO" -> {
                MongoDatabaseClient mongoClient = new MongoDatabaseClient(databaseAddress);
                yield new MongoConfigurationDatabaseAccess(mongoClient);
            }
            default -> throw new RuntimeException("Invalid database type");
        };
    }

    private AccountDatabaseAccess getAccountDatabaseAccess(ConfigurationManagementDatabaseConfiguration configuration) {
        String databaseType = configuration.getDatabaseType();
        String databaseAddress = configuration.getDatabaseAddress();
        return switch (databaseType) {
            case "REDIS" -> {
                RedisDatabaseClient redisClient = new RedisDatabaseClient(databaseAddress);
                yield new RedisAccountDatabaseAccess(redisClient);
            }
            case "MONGO" -> {
                MongoDatabaseClient mongoClient = new MongoDatabaseClient(databaseAddress);
                yield new MongoAccountDatabaseAccess(mongoClient);
            }
            default -> throw new RuntimeException("Invalid database type");
        };
    }
}