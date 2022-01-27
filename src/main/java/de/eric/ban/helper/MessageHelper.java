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
                addMessage(key, configuration.getString(key));
            }
        }
    }

    public String getMessage(String identifier){
        return messages.getOrDefault(identifier, "");
    }
}
