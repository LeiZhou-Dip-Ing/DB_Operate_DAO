package org.example.Connector;

import com.google.inject.Inject;
import org.example.Config.IDatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DynamicDatabaseConnector implements IDatabaseConnector {
    private final IDatabaseConfig config;

    @Inject
    public DynamicDatabaseConnector(IDatabaseConfig config) {
        this.config = config;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    }
}
