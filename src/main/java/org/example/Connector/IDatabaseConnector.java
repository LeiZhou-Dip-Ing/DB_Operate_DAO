package org.example.Connector;

import org.example.Config.IDatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseConnector {
    Connection getConnection() throws SQLException;

    void configure(IDatabaseConfig config);
}

