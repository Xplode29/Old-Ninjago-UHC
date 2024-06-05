package me.butteronmc.uhctemplate.events;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.utils.WorldUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class playerEvents implements Listener {

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player){
            if(UHCHandler.mainTimer.phase < 3) { // teleporting
                ((Player) event.getEntity()).getPlayer().setFoodLevel(20);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(1 < UHCHandler.mainTimer.phase && UHCHandler.mainTimer.phase < 4) {
            if(event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
                event.setTo(event.getFrom());
            }
        }
    }

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
        if(event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN)
            event.setCancelled(true);
    }


    @EventHandler
    public void onWalkOverWater(PlayerMoveEvent event) {
        if(event.getPlayer() == null) return;
    }
}
