package org.amoseman.cmdb;

import org.amoseman.cmdb.application.ConfigurationManagementDatabase;

public class Main {
    public static void main(String[] args) throws Exception {
        new ConfigurationManagementDatabase().run(args);
    }
}
