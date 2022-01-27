package de.eric.ban.listener;


import de.eric.ban.Ban;
import de.eric.ban.helper.MessageTypes;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ConnectListener implements Listener {

    @EventHandler
    public void onLogin(PostLoginEvent event){
        UUID connectingPlayer = event.getPlayer().getUniqueId();
        //TODO: Check if time of ban is over
        if(Ban.getInstance().getMysql().isPlayerBanned(connectingPlayer.toString())){
            //get ban information
            long remainingTime = Ban.getInstance().getMysql().getRemainingBanTime(connectingPlayer.toString());
            String banReason = Ban.getInstance().getMysql().getBanReason(connectingPlayer.toString());

            //CREATE MESSAGE
            BaseComponent[] component = new ComponentBuilder()
                    .append(Ban.getInstance().getMessageHelper().replace(
                            Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_BAN_FIRST_LINE),
                            remainingTime,
                            event.getPlayer().getDisplayName(),
                            banReason))
                    .append("\n")
                    .append(Ban.getInstance().getMessageHelper().replace(
                            Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_BAN_SECOND_LINE),
                            remainingTime,
                            event.getPlayer().getDisplayName(),
                            banReason))
                    .create();

            //Kick player with message defined in config if banned
            Ban.getInstance().getProxy().getPlayer(connectingPlayer).disconnect(component);
        }
    }
}
