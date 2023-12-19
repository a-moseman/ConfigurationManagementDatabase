package org.amoseman.cmdb.application;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.application.authentication.UserAuthenticator;
import org.amoseman.cmdb.application.configuration.ApplicationConfiguration;
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

public class ConfigurationManagementDatabase extends Application<ApplicationConfiguration>  {
    @Override
    public String getName() {
        return "cmdb";
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
        // todo?
    }

    @Override
    public void run(ApplicationConfiguration configuration, Environment environment) {
        ConfigurationDatabaseAccess configurationDatabaseAccess;
        AccountDatabaseAccess accountDatabaseAccess;
        String type = configuration.getDatabaseType();
        String connectionString = configuration.getDatabaseConnectionString();
        switch (type) {
            case "REDIS" -> {
                RedisDatabaseClient client = new RedisDatabaseClient(connectionString);
                configurationDatabaseAccess = new RedisConfigurationDatabaseAccess(client);
                accountDatabaseAccess =  new RedisAccountDatabaseAccess(client);
            }
            case "MONGO" -> {
                MongoDatabaseClient client = new MongoDatabaseClient(connectionString);
                configurationDatabaseAccess = new MongoConfigurationDatabaseAccess(client);
                accountDatabaseAccess = new MongoAccountDatabaseAccess(client);
            }
            default -> throw new RuntimeException("Invalid database type");
        };


        ConfigurationResource configurationResource = new ConfigurationResource(configurationDatabaseAccess, configuration.getDefaultValue());
        environment.jersey().register(configurationResource);

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
}