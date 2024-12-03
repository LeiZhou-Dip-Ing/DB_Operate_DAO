package org.example.Connector;

import org.example.Config.IDatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DynamicDatabaseConnector implements IDatabaseConnector {
    private IDatabaseConfig config;

    public DynamicDatabaseConnector() {}

    @Override
    public Connection getConnection() throws SQLException {
        if (config == null) {
            throw new IllegalStateException("Database configuration is not set.");
        }
        return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    }

    @Override
    public void configure(IDatabaseConfig config) {
        this.config = config;
    }
}
