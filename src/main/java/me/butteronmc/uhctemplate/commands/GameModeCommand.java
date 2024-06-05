package me.butteronmc.uhctemplate.commands;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatModule;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import me.butteronmc.uhctemplate.roles.api.powers.Power;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            return true;
        }

        if(strings.length == 0) {
            commandSender.sendMessage("Usage: /" + Main.commandPrefix +" <commande>");
            return true;
        }

        UHCPlayer uhcPlayer = UHCHandler.getUHCPlayer((Player) commandSender);
        if(uhcPlayer == null) {
            commandSender.sendMessage(ChatPrefixes.ERROR.getMessage("Vous n'êtes pas enregistré comme joueur !"));
            return true;
        }
        switch (strings[0]) {
            case "me":
                if(UHCHandler.mainTimer.phase > 4) {
                    ChatModule.rolePresentation(uhcPlayer);
                }
                else {
                    commandSender.sendMessage(ChatPrefixes.ERROR.getMessage("Vous n'avez pas encore obtenu de role !"));
                }
                break;

            case "roles":
                ChatModule.roleList(uhcPlayer);
                break;

            case "help":
                ChatModule.listGameCommands(uhcPlayer.getPlayer());
                break;

            case "effects":
                if(UHCHandler.mainTimer.phase > 4) {
                    ChatModule.listEffects(uhcPlayer);
                }
                else {
                    commandSender.sendMessage(ChatPrefixes.ERROR.getMessage("La partie n'a pas commencé !"));
                }
                break;

            default:
                if(UHCHandler.mainTimer.phase < 5) {
                    commandSender.sendMessage(ChatPrefixes.ERROR.getMessage("Vous n'avez pas encore obtenu de role !"));
                    return true;
                }
                boolean hasPower = false;
                for(Power power : uhcPlayer.getRoleEnum().getAbstractRole().getPowers()) {
                    if(power instanceof CommandPower) {
                        if(((CommandPower) power).getArgument().equals(strings[0])) {
                            ((CommandPower) power).onPowerUsed(uhcPlayer.getPlayer(), strings);
                            hasPower = true;
                        }
                    }
                }

                if(!hasPower) {
                    commandSender.sendMessage(ChatPrefixes.ERROR.getMessage(
                            "Cette commande n'est pas disponible. Pour plus d'information, /" + Main.commandPrefix + " help ou /" + Main.commandPrefix + " me"));
                }
                break;
        }
        return true;
    }
}
