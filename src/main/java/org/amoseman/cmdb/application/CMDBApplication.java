package org.amoseman.cmdb.application;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.core.Application;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.amoseman.cmdb.AccountLoader;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.application.authentication.UserAuthenticator;
import org.amoseman.cmdb.application.configuration.ConfigurationManagementDatabaseConfiguration;
import org.amoseman.cmdb.application.resources.ConfigurationResource;
import org.amoseman.cmdb.dao.ConfigurationDatabaseAccess;
import org.amoseman.cmdb.dao.configurationdaos.MongoConfigurationDatabaseAccess;
import org.amoseman.cmdb.dao.configurationdaos.RedisConfigurationDatabaseAccess;
import org.amoseman.cmdb.databaseclient.DatabaseClient;
import org.amoseman.cmdb.databaseclient.databaseclients.MongoDatabaseClient;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;
import java.util.Map;

public class CMDBApplication extends Application<ConfigurationManagementDatabaseConfiguration>  {
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
        ConfigurationResource resource = new ConfigurationResource(configurationDatabaseAccess, configuration.getDefaultValue());
        environment.jersey().register(resource);

        // security
        Map<String, String> passwords = AccountLoader.load(configuration.getAccountFilePath());
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new UserAuthenticator(passwords))
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
}