package org.amoseman.cmdb.application;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.amoseman.cmdb.security.AccountValidator;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.application.authentication.UserAuthenticator;
import org.amoseman.cmdb.security.accountvalidators.MongoAccountValidator;
import org.amoseman.cmdb.security.accountvalidators.RedisAccountValidator;
import org.amoseman.cmdb.application.configuration.ApplicationConfiguration;
import org.amoseman.cmdb.application.resources.AccountResource;
import org.amoseman.cmdb.application.resources.ConfigurationResource;
import org.amoseman.cmdb.dao.AccountDAO;
import org.amoseman.cmdb.dao.ConfigurationDAO;
import org.amoseman.cmdb.dao.accountdaos.MongoAccountDAO;
import org.amoseman.cmdb.dao.accountdaos.RedisAccountDAO;
import org.amoseman.cmdb.dao.configurationdaos.MongoConfigurationDAO;
import org.amoseman.cmdb.dao.configurationdaos.RedisConfigurationDAO;
import org.amoseman.cmdb.databaseclient.databaseclients.MongoDatabaseClient;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;

import java.util.concurrent.TimeUnit;

/**
 * The configuration management database (CMDB) application.
 */
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
        ConfigurationDAO configurationDAO;
        AccountDAO accountDAO;
        AccountValidator accountValidator;
        String type = configuration.getDatabaseType();
        String connectionString = configuration.getDatabaseConnectionString();
        switch (type) {
            case "REDIS" -> {
                RedisDatabaseClient client = new RedisDatabaseClient(connectionString);
                configurationDAO = new RedisConfigurationDAO(client);
                accountDAO =  new RedisAccountDAO(client);
                accountValidator = new RedisAccountValidator(client);
            }
            case "MONGO" -> {
                MongoDatabaseClient client = new MongoDatabaseClient(connectionString);
                configurationDAO = new MongoConfigurationDAO(client);
                accountDAO = new MongoAccountDAO(client);
                accountValidator = new MongoAccountValidator(client);
            }
            default -> throw new RuntimeException("Invalid database type");
        };

        MetricRegistry metrics = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(60, TimeUnit.SECONDS);

        ConfigurationResource configurationResource = new ConfigurationResource(configurationDAO, configuration.getDefaultValue(), metrics);
        environment.jersey().register(configurationResource);

        AccountResource accountResource = new AccountResource(accountDAO, metrics);
        environment.jersey().register(accountResource);

        // security
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new UserAuthenticator(accountValidator))
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()
        ));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }
}