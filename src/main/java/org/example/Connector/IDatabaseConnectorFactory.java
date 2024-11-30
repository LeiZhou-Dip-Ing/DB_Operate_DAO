package org.example.Connector;

import org.example.Config.IDatabaseConfig;
public interface IDatabaseConnectorFactory {
    IDatabaseConnector createConnector(IDatabaseConfig config,DatabaseType dbType);
}
