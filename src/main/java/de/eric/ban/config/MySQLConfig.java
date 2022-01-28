package de.eric.ban.config;

import net.md_5.bungee.config.Configuration;

public class MySQLConfig extends AConfig {

    public MySQLConfig(String configName) {
        super(configName);
    }

    @Override
    public void setDefaults() {
        Configuration configuration = getCurrentConfiguration();
        configuration.set("mysql.host", "localhost");
        configuration.set("mysql.user", "root");
        configuration.set("mysql.password", "password");
        configuration.set("mysql.database", "database");
        configuration.set("mysql.port", "3306");
        save();
    }
}
