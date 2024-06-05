package me.butteronmc.uhctemplate.commands;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatModule;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class HostCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if(!(commandSender instanceof Player)) return false;

        UHCPlayer playerExecutor = UHCHandler.getUHCPlayer((Player) commandSender);
        if(playerExecutor == null) return false;

        if(args.length < 1) {
            UHCHandler.setPlayerHost(playerExecutor);
            playerExecutor.getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Vous êtes maintenant le host !"));
            return true;
        }

        UHCPlayer targetPlayer = null;
        if(args.length >= 2) {
            targetPlayer = UHCHandler.getUHCPlayer(Bukkit.getPlayer(args[1]));
        }

        switch (args[0]) {
            case "set":
                if(args.length < 2) {
                    playerExecutor.getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Usage: /host set <joueur>"));
                    return true;
                }

                if(targetPlayer == null) {
                    playerExecutor.getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Veuillez indiquer un joueur correct !"));
                    return true;
                }

                UHCHandler.setPlayerHost(targetPlayer);
                break;

            case "heal":
                for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers) {
                    uhcPlayer.getPlayer().setHealth(uhcPlayer.getPlayer().getMaxHealth());
                    uhcPlayer.getPlayer().setFoodLevel(20);
                }
                break;

            case "revive":
                if(args.length < 2) {
                    playerExecutor.getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Usage: /host set <joueur>"));
                    return true;
                }

                if(targetPlayer == null) {
                    playerExecutor.getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Veuillez indiquer un joueur correct !"));
                    return true;
                }

                UHCHandler.revivePlayer(targetPlayer);
                break;

            case "stop":
                UHCHandler.stopGame();
                break;

            case "enchant":
                if(playerExecutor.getPlayer().getItemInHand() == null || playerExecutor.getPlayer().getItemInHand().getType() == Material.AIR) {
                    playerExecutor.getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Veuillez tenir un item dans votre main !"));
                    return true;
                }

                InventoriesManager.enchantItemMenu.setItemToEnchant(playerExecutor.getPlayer().getItemInHand());
                InventoriesManager.enchantItemMenu.openInv(playerExecutor.getPlayer());
                break;

            case "save":
                if(UHCHandler.modifyInvPlayer == playerExecutor) {
                    UHCHandler.startingItems= Arrays.asList(playerExecutor.getPlayer().getInventory().getContents());
                    playerExecutor.getPlayer().getInventory().clear();
                    playerExecutor.getPlayer().getInventory().setArmorContents(null);
                    playerExecutor.getPlayer().setGameMode(GameMode.SURVIVAL);
                    UHCHandler.setPlayerHost(playerExecutor);
                    UHCHandler.modifyInvPlayer = null;
                    playerExecutor.getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Inventaire sauvegardé"));
                }
                else {
                    playerExecutor.getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Veuillez d'abord modifier l'inventaire !"));
                }
                break;

            case "help":
                ChatModule.listHostCommands(playerExecutor.getPlayer());
                break;
        }
        return true;
    }
}
