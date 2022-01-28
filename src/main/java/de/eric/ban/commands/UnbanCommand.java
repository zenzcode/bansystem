package de.eric.ban.commands;

import de.eric.ban.Ban;
import de.eric.ban.helper.MessageTypes;
import me.kbrewster.exceptions.APIException;
import me.kbrewster.exceptions.InvalidPlayerException;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;
import java.util.UUID;

public class UnbanCommand extends Command {

    public UnbanCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!commandSender.hasPermission(getPermission())){
            commandSender.sendMessage(Ban.getInstance().getMessageHelper().getNotAllowedMessage());
            return;
        }

        //We only want to specify name
        if(args.length == 1){
            try{
                UUID playerUUID = Ban.getInstance().getBanHelper().getUUIDFromName(args[0]);
                if(!Ban.getInstance().getMysql().isPlayerBanned(playerUUID.toString())){
                    commandSender.sendMessage(getNotBannedMessage(args[0]));
                    return;
                }
                Ban.getInstance().getBanHelper().unbanPlayer(playerUUID);
                commandSender.sendMessage(getUnbanMessage(args[0]));
            }catch(APIException | IOException | InvalidPlayerException exception){
                commandSender.sendMessage(getErrorMessage());
            }
        }else{
            commandSender.sendMessage(getCommandUsage());
        }
    }

    private BaseComponent[] getNotBannedMessage(String name){
        return new ComponentBuilder().append(
                Ban.getInstance().getMessageHelper().replace(
                        Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_UNBAN_NOT_BANNED),
                        "",
                            name,
                        ""
                )
        ).create();
    }

    private BaseComponent[] getUnbanMessage(String name){
        return new ComponentBuilder().append(
                Ban.getInstance().getMessageHelper().replace(
                        Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_UNBAN),
                        "",
                        name,
                        ""
                )
        ).create();
    }

    private BaseComponent[] getCommandUsage(){
        return new ComponentBuilder().append(
                Ban.getInstance().getMessageHelper().replaceCommand(
                        Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_WRONG_USAGE),
                        "/unban <name>"
                )
        ).create();
    }

    private BaseComponent[] getErrorMessage() {
        return new ComponentBuilder().append(
                Ban.getInstance().getMessageHelper().replace(
                        Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_ERROR),
                        "",
                        "",
                        ""
                )
        ).create();
    }
}
