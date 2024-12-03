package org.example.Config;

public class UserConfig {
    private final String configFileName;
    private final String configType;

    public UserConfig(String configFileName, String configType) {
        this.configFileName = configFileName;
        this.configType = configType;
    }

    // Getters
    public String getConfigFileName() {
        return configFileName;
    }

    public String getConfigType() {
        return configType;
    }
}
