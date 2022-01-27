package de.eric.ban.helper;

import de.eric.ban.Ban;
import net.md_5.bungee.config.Configuration;

import java.util.HashMap;

//load messages into here so that we don't have to load everytime
public class MessageHelper {
    private HashMap<String, String> messages;

    public MessageHelper(){
        messages = new HashMap<>();
    }

    public void addMessage(String identifier, String message){
        messages.put(identifier, message);
    }

    public void setMessages(Configuration configuration){
        for(String key : configuration.getKeys()){
            if(configuration.get(key) instanceof Configuration){
                setMessages(configuration.getSection(key));
                break;
            }else{
                addMessage(key, configuration.getString(key).replaceAll("&", "§"));
            }
        }
    }

    public String getMessage(String identifier){
        return messages.getOrDefault(identifier, "");
    }

    private String replaceReason(String input, String reason){
        return input.replaceAll("%reason%", reason);
    }

    private String replacePlayer(String input, String playerName){
        return input.replaceAll("%player%", playerName);
    }

    private String replaceTime(String input, long time){
        if(time == -1){
            return input.replaceAll("%time%", "Permanent");
        }

        //TODO: Convert time
        return input.replaceAll("%time%", Long.toString(time));
    }

    public String replace(String message, long time, String player, String reason){
        message = replaceTime(message, time);
        message = replacePlayer(message, player);
        message = replaceReason(message, reason);
        return message;
    }
}
