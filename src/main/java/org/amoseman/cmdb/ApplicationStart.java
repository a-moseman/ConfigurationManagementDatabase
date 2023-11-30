package org.amoseman.cmdb;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.amoseman.cmdb.application.ApplcationConfig;
import org.amoseman.cmdb.application.ApplicationResource;
import org.amoseman.cmdb.databaseclient.DatabaseClient;
import org.amoseman.cmdb.databaseclient.databaseclients.RedisDatabaseClient;

public class ApplicationStart extends Application<ApplcationConfig>  {
    public static void main(String[] args) throws Exception {
        new ApplicationStart().run(args);
    }

    @Override
    public String getName() {
        return "cmdb";
    }

    @Override
    public void initialize(Bootstrap<ApplcationConfig> bootstrap) {

    }

    @Override
    public void run(ApplcationConfig configuration, Environment environment) {
        DatabaseClient client = new RedisDatabaseClient(configuration.getDatabaseAddress());
        ApplicationResource resource = new ApplicationResource(client, configuration.getDefaultValue());
        environment.jersey().register(resource);
    }
}