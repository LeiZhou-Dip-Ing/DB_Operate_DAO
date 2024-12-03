package org.example.Config;

import java.util.HashMap;
import java.util.Map;

public class DatabaseConfigFactoryProvider {
    private static final Map<String, IDatabaseConfigFactory> factoryMap = new HashMap<>();
    static {
        // Registering factories for supported profile types
        factoryMap.put("PROPERTIES", new PropertiesDatabaseConfigFactory());
        factoryMap.put("JSON", new JsonDatabaseConfigFactory());
        factoryMap.put("XML", new XmlDatabaseConfigFactory());
    }

    // Returns the corresponding configuration factory based on the file type
    public static IDatabaseConfigFactory getFactory(String configType) {
        IDatabaseConfigFactory factory = factoryMap.get(configType.toUpperCase());
        if (factory == null) {
            throw new IllegalArgumentException("Unsupported config type: " + configType);
        }
        return factory;
    }
}
