package me.butteronmc.uhctemplate.tasks;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.utils.GraphicUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PreGenTask extends BukkitRunnable {

    private final World world;
    private final int size;
    private int x, y, z;

    public PreGenTask(int size, World world) {
        this.world = world;
        this.size = size;
        this.x = -size;
        this.y = 60;
        this.z = -size;

        this.runTaskTimer(Main.getInstance(), 0, 1);
    }

    @Override
    public void run() {
        float progress = (float) (size * (z + size) + (x + size)) / (2 * size * (size + 1));
        for(Player player : Bukkit.getOnlinePlayers()) {
            GraphicUtils.sendActionText(player,  GraphicUtils.getProgressBar((int) (progress * 100), 100, 20, '|', ChatColor.GREEN, ChatColor.GRAY));
        }
        for(int i = 0; i < 60; i++) {
            if(x <= size) {
                world.getChunkAt(world.getBlockAt(x, y, z)).load();
                this.x += 16;
            } else if (z <= size) {
                x = -this.size;
                z += 16;
            } else {
                this.cancel();
                Bukkit.broadcastMessage(ChatPrefixes.INFO.getMessage("Prégénération finie !"));
                UHCHandler.mapPregenerated = true;
                UHCHandler.isGenerating = false;
                return;
            }
        }
    }
}
