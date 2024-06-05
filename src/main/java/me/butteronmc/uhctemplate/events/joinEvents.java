package me.butteronmc.uhctemplate.events;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.utils.ParticlesEffects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class joinEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();
        if(UHCHandler.mainTimer.phase > 1) { //After Launch
            if(UHCHandler.getUHCPlayer(joinedPlayer) == null) {
                UHCHandler.teleportToSpawn(joinedPlayer);
                joinedPlayer.sendMessage(ChatPrefixes.INFO.getMessage("The Game is already launched, sorry !"));
            }
            else {
                joinedPlayer.sendMessage(ChatPrefixes.INFO.getMessage("Welcome back !"));
            }
        }


        else { //Before launch
            if(UHCHandler.getUHCPlayer(joinedPlayer) == null) {
                UHCHandler.alivePlayers.add(new UHCPlayer(joinedPlayer));
            }
            UHCHandler.clearPlayer(joinedPlayer);
            UHCHandler.teleportToSpawn(joinedPlayer);

        }

        event.setJoinMessage("[" + (Bukkit.getOnlinePlayers().size() > UHCHandler.maxPlayers ? ChatColor.RED : ChatColor.GREEN) + Bukkit.getOnlinePlayers().size() + ChatColor.WHITE + "/" + ChatColor.GREEN + UHCHandler.maxPlayers + ChatColor.WHITE + "] " + Main.mainColor + joinedPlayer.getDisplayName() + ChatColor.WHITE + " joined the game");
        ParticlesEffects.createSpinjitzuEffect(joinedPlayer, Color.BLUE);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player quittedPlayer = event.getPlayer();
        if(UHCHandler.mainTimer.phase < 2) {
            if(UHCHandler.getUHCPlayer(quittedPlayer) != null) {
                UHCHandler.alivePlayers.remove(UHCHandler.getUHCPlayer(quittedPlayer));
            }
        }

        event.setQuitMessage("[" + (Bukkit.getOnlinePlayers().size() > UHCHandler.maxPlayers ? ChatColor.RED : ChatColor.GREEN) + Bukkit.getOnlinePlayers().size() + ChatColor.WHITE + "/" + ChatColor.GREEN + UHCHandler.maxPlayers + ChatColor.WHITE + "] " + Main.mainColor + quittedPlayer.getDisplayName() + ChatColor.WHITE + " left the game");
    }
}
