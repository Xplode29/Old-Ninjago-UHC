package me.butteronmc.uhctemplate.tasks;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.utils.GraphicUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FillTreeTask extends BukkitRunnable {

    private final World world;
    private final int size;
    private int x, y, z;

    public FillTreeTask(int size, World world) {
        this.world = world;
        this.size = size;
        this.x = -size;
        this.y = 60;
        this.z = -size;

        Bukkit.broadcastMessage(ChatPrefixes.INFO.getMessage("Plantation des arbres..."));
        this.runTaskTimer(Main.getInstance(), 0, 1);
    }

    @Override
    public void run() {
        float progress = (float) (size * (z + size) + (x + size)) / (2 * size * (size + 1));
        for(Player player : Bukkit.getOnlinePlayers()) {
            GraphicUtils.sendActionText(player,  GraphicUtils.getProgressBar((int) (progress * 100), 100, 20, '|', ChatColor.GREEN, ChatColor.GRAY));
        }
        for(int i = 0; i < 100; i++) {
            if(x <= size) {
                int r = (new Random()).nextInt(10);
                if(r > 8) {
                    Block highestBlock = world.getHighestBlockAt(x, z).getLocation().add(0, -1, 0).getBlock();

                    if(highestBlock.getType() == Material.GRASS || highestBlock.getType() == Material.DIRT) {
                        if(-UHCHandler.sizeRoofed < x  && x < UHCHandler.sizeRoofed && -UHCHandler.sizeRoofed < z  && z < UHCHandler.sizeRoofed){
                            world.generateTree(highestBlock.getLocation(), TreeType.DARK_OAK);
                        }
                        else {
                            world.generateTree(highestBlock.getLocation(), TreeType.TALL_REDWOOD);
                        }
                    }

                }
                x += 10;
            } else if (z <= size) {
                x = -this.size;
                z += 10;
            } else {
                this.cancel();
                Bukkit.broadcastMessage(ChatPrefixes.INFO.getMessage("Map générée"));
                UHCHandler.mapGenerated = true;
                UHCHandler.isGenerating = false;
                return;
            }
        }
    }
}
