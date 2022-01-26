package de.eric.ban;

import de.eric.ban.sql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

public class Ban extends Plugin {

    //Store instance of ban to access data
    private static Ban instance;

    //Connection to mysql
    private MySQL mysql;

    @Override
    public void onEnable() {
        instance = this;
        mysql = new MySQL();
        registerListener();
        registerCommands();
        getProxy().getConsole().sendMessage("$aBansystem aktiviert");
    }

    @Override
    public void onDisable() {
        getProxy().getConsole().sendMessage("$cBansystem deaktiviert");
    }

    private void registerListener(){

    }

    private void registerCommands(){

    }

    /***
     *
     * Getters and setters
     *
     */
    public static Ban getInstance(){
        return instance;
    }

    public MySQL getMysql() {
        return mysql;
    }
}
