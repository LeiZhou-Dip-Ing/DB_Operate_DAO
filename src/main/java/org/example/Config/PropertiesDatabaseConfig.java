package org.example.Config;

import com.google.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesDatabaseConfig implements IDatabaseConfig{
    private final Properties properties;
    @Inject
    public PropertiesDatabaseConfig(String configFileName) {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new RuntimeException("Configuration file not found: " + configFileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    @Override
    public String getUrl() {
        return properties.getProperty("database.url");
    }

    @Override
    public String getUsername() {
        return properties.getProperty("database.username");
    }

    @Override
    public String getPassword() {
        return properties.getProperty("database.password");
    }
}
