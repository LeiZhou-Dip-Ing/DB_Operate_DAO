package org.example.Connector;

import com.google.inject.Inject;
import org.example.Config.DatabaseConfigFactoryProvider;
import org.example.Config.IDatabaseConfig;
import org.example.Config.IDatabaseConfigFactory;

public class DynamicDatabaseConnectorFactory implements IDatabaseConnectorFactory{
    private final IDatabaseConnector databaseConnector;

    @Inject
    public DynamicDatabaseConnectorFactory(IDatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public IDatabaseConnector createConnector(DatabaseType dbType, String configFileName, String configType) {
        IDatabaseConfigFactory configFactory = DatabaseConfigFactoryProvider.getFactory(configType);
        IDatabaseConfig config = configFactory.createConfig(configFileName, dbType);
        databaseConnector.configure(config);
        return  databaseConnector;
    }
}
