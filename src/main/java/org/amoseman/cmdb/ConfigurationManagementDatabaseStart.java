package org.amoseman.cmdb;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.amoseman.cmdb.application.ConfigurationManagementDatabaseConfiguration;
import org.amoseman.cmdb.application.resources.ConfigurationResource;
import org.amoseman.cmdb.databaseclient.DatabaseClient;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;

public class ConfigurationManagementDatabaseStart extends Application<ConfigurationManagementDatabaseConfiguration>  {
    @Override
    public String getName() {
        return "cmdb";
    }

    @Override
    public void initialize(Bootstrap<ConfigurationManagementDatabaseConfiguration> bootstrap) {

    }

    @Override
    public void run(ConfigurationManagementDatabaseConfiguration configuration, Environment environment) {
        DatabaseClient client = new RedisDatabaseClient(configuration.getDatabaseAddress());
        ConfigurationResource resource = new ConfigurationResource(client, configuration.getDefaultValue());
        environment.jersey().register(resource);
    }
}