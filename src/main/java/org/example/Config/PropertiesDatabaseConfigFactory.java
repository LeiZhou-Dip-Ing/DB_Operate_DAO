package org.example.Config;

import org.example.Connector.DatabaseType;

public class PropertiesDatabaseConfigFactory implements IDatabaseConfigFactory{
    @Override
    public IDatabaseConfig createConfig(String configFileName, DatabaseType dbType) {
        return new DatabasePropertiesConfig(configFileName,dbType);
    }
}
