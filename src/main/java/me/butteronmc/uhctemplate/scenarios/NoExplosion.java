package me.butteronmc.uhctemplate.scenarios;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class NoExplosion extends Scenario implements Listener {
    public NoExplosion() {
        super("NoExplosions", Material.TNT);
    }

    @EventHandler
    public void onObjectCraft(BlockExplodeEvent event) {
        event.setCancelled(true);
    }
}
