package de.eric.ban.helper;

import de.eric.ban.Ban;
import me.kbrewster.exceptions.APIException;
import me.kbrewster.mojangapi.MojangAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

//supports in ban functionality
public class BanHelper {

    /**
     * Get remaining time from database
     *
     * @param uuid uuid of player to get time from
     * @return string with infos
     */
    public String getRemainingTimeString(UUID uuid) {
        if (!Ban.getInstance().getMysql().isPlayerBanned(uuid.toString())) return "";
        long differenceSeconds = (getTimeLeft(uuid));
        if (differenceSeconds == -1) {
            return "Permanent";
        }
        //Convert to seconds
        differenceSeconds /= 1000;

        //convert to unit
        int days = (int) TimeUnit.SECONDS.toDays(differenceSeconds);
        long hours = TimeUnit.SECONDS.toHours(differenceSeconds) - (days * 24L);
        long minutes = TimeUnit.SECONDS.toMinutes(differenceSeconds) - (TimeUnit.SECONDS.toHours(differenceSeconds) * 60);
        long seconds = TimeUnit.SECONDS.toSeconds(differenceSeconds) - (TimeUnit.SECONDS.toMinutes(differenceSeconds) * 60);

        //Return config message with replaced color codes
        return Ban.getInstance().getMessageHelper().replaceTime(
                Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_TIME),
                days,
                hours,
                minutes,
                seconds
        );
    }

    /**
     * Get Remaining Time from argument
     *
     * @param time time that ban ends
     * @return string with infos
     */
    public String getRemainingTimeStringLocal(long time) {
        long differenceSeconds = time - System.currentTimeMillis();
        if (time == -1) {
            return "Permanent";
        }
        //Convert to seconds
        differenceSeconds /= 1000;

        //convert to unit
        int days = (int) TimeUnit.SECONDS.toDays(differenceSeconds);
        long hours = TimeUnit.SECONDS.toHours(differenceSeconds) - (days * 24L);
        long minutes = TimeUnit.SECONDS.toMinutes(differenceSeconds) - (TimeUnit.SECONDS.toHours(differenceSeconds) * 60);
        long seconds = TimeUnit.SECONDS.toSeconds(differenceSeconds) - (TimeUnit.SECONDS.toMinutes(differenceSeconds) * 60);

        //Return config message with replaced color codes
        return Ban.getInstance().getMessageHelper().replaceTime(
                Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_TIME),
                days,
                hours,
                minutes,
                seconds
        );
    }

    //Returns the reason for a ban from database
    public String getReason(UUID uuid) {
        return Ban.getInstance().getMysql().getBanReason(uuid.toString());
    }

    //returns time that is left in milliseconds
    private long getTimeLeft(UUID uuid) {
        long now = System.currentTimeMillis();
        long end = Ban.getInstance().getMysql().getRemainingBanTime(uuid.toString());
        if (end == -1) {
            return -1;
        }
        return end - now;
    }

    //returns whether a player can be unbanned or not
    public boolean canBeUnbanned(UUID uuid) {
        return getTimeLeft(uuid) < -1;
    }

    //unbans a player
    public void unbanPlayer(UUID uuid) {
        Ban.getInstance().getMysql().unbanPlayer(uuid.toString());
    }


    //bans a player
    public void banPlayer(UUID uuid, String name, String reason, long time) {
        Ban.getInstance().getMysql().banPlayer(uuid.toString(), reason,
                time);
        //dont want to disconnect when player is offline
        if (Ban.getInstance().getProxy().getPlayer(uuid) == null) return;
        Ban.getInstance().getProxy().getPlayer(uuid).disconnect(
                getBanMessage(uuid, name,
                        reason,
                        getRemainingTimeStringLocal(time))
        );
    }

    //Create Base Components because String is deprecated in sendMessage
    public BaseComponent[] getBanMessage(UUID uuid, String displayName, String banReason, String timeLeft) {
        return new ComponentBuilder()
                .append(Ban.getInstance().getMessageHelper().replace(
                        Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_BAN_FIRST_LINE),
                        timeLeft,
                        displayName,
                        banReason))
                .append("\n")
                .append(Ban.getInstance().getMessageHelper().replace(
                        Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_BAN_SECOND_LINE),
                        timeLeft,
                        displayName,
                        banReason))
                .create();
    }

    //use MojangAPI to get UUID of offline Players
    public UUID getUUIDFromName(String name) throws APIException, IOException {
        return MojangAPI.getUUID(name);
    }
}
