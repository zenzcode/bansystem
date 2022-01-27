package de.eric.ban;

import de.eric.ban.config.MessagesConfig;
import de.eric.ban.config.MySQLConfig;
import de.eric.ban.helper.MessageHelper;
import de.eric.ban.sql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

public class Ban extends Plugin {

    //Store instance of ban to access data
    private static Ban instance;

    /**
     *
     * DATABASE
     *
     */
    //Connection to mysql
    private MySQL mysql;
    //File to load mysql data from
    private MySQLConfig mySQLConfig;

    /**
     *
     * Messages
     *
     */
    private MessagesConfig messagesConfig;
    private MessageHelper messageHelper;

    @Override
    public void onEnable() {
        instance = this;
        mySQLConfig = new MySQLConfig("mysql.yaml");
        mysql = new MySQL();
        messagesConfig = new MessagesConfig("messages.yaml");
        messageHelper = new MessageHelper();
        messageHelper.setMessages(messagesConfig.getSection("messages"));
        registerListener();
        registerCommands();
        getProxy().getConsole().sendMessage("§aBansystem aktiviert");
    }

    @Override
    public void onDisable() {
        getProxy().getConsole().sendMessage("§aBansystem deaktiviert");
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

    public MySQLConfig getMySQLConfig() {
        return mySQLConfig;
    }
}
