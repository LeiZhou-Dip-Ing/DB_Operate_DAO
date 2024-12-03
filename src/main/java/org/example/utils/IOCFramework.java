package org.example.utils;

import com.google.inject.AbstractModule;
import org.example.Config.UserConfig;
import org.example.Connector.DynamicDatabaseConnector;
import org.example.Connector.DynamicDatabaseConnectorFactory;
import org.example.Connector.IDatabaseConnector;
import org.example.Connector.IDatabaseConnectorFactory;
import org.example.DAO.IKundenDAO;
import org.example.DAO.KundenDAO;

public class IOCFramework extends AbstractModule {
    @Override
    protected void configure() {
        // Bind UserConfigProvider to the UserConfig class
        bind(UserConfig.class).toInstance(new UserConfig("propertiesDbConfig.properties", "PROPERTIES"));

        // Bind IKundenDAO to its implementation
        bind(IKundenDAO.class).to(KundenDAO.class);

        // Bind database connector factory
        bind(IDatabaseConnectorFactory.class).to(DynamicDatabaseConnectorFactory.class);

        // Bind database connector
        bind(IDatabaseConnector.class).to(DynamicDatabaseConnector.class);
    }
}
