package me.butteronmc.uhctemplate.tasks;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatModule;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.roles.api.powers.Power;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class KillTimer extends BukkitRunnable {
    UHCPlayer deadUhcPlayer;
    Location deathLocation;
    List<ItemStack> items;
    boolean showKill;

    public KillTimer(UHCPlayer uhcPlayer, boolean showKill) {
        deadUhcPlayer = uhcPlayer;
        Player deadPlayer = uhcPlayer.getPlayer();
        deathLocation = deadPlayer.getLocation();
        items = Stream.concat(
                    Arrays.stream(deadPlayer.getInventory().getContents()),
                    Arrays.stream(deadPlayer.getInventory().getArmorContents())
                ).collect(Collectors.toList());
        this.showKill = showKill;
        this.runTaskLater(getPlugin(Main.class), UHCHandler.timeToDie * 20L);
    }

    @Override
    public void run() {
        if(deadUhcPlayer.getRoleEnum() != null) {
            for(Power power : deadUhcPlayer.getRoleEnum().getAbstractRole().getPowers()) {
                if(power instanceof ItemPower) {
                    ItemStack itemStack = ((ItemPower) power).getItemStack();
                    items.remove(itemStack);
                }
            }
        }

        for(ItemStack item : items) {
            if(item != null && item.getType() != Material.AIR) {
                deathLocation.getWorld().dropItem(deathLocation, item);
            }
        }

        if(showKill) {
            ChatModule.playerDeath(deadUhcPlayer);
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.WITHER_DEATH, 6.0F, 1);
            }
        }

        UHCHandler.clearPlayer(deadUhcPlayer.getPlayer());
    }
}
