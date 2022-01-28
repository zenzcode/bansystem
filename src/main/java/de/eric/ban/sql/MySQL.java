package de.eric.ban.sql;

import de.eric.ban.Ban;
import de.eric.ban.helper.MessageTypes;

import java.sql.*;
import java.util.Optional;

public class MySQL {

    private Connection connection;
    private String host, database, user, password;
    private int port;

    public MySQL(String host, String database, String user, String password, int port){
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
        this.port = port;
        connect();
    }

    //function to connect to mysql
    private void connect(){
        if(connection != null) return;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionString = "jdbc:mysql://" + host + ":" + port + "/" + database +
                    "?autoReconnect=true";
            connection = DriverManager.getConnection(connectionString, user, password);
            createTables();
        } catch (ClassNotFoundException | SQLException exception) {
            exception.printStackTrace();
            Ban.getInstance().getProxy().getConsole().sendMessage(Ban.getInstance()
                    .getMessageHelper().getMessage(MessageTypes.MESSAGE_PREFIX)
                    +"§cVerbindung konnte nicht aufgebaut werden");
        }
    }

    private void createTables() throws SQLException {
        if(connection.isClosed()) return;
        update("CREATE TABLE IF NOT EXISTS `bans`(`uuid` VARCHAR(128) NOT NULL, `reason` VARCHAR(256) NOT NULL, " +
                "`unban` bigint(64) NOT NULL, PRIMARY KEY(`uuid`))");
        Ban.getInstance().getProxy().getConsole().sendMessage(Ban.getInstance()
                .getMessageHelper().getMessage(MessageTypes.MESSAGE_PREFIX)
                +"§aVerbindung aufgebaut und Tabellen erstellt");
    }

    private void update(String query){
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    private Optional<ResultSet> get(String query){
        try{
            Statement statement = connection.createStatement();
            Optional<ResultSet> set = Optional.of(statement.executeQuery(query));
            return set;
        }catch (SQLException exception){
            exception.printStackTrace();
            Ban.getInstance().getProxy().getConsole().sendMessage(Ban.getInstance()
                    .getMessageHelper().getMessage(MessageTypes.MESSAGE_PREFIX)
                    +"§cStatement konnte nicht korret ausgeführt werden");
        }
        return Optional.empty();
    }

    //checks if player banned (could be moved to BanHelper)
    public boolean isPlayerBanned(String uuid) {
        Optional<ResultSet> resultSet = get("SELECT * FROM `bans` WHERE `uuid` = '" + uuid + "'");
        if(resultSet.isEmpty()) return false;
        try{
            if(resultSet.get().next()){
                return true;
            }
        }catch (SQLException exception){
            exception.printStackTrace();
            Ban.getInstance().getProxy().getConsole().sendMessage(Ban.getInstance()
                    .getMessageHelper().getMessage(MessageTypes.MESSAGE_PREFIX)
                    +"§cFehler beim überprüfen ob Spieler gebannt ist.");
        }
        return false;
    }

    //retrieves remaining ban time from a database
    public long getRemainingBanTime(String uuid){
        Optional<ResultSet> resultSet = get("SELECT `unban` FROM `bans` WHERE `uuid` = '" + uuid + "'");
        if(resultSet.isEmpty()) return -1;
        try{
            if(resultSet.get().next()){
                return resultSet.get().getLong("unban");
            }
        }catch(SQLException exception){
            exception.printStackTrace();
        }
        return -1;
    }


    //retrieves the reason of a ban from database
    public String getBanReason(String uuid){
        Optional<ResultSet> resultSet = get("SELECT `reason` FROM `bans` WHERE `uuid` = '" + uuid + "'");
        if(resultSet.isEmpty()) return "";
        try{
            if(resultSet.get().next()){
                return resultSet.get().getString("reason");
            }
        }catch(SQLException exception){
            exception.printStackTrace();
        }
        return "";
    }

    //unbans a player
    public void unbanPlayer(String uuid){
        update("DELETE FROM `bans` WHERE `uuid` = '" + uuid + "'");
    }

    //Prepared statements wären auch ne option
    public void banPlayer(String uuid, String reason, long end){
        update("INSERT INTO `bans`(`uuid`, `reason`, `unban`) " +
                "VALUES('"+ uuid+"', '"+reason+"', "+ end+")");
    }
}
