package de.eric.ban;

import de.eric.ban.commands.BanCommand;
import de.eric.ban.commands.UnbanCommand;
import de.eric.ban.config.MessagesConfig;
import de.eric.ban.config.MySQLConfig;
import de.eric.ban.helper.BanHelper;
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

    /**
     *
     * Ban Helper
     *
     */
    private BanHelper banHelper;

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
        banHelper = new BanHelper();
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
        getProxy().getPluginManager().registerCommand(this, new BanCommand("ban", "ban.ban",
                "punish"));
        getProxy().getPluginManager().registerCommand(this, new UnbanCommand("unban", "ban.unban",
                "pardon"));
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

    public BanHelper getBanHelper() {
        return banHelper;
    }
}
