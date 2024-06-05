package me.butteronmc.uhctemplate.scenarios;

import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.items.CustomItem;
import me.butteronmc.uhctemplate.items.ItemManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class NoRod extends Scenario implements Listener {
    public NoRod() {
        super("NoRods", Material.FISHING_ROD);
    }

    @EventHandler
    public void onObjectCraft(CraftItemEvent event) {
        ItemStack item = event.getCurrentItem();
        if(item == null) {
            return;
        }
        for(CustomItem customItem : ItemManager.customItems) {
            if(customItem.getItem().equals(item)) {
                return;
            }
        }
        if(item.getType() == Material.FISHING_ROD) {
            event.getWhoClicked().sendMessage(ChatPrefixes.ERROR.getMessage("You cant craft rods !"));

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if(event.hasItem()) {
            ItemStack item = event.getItem();
            if(item.getType() == Material.FISHING_ROD) {
                event.getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("You cant use rods !"));

                event.setCancelled(true);
            }
        }
    }
}
