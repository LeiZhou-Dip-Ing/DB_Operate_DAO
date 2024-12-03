package org.example.Config;

import org.example.Connector.DatabaseType;

public class JsonDatabaseConfigFactory implements IDatabaseConfigFactory{
    @Override
    public IDatabaseConfig createConfig(String configFileName, DatabaseType dbType) {
        throw new UnsupportedOperationException("The method JsonDatabaseConfig() is not implemented yet.");
    }
}
