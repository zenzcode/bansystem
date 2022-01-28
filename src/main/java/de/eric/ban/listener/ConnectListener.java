package de.eric.ban.listener;


import de.eric.ban.Ban;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ConnectListener implements Listener {

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        UUID connectingPlayer = event.getPlayer().getUniqueId();
        if (Ban.getInstance().getMysql().isPlayerBanned(connectingPlayer.toString())) {
            //check if player can be unbanned
            if (Ban.getInstance().getBanHelper().canBeUnbanned(connectingPlayer)) {
                Ban.getInstance().getBanHelper().unbanPlayer(connectingPlayer);
            } else {
                //get ban information
                String banReason = Ban.getInstance().getMysql().getBanReason(connectingPlayer.toString());


                //Kick player with message defined in config if banned
                Ban.getInstance().getProxy().getPlayer(connectingPlayer).disconnect(
                        Ban.getInstance().getBanHelper().getBanMessage(
                                connectingPlayer,
                                event.getPlayer().getDisplayName(),
                                banReason,
                                Ban.getInstance().getBanHelper().getRemainingTimeString(connectingPlayer)
                        )
                );
            }
        }
    }
}
