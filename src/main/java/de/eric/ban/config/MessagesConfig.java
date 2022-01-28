package de.eric.ban.config;

import net.md_5.bungee.config.Configuration;

public class MessagesConfig extends AConfig {

    public MessagesConfig(String configName) {
        super(configName);
    }

    @Override
    void setDefaults() {
        Configuration configuration = getCurrentConfiguration();
        configuration.set("messages.prefix", "&8[&6Ban&8] &7");
        configuration.set("messages.banmessage", "%prefix% Der Spieler &e%player% &7wurde für &e%reason% &e%time% &7gebannt.");
        configuration.set("messages.unbanmessage", "%prefix% Der Spieler &e%player% &7wurde entbannt.");
        configuration.set("messages.time", "%days% Tage %hours% Stunden %minutes% Minuten %seconds% Sekunden");
        configuration.set("messages.notallowed", "%prefix% &cDu darfst diesen Command &4&lnicht &causführen.");
        configuration.set("messages.playerbanned", "%prefix% Der Spieler %player% ist bereits gebannt.");
        configuration.set("messages.playernotbanned", "%prefix% Der Spieler %player% ist nicht gebannt.");
        configuration.set("messages.wrongusage", "%prefix% &cBitte benutze %command%");
        configuration.set("messages.error", "%prefix% &cEs ist ein Fehler aufgetreten");
        configuration.set("messages.connectavoid.firstline", "&cDu &e%time% &cgebannt.");
        configuration.set("messages.connectavoid.secondline", "&cGrund: &e%reason%");
        save();
    }
}
