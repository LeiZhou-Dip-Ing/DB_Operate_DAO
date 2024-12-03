package org.example.Config;

import org.example.Connector.DatabaseType;

public class XmlDatabaseConfigFactory implements IDatabaseConfigFactory{
    @Override
    public IDatabaseConfig createConfig(String configFileName, DatabaseType dbType) {
        throw new UnsupportedOperationException("The method XmlDatabaseConfig is not implemented yet.");
    }
}
