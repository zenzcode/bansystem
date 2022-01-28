package de.eric.ban.config;

import de.eric.ban.Ban;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public abstract class AConfig {
    private File file = null;
    private Optional<Configuration> yamlConfiguration = Optional.empty();
    private boolean hasBeenCreated = false;

    //Constructor creates file and configuration
    public AConfig(String configName) {
        file = new File(Ban.getInstance().getDataFolder(), configName);
        if (!doesFileExist()) {
            createFile();
            hasBeenCreated = true;
        }
        try {
            yamlConfiguration = Optional.of(ConfigurationProvider.getProvider(YamlConfiguration.class).load(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (hasBeenCreated) {
            setDefaults();
        }
    }

    //function to set default values for config on creation
    abstract void setDefaults();

    //function to save config
    public void save() {
        Configuration configuration = getCurrentConfiguration();
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //generic function to return a value from a config
    public <T> T getFromConfig(String name) {
        Configuration configuration = getCurrentConfiguration();
        return (T) configuration.get(name);
    }

    public Configuration getSection(String name) {
        Configuration configuration = getCurrentConfiguration();
        return configuration.getSection(name);
    }

    //check if yaml config is present, constructor could have failed to get it
    Configuration getCurrentConfiguration() {
        return yamlConfiguration.orElse(null);
    }

    boolean doesFileExist() {
        return file.exists();
    }

    //function to create file
    void createFile() {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
