package me.butteronmc.uhctemplate.scenarios;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.utils.WorldUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TimberPvp extends Scenario implements Listener {

    public TimberPvp() {
        super("Timber Pvp", Material.DIAMOND_AXE);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock() == null || event.getBlock().getDrops() == null) {
            return;
        }

        if(UHCHandler.mainTimer.phase < 5) {
            if(event.getBlock().getType() == Material.LOG || event.getBlock().getType() == Material.LOG_2) {
                new breakRunnable(event.getBlock());
            }
        }
    }

    private class breakRunnable extends BukkitRunnable {

        private final Block block;

        public breakRunnable(Block block) {
            this.block = block;
            this.runTaskLater(Main.getInstance(), 1);
        }

        @Override
        public void run() {
            for(Block b : WorldUtils.getNearbyBlocks(block.getLocation(), 1)) {
                if(b.getType() == Material.LOG || b.getType() == Material.LOG_2) {
                    b.breakNaturally();
                    new breakRunnable(b);
                }
            }
            cancel();
        }
    }
}
