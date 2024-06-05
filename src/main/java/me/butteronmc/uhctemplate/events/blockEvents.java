package me.butteronmc.uhctemplate.events;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.items.CustomItem;
import me.butteronmc.uhctemplate.items.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class blockEvents implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if(UHCHandler.mainTimer.phase < 4) {
            event.setCancelled(true);
        }

        if(player.getItemInHand() != null) {
            ItemStack holdItem = player.getItemInHand();
            for(CustomItem customItem : ItemManager.customItems) {
                if(holdItem.equals(customItem.getItem())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if(UHCHandler.mainTimer.phase < 4 && UHCHandler.hostPlayer != player) {
            event.setCancelled(true);
        }

        if(player.getItemInHand() != null) {
            ItemStack holdItem = player.getItemInHand();
            for(CustomItem customItem : ItemManager.customItems) {
                if(holdItem.equals(customItem.getItem())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
