package org.example.Connector;

import org.example.Config.IDatabaseConfig;

public class DynamicDatabaseConnectorFactory implements IDatabaseConnectorFactory{
    @Override
    public IDatabaseConnector createConnector(IDatabaseConfig config, DatabaseType dbType) {
        switch (dbType) {
            case DERBY:
                return new DynamicDatabaseConnector(config); // Derby connector
            case SQLSERVER:
                return null; // SQL Server connector
            case MYSQL:
                return null; // MYSQL connector
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }
}
