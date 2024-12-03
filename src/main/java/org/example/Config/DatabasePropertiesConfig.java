package org.example.Config;

import org.example.Connector.DatabaseType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabasePropertiesConfig implements IDatabaseConfig{
    private final String url;
    private final String username;
    private final String password;

    public DatabasePropertiesConfig(String configFileName, DatabaseType dbType) {
        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (inputStream == null) {
                throw new RuntimeException("Configuration file not found: " + configFileName);
            }
            properties.load(inputStream);

            // Load the appropriate configuration items for each database type.
            switch (dbType) {
                case MYSQL:
                    this.url = properties.getProperty("mysql.db.url");
                    this.username = properties.getProperty("mysql.db.username");
                    this.password = properties.getProperty("mysql.db.password");
                    break;
                case DERBY:
                    this.url = properties.getProperty("derby.db.url");
                    this.username = properties.getProperty("derby.db.username");
                    this.password = properties.getProperty("derby.db.password");
                    break;
                case SQLSERVER:
                    this.url = properties.getProperty("sqlserver.db.url");
                    this.username = properties.getProperty("sqlserver.db.username");
                    this.password = properties.getProperty("sqlserver.db.password");
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported database type: " + dbType);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error loading database configuration from file", e);
        }
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
