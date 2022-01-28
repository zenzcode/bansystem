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

public class BanCommand extends Command {


    public BanCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!commandSender.hasPermission(getPermission())){
            commandSender.sendMessage(Ban.getInstance().getMessageHelper().getNotAllowedMessage());
            return;
        }

        if(args.length > 1){
            try{
                UUID target = Ban.getInstance().getBanHelper().getUUIDFromName(args[0]);
                //we dont want to ban a already banned player
                if(Ban.getInstance().getMysql().isPlayerBanned(target.toString())){
                    //TODO: Ban Message
                    commandSender.sendMessage(getPlayerBannedMessage(target, args[0]));
                    return;
                }
                String reason = args[1];
                UUID commandSenderUUID = Ban.getInstance().getProxy().getPlayer(commandSender.getName()).getUniqueId();
                switch (args.length){
                    case 2:
                        if(commandSenderUUID == target) return;
                        Ban.getInstance().getBanHelper().banPlayer(target, args[0], reason, -1);
                        commandSender.sendMessage(getBanMessage(args[0], reason, -1));
                        break;
                    case 3:
                        if(commandSenderUUID == target) return;
                        try{
                            int timeInDays = Integer.parseInt(args[2]);
                            long timeMillis = System.currentTimeMillis() + (long) timeInDays * 24 * 60 * 60 * 1000;
                            Ban.getInstance().getBanHelper().banPlayer(target, args[0], reason, timeMillis);
                            commandSender.sendMessage(getBanMessage(args[0], reason, timeMillis));
                        }catch(NumberFormatException ex){
                            commandSender.sendMessage(getCommandUsage());
                        }
                        break;
                    default:
                        commandSender.sendMessage(getCommandUsage());
                        break;
                }
            }catch(APIException | IOException | InvalidPlayerException exception){
                commandSender.sendMessage(getErrorMessage());
            }
        }else{
            commandSender.sendMessage(
                    getCommandUsage()
            );
        }
    }

    //Create Base Components because String is deprecated in sendMessage

    private BaseComponent[] getPlayerBannedMessage(UUID target, String name){
        return new ComponentBuilder().append(
                Ban.getInstance().getMessageHelper().replace(
                        Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_PLAYER_BANNED),
                        Ban.getInstance().getBanHelper().getRemainingTimeString(target),
                        name,
                        Ban.getInstance().getBanHelper().getReason(target))
        ).create();
    }

    private BaseComponent[] getBanMessage(String name, String reason, long time){

        return new ComponentBuilder()
                .append(Ban.getInstance().getMessageHelper().replace(
                        Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_BAN),
                        Ban.getInstance().getBanHelper().getRemainingTimeStringLocal(time),
                        name,
                        reason
                )).create();
    }

    private BaseComponent[] getCommandUsage(){
        return new ComponentBuilder().append(
                Ban.getInstance().getMessageHelper().replaceCommand(
                        Ban.getInstance().getMessageHelper().getMessage(MessageTypes.MESSAGE_WRONG_USAGE),
                        "/ban <name> <grund> [<zeit>]"
                )
        ).create();
    }

    private BaseComponent[] getErrorMessage(){
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
