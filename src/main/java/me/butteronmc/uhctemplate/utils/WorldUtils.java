package me.butteronmc.uhctemplate.utils;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

public class WorldUtils {

    public static void fillBlocks(World world, int xStart, int yStart, int zStart, int widthX, int height, int widthZ, Material mat) {
        for(int x = xStart; x < xStart + widthX; x++) {
            for(int y = yStart; y < yStart + height; y++) {
                for(int z = zStart; z < zStart + widthZ; z++) {
                    Location loc = new Location(world, x, y, z);
                    loc.getBlock().setType(mat);
                }
            }
        }
    }

    public static Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR ||
                lastBlock.getType() == Material.WATER || lastBlock.getType() == Material.STATIONARY_WATER ||
                lastBlock.getType() == Material.WATER || lastBlock.getType() == Material.STATIONARY_WATER
            ) {
                continue;
            }
            break;
        }
        return lastBlock;
    }

    public static UHCPlayer getTargetPlayer(Player player, int range) {
        List<Entity> nearbyE = player.getNearbyEntities(range, range, range);
        ArrayList<LivingEntity> livingE = new ArrayList<>();

        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
                livingE.add((LivingEntity) e);
            }
        }

        BlockIterator bItr = new BlockIterator(player, range);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
            block = bItr.next();
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            // check for entities near this block in the line of sight
            for (LivingEntity entity : livingE) {
                loc = entity.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) {
                    // entity is close enough, set target and stop
                    if(entity instanceof Player) {
                        if(UHCHandler.getUHCPlayer(((Player) entity)) != null) {
                            return UHCHandler.getUHCPlayer(((Player) entity));
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean isInZone(Player player, Location center, int width, int height) {
        if(center.getX() - (double) width / 2 <= player.getLocation().getX() && player.getLocation().getX() <= center.getX() + (double) width / 2) {
            return center.getZ() - (double) height / 2 <= player.getLocation().getZ() && player.getLocation().getZ() <= center.getZ() + (double) height / 2;
        }
        return false;
    }

    public static List<UHCPlayer> getNearPlayers(Player player, int radius) {
        List<UHCPlayer> nearPlayers = new ArrayList<>();

        for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers) {
            if(uhcPlayer != null && uhcPlayer.getRoleEnum() != null) {
                if(player.getLocation().distance(uhcPlayer.getPlayer().getLocation()) <= radius) {
                    nearPlayers.add(uhcPlayer);
                }
            }
        }

        return nearPlayers;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for(double x = location.getX() - radius; x <= location.getX() + radius; x++) {
            for(double y = location.getY() - radius; y <= location.getY() + radius; y++) {
                for(double z = location.getZ() - radius; z <= location.getZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt((int) x, (int) y, (int) z));
                }
            }
        }
        return blocks;
    }
}
