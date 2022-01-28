package de.eric.ban.helper;

import de.eric.ban.Ban;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.config.Configuration;

import java.util.HashMap;

//load messages into here so that we don't have to load everytime
public class MessageHelper {
    private final HashMap<String, String> messages;

    public MessageHelper() {
        messages = new HashMap<>();
    }

    public void addMessage(String identifier, String message) {
        messages.put(identifier, message);
    }

    //add all messages to hashmap of messages with their keys as key
    public void setMessages(Configuration configuration) {
        for (String key : configuration.getKeys()) {
            if (configuration.get(key) instanceof Configuration) {
                setMessages(configuration.getSection(key));
                break;
            } else {
                addMessage(key, configuration.getString(key).replaceAll("&", "§"));
            }
        }
    }

    public String getMessage(String identifier) {
        return messages.getOrDefault(identifier, "");
    }

    private String replaceReason(String input, String reason) {
        return input.replaceAll("%reason%", reason);
    }

    private String replacePlayer(String input, String playerName) {
        return input.replaceAll("%player%", playerName);
    }

    private String replaceTime(String input, String time) {
        return input.replaceAll("%time%", time);
    }

    private String replacePrefix(String input) {
        return input.replaceAll("%prefix%", messages.get(MessageTypes.MESSAGE_PREFIX));
    }

    /**
     * Replaces common things
     *
     * @param message the message to replace in
     * @param time    what %time% is replaced with
     * @param player  what %player% is replaced with
     * @param reason  what %reason% is replaced with
     * @return modified string
     */
    public String replace(String message, String time, String player, String reason) {
        message = replaceTime(message, time);
        message = replacePlayer(message, player);
        message = replaceReason(message, reason);
        message = replacePrefix(message);
        return message;
    }

    /**
     * Single replace functions to not have one big
     */

    private String replaceDays(String message, int days) {
        return message.replaceAll("%days%", Integer.toString(days));
    }

    private String replaceHours(String message, long hours) {
        return message.replaceAll("%hours%", Long.toString(hours));
    }

    private String replaceMinutes(String message, long minutes) {
        return message.replaceAll("%minutes%", Long.toString(minutes));
    }

    private String replaceSeconds(String message, long seconds) {
        return message.replaceAll("%seconds%", Long.toString(seconds));
    }


    //Replaces a %time%
    public String replaceTime(String message, int days, long hours, long minutes, long seconds) {
        message = replaceDays(message, days);
        message = replaceHours(message, hours);
        message = replaceMinutes(message, minutes);
        message = replaceSeconds(message, seconds);

        return message;
    }

    //replaces a %command%
    public String replaceCommand(String message, String help) {
        return replacePrefix(message.replaceAll("%command%", help));
    }

    //Return component for not allowed message
    public BaseComponent[] getNotAllowedMessage() {
        return new ComponentBuilder()
                .append(Ban.getInstance().getMessageHelper().getMessage(
                        MessageTypes.MESSAGE_NOT_ALLOWED)
                ).create();
    }
}
