package de.eric.ban.config;

import net.md_5.bungee.config.Configuration;

public class MessagesConfig extends AConfig{

    public MessagesConfig(String configName) {
        super(configName);
        if(!doesFileExist()){
            createFile();
            setDefaults();
        }
    }

    @Override
    void setDefaults() {
        Configuration configuration = getCurrentConfiguration();
        configuration.set("messages.prefix", "&8[&6Ban&8] &7");
        configuration.set("messages.banmessage", "%prefix% Der Spieler &e%player% &7wurde für &e%reason% &7für &e%time% &7gebannt.");
        configuration.set("messages.unbanmessage", "%prefix% Der Spieler &e%player% &7wurde entbannt.");
        configuration.set("messages.timeremain", "%prefix% Der Spieler &e%player% &7ist noch &e%time% &7gebannt.");
        configuration.set("messages.connectavoid.firstline", "&cDu bist noch für &e%time% &cgebannt.");
        configuration.set("messages.connectavoid.secondline", "&cGrund: &e%reason%");
        save();
    }
}
