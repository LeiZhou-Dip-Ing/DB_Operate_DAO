package org.example.utils;

import com.google.inject.AbstractModule;
import org.example.Config.IDatabaseConfig;
import org.example.Config.PropertiesDatabaseConfig;
import org.example.Connector.DynamicDatabaseConnector;
import org.example.Connector.DynamicDatabaseConnectorFactory;
import org.example.Connector.IDatabaseConnector;
import org.example.Connector.IDatabaseConnectorFactory;
import org.example.DAO.IKundenDAO;
import org.example.DAO.KundenDAO;

public class IOCFramework extends AbstractModule {
    @Override
    protected void configure() {
        // Bind the configuration file name to "database.properties"
        bind(IDatabaseConfig.class).toProvider(() -> new PropertiesDatabaseConfig("dbconfig.properties"));

        // Bind IKundenDAO to its implementation
        bind(IKundenDAO.class).to(KundenDAO.class);

        // Bind database connector factory
        bind(IDatabaseConnectorFactory.class).to(DynamicDatabaseConnectorFactory.class);

        // Bind database connector
        bind(IDatabaseConnector.class).to(DynamicDatabaseConnector.class);
    }
}
