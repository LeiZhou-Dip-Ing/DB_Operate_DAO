package org.example.Connector;

import java.sql.SQLException;

public interface IDatabaseConnectorFactory {
    IDatabaseConnector createConnector(DatabaseType dbType, String configFileName, String configType)throws SQLException, ClassNotFoundException ;
}
