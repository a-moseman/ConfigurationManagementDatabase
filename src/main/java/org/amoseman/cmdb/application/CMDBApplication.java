package org.amoseman.cmdb.application;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.amoseman.cmdb.AccountLoader;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.application.authentication.UserAuthenticator;
import org.amoseman.cmdb.application.configuration.ConfigurationManagementDatabaseConfiguration;
import org.amoseman.cmdb.application.resources.ConfigurationResource;
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

    }

    @Override
    public void run(ConfigurationManagementDatabaseConfiguration configuration, Environment environment) {
        String databaseType = configuration.getDatabaseType();
        DatabaseClient client = switch (databaseType) {
            case "REDIS" -> new RedisDatabaseClient(configuration.getDatabaseAddress());
            case "MONGO" -> new MongoDatabaseClient(configuration.getDatabaseAddress());
            default -> throw new RuntimeException("Invalid database type");
        };
        ConfigurationResource resource = new ConfigurationResource(client, configuration.getDefaultValue());
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
}