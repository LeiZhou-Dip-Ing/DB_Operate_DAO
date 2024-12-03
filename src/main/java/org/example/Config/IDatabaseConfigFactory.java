package org.example.Config;

import org.example.Connector.DatabaseType;

public interface IDatabaseConfigFactory {
    IDatabaseConfig createConfig(String configFileName, DatabaseType dbType);
}
