package me.butteronmc.uhctemplate.tasks;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.utils.GraphicUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CleanTreeTask extends BukkitRunnable {

    private final World world;
    private final int size;
    private int x, y, z;

    public CleanTreeTask(int size, World world) {
        this.world = world;
        this.size = size;
        this.x = -size;
        this.y = 60;
        this.z = -size;

        Bukkit.broadcastMessage(ChatPrefixes.INFO.getMessage("Nettoyage de la map..."));
        this.runTaskTimer(Main.getInstance(), 0, 1);
    }

    @Override
    public void run() {
        float progress = (float) (size * (z + size) + (x + size)) / (2 * size * (size + 1));
        for(Player player : Bukkit.getOnlinePlayers()) {
            GraphicUtils.sendActionText(player,  GraphicUtils.getProgressBar((int) (progress * 100), 100, 20, '|', ChatColor.GREEN, ChatColor.GRAY));
        }
        for(int i = 0; i < 1000; i++) {
            if(x <= size) {
                for(int y = 50; y < 120; y++) {
                    Block block = world.getBlockAt(x, y, z);

                    if(block.getType() == Material.WATER ||
                            block.getType() == Material.STATIONARY_WATER ||
                            block.getType() == Material.LAVA ||
                            block.getType() == Material.STATIONARY_LAVA ||
                            block.getType() == Material.SAND
                    ) {
                        block.setType(Material.DIRT);
                    } else if (block.getType() == Material.SANDSTONE) {
                        block.setType(Material.STONE);
                    } else if (block.getType() == Material.SNOW ||
                            block.getType() == Material.CACTUS ||
                            block.getType() == Material.LEAVES ||
                            block.getType() == Material.LEAVES_2 ||
                            block.getType() == Material.LOG ||
                            block.getType() == Material.LOG_2
                    ) {
                        block.setType(Material.AIR);
                    }

                    if(-UHCHandler.sizeRoofed < x  && x < UHCHandler.sizeRoofed && -UHCHandler.sizeRoofed < z  && z < UHCHandler.sizeRoofed){
                        world.setBiome(x, z, Biome.ROOFED_FOREST);
                    }
                    else {
                        world.setBiome(x, z, Biome.TAIGA);
                    }
                }

                Block highestBlock = world.getHighestBlockAt(x, z);

                if(highestBlock.getType() == Material.GRAVEL ||
                        highestBlock.getType() == Material.STONE ||
                        highestBlock.getType() == Material.DIRT ||
                        highestBlock.getType() == Material.SANDSTONE ||
                        highestBlock.getType() == Material.SAND
                ) {
                    highestBlock.setType(Material.GRASS);
                }

                this.x++;
            } else if (z <= size) {
                x = -this.size;
                z++;
            } else {
                this.cancel();
                new FillTreeTask(size, this.world);
                return;
            }
        }
    }
}
