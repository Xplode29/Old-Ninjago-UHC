package me.butteronmc.uhctemplate.chat;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.CampEnum;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.roles.api.powers.Power;
import me.butteronmc.uhctemplate.utils.GraphicUtils;
import org.bukkit.Bukkit;

import java.util.List;

public class ChatModule {

    public static void joinMessage(org.bukkit.entity.Player player) {
        player.sendMessage(ChatPrefixes.SEPARATOR.getPrefix());
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.JOINED.getMessage("Bienvenue sur l'UHC !"));
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.SEPARATOR.getPrefix());
    }

    public static void rolePresentation(UHCPlayer uhcPlayer) {
        org.bukkit.entity.Player player = uhcPlayer.getPlayer();

        player.sendMessage(ChatPrefixes.SEPARATOR.getPrefix());
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.JOINED.getMessage("Vous êtes " + uhcPlayer.getRoleEnum().getCamp().getColor() + uhcPlayer.getRoleEnum().getAbstractRole().getName()));
        if(uhcPlayer.getRoleEnum().getCamp() == CampEnum.SOLO) {
            player.sendMessage(ChatPrefixes.JOINED.getMessage("Vous devez gagner §eSEUL§r"));
        }
        else {
            player.sendMessage(ChatPrefixes.JOINED.getMessage("Vous devez gagner aves les " + uhcPlayer.getRoleEnum().getCamp().getColor() + uhcPlayer.getRoleEnum().getCamp().getName()));
        }
        player.sendMessage("");

        player.sendMessage(ChatPrefixes.LIST_HEADER.getMessage("Description:"));
        //Before the role presentation
        for(String line : uhcPlayer.getRoleEnum().getAbstractRole().getDescription()) {
            player.sendMessage(ChatPrefixes.NORMAL.getMessage(line));
        }
        player.sendMessage("");

        //Items
        if(uhcPlayer.getRoleEnum().getAbstractRole().hasItems()) {
            player.sendMessage(ChatPrefixes.LIST_HEADER.getMessage("Items"));
            for(Power power : uhcPlayer.getRoleEnum().getAbstractRole().getPowers()) {
                if(power instanceof ItemPower) {
                    if(((ItemPower) power).isVanilla()) {
                        player.sendMessage(ChatPrefixes.LIST_ELEMENT.getMessage(
                                power.getName() + ": " + power.getDescription()
                        ));
                    }
                    else {
                        player.sendMessage(ChatPrefixes.LIST_ELEMENT.getMessage(
                                power.getName() + ": " + power.getDescription() +
                                        " (" + ((power.getMaxUses() > 0) ? (power.getMaxUses() + " Utilisation(s)") : "Utilisation infinie") +
                                        " / " + ((power.getCooldown() > 0) ? (GraphicUtils.convertToAccurateTime(power.getCooldown())) : "Pas de cooldown") + ")"
                        ));
                    }
                }
            }
            player.sendMessage("");
        }

        //Commands
        if(uhcPlayer.getRoleEnum().getAbstractRole().hasCommands()) {
            player.sendMessage(ChatPrefixes.LIST_HEADER.getMessage("Commandes"));
            for(Power power : uhcPlayer.getRoleEnum().getAbstractRole().getPowers()) {
                if(power instanceof CommandPower) {
                    CommandPower commandPower = (CommandPower) power;
                    if(!commandPower.isHidden()) {
                        player.sendMessage(ChatPrefixes.LIST_ELEMENT.getMessage(
                                power.getName() + " (/" + Main.commandPrefix + " " + commandPower.getArgument() + "): " + power.getDescription() +
                                        " (" + ((power.getMaxUses() > 0) ? (power.getMaxUses() + " Utilisation(s)") : "Utilisation infinie") +
                                        " / " + ((power.getCooldown() > 0) ? (GraphicUtils.convertToAccurateTime(power.getCooldown())) : "Pas de cooldown") + ")"
                        ));
                    }
                }
            }
            player.sendMessage("");
        }

        player.sendMessage(ChatPrefixes.SEPARATOR.getPrefix());

        //After the role presentation
        for(String line : uhcPlayer.getRoleEnum().getAbstractRole().additionalDescription()) {
            player.sendMessage(ChatPrefixes.NORMAL.getMessage(line));
        }
    }

    public static void roleList(UHCPlayer uhcPlayer) {
        org.bukkit.entity.Player player = uhcPlayer.getPlayer();

        player.sendMessage(ChatPrefixes.SEPARATOR.getPrefix());
        player.sendMessage("");
        for(CampEnum campEnum : CampEnum.values()) {
            List<RoleEnum> campList = UHCHandler.getCampList(campEnum);
            player.sendMessage(ChatPrefixes.LIST_HEADER.getMessage(campEnum.getColor() + "§l" + campEnum.getName() + "§r (" + campList.size() + ")"));
            for(RoleEnum roleEnum : campList) {
                player.sendMessage(ChatPrefixes.LIST_ELEMENT.getMessage(roleEnum.getAbstractRole().getName()));
            }
            player.sendMessage("");
        }
        player.sendMessage(ChatPrefixes.SEPARATOR.getPrefix());
    }

    public static void listEffects(UHCPlayer uhcPlayer) {
        org.bukkit.entity.Player player = uhcPlayer.getPlayer();
        player.sendMessage(ChatPrefixes.SEPARATOR.getPrefix());
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.JOINED.getMessage("Strength: " + uhcPlayer.getStrengthEffect() + "%"));
        player.sendMessage(ChatPrefixes.JOINED.getMessage("Resistance: " + uhcPlayer.getResiEffect() + "%"));
        player.sendMessage(ChatPrefixes.JOINED.getMessage("Speed: " + uhcPlayer.getSpeedEffect() + "%"));
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.SEPARATOR.getPrefix());
    }

    public static void playerDeath(UHCPlayer uhcPlayer) {
        Bukkit.broadcastMessage(ChatPrefixes.SEPARATOR.getPrefix());
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatPrefixes.JOINED.getMessage(
                "Le joueur " + uhcPlayer.getPlayer().getName() + " est mort. Il était " + ((uhcPlayer.getRoleEnum() == null) ?
                        "Pas de role" : uhcPlayer.getRoleEnum().getCamp().getColor() + uhcPlayer.getRoleEnum().getAbstractRole().getName())
        ));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatPrefixes.SEPARATOR.getPrefix());
    }

    public static void listGameCommands(org.bukkit.entity.Player player) {
        player.sendMessage(ChatPrefixes.SEPARATOR.getPrefix());
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.JOINED.getMessage("/ni me: Description de votre role"));
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.JOINED.getMessage("/ni roles: Liste des roles dans la partie"));
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.JOINED.getMessage("/ni effects: Vos pourcentages d'effets"));
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.SEPARATOR.getPrefix());
    } // TO DO

    public static void listHostCommands(org.bukkit.entity.Player player) {
        player.sendMessage(ChatPrefixes.SEPARATOR.getPrefix());
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.JOINED.getMessage("/host set <joueur>: Mettre un joueur host"));
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.JOINED.getMessage("/host heal: Soigner les joueurs de la partie"));
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.JOINED.getMessage("/host revive <joueur>: Ranimer un joueur"));
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.JOINED.getMessage("/host stop: Arréter la partie"));
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.JOINED.getMessage("/host enchant: Ouvre un menu où vous pouvez enchanter l'item que vous tenez"));
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.JOINED.getMessage("/host save: Sauvegarder l'inventaire de départ"));
        player.sendMessage("");
        player.sendMessage(ChatPrefixes.SEPARATOR.getPrefix());
    } // TO DO
}
