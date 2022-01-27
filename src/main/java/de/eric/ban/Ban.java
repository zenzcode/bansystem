package de.eric.ban;

import de.eric.ban.config.MessagesConfig;
import de.eric.ban.config.MySQLConfig;
import de.eric.ban.helper.MessageHelper;
import de.eric.ban.listener.ConnectListener;
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
        messagesConfig = new MessagesConfig("messages.yaml");
        messageHelper = new MessageHelper();
        messageHelper.setMessages(messagesConfig.getSection("messages"));
        mySQLConfig = new MySQLConfig("mysql.yaml");
        mysql = new MySQL(mySQLConfig.getFromConfig("mysql.host"),
                mySQLConfig.getFromConfig("mysql.database"),
                mySQLConfig.getFromConfig("mysql.user"),
                mySQLConfig.getFromConfig("mysql.password"),
                Integer.parseInt(mySQLConfig.getFromConfig("mysql.port")));
        registerListener();
        registerCommands();
        getProxy().getConsole().sendMessage("§aBansystem aktiviert");
    }

    @Override
    public void onDisable() {
        getProxy().getConsole().sendMessage("§aBansystem deaktiviert");
    }

    private void registerListener(){
        getProxy().getPluginManager().registerListener(this, new ConnectListener());
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

    public MessageHelper getMessageHelper() {
        return messageHelper;
    }
}
