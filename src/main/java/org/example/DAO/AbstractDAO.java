package org.example.DAO;

import org.example.Config.IDatabaseConfig;
import org.example.Connector.DatabaseType;
import org.example.Connector.IDatabaseConnector;
import org.example.Connector.IDatabaseConnectorFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractDAO {
    private final IDatabaseConnectorFactory connectorFactory;
    private final IDatabaseConfig config;

    // Constructor injection factories and configurations
    protected AbstractDAO(IDatabaseConnectorFactory connectorFactory, IDatabaseConfig config) {
        this.connectorFactory = connectorFactory;
        this.config = config;
    }

    // Methods for creating connections, using factories to create database connectors
    protected Connection getConnection(DatabaseType dbType) throws SQLException {
        IDatabaseConnector connector = connectorFactory.createConnector(config, dbType);
        return connector.getConnection();
    }

    // Initialize database tables, or create them if they don't exist
    protected void initializeTable(String tableName, String createTableSQL, DatabaseType dbType) {
        try (Connection connection = getConnection(dbType)) {
            // check if the table exists
            if (!doesTableExist(connection, tableName)) {
                System.out.println(tableName + " table does not exist and is being created...");
                // If the table does not exist, create it
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createTableSQL);
                    System.out.println("The " + tableName + " table has been created.");
                }
            } else {
                System.out.println(tableName + " table already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database table initialization failed", e);
        }
    }

    // check weather table exists
    protected abstract boolean doesTableExist(Connection connection, String tableName) throws SQLException;
}
