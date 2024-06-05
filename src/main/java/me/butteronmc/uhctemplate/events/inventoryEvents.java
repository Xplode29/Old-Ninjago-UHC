package me.butteronmc.uhctemplate.events;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.inventories.CustomInventory;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import me.butteronmc.uhctemplate.items.CustomItem;
import me.butteronmc.uhctemplate.items.ItemManager;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.roles.api.powers.Power;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class inventoryEvents implements Listener {

    @EventHandler
    public void onClickItem(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) { return; } // No inv
        if (event.getWhoClicked() == null) { return; }
        if (event.getCurrentItem() == null || event.getCursor() == null) { return; }
        if (event.getCurrentItem().getType() == Material.AIR && event.getCursor().getType() == Material.AIR) { return; }

        for(CustomItem customItem : ItemManager.customItems) {
            if(event.getCurrentItem().equals(customItem.getItem()) || event.getCursor().equals(customItem.getItem())) {
                if(!customItem.isMovable()) {
                    event.setCancelled(true);
                }
            }

            if(event.getClick() == ClickType.NUMBER_KEY) {
                if(customItem.getItem().equals(event.getClickedInventory().getItem(event.getHotbarButton()))) {
                    if(!customItem.isMovable()) {
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (event.getClickedInventory().getHolder() instanceof CustomInventory) {
            InventoriesManager.onClickItem(event);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        for(CustomItem customItem : ItemManager.customItems) {
            if(event.getItemDrop().getItemStack().equals(customItem.getItem())) {
                event.setCancelled(true);
            }
        }

        if(UHCHandler.mainTimer.phase > 1) {
            UHCPlayer uhcPlayer = UHCHandler.getUHCPlayer(event.getPlayer());
            if(uhcPlayer == null) {
                event.getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Vous n'êtes pas enregistré comme joueur !"));
                return;
            }

            if(uhcPlayer.getRoleEnum() == null) return;

            for(Power power : uhcPlayer.getRoleEnum().getAbstractRole().getPowers()) {
                if(power instanceof ItemPower) {
                    if(((ItemPower) power).getItemStack().equals(event.getItemDrop())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {
        for(CustomItem customItem : ItemManager.customItems) {
            if(event.getItem().equals(customItem.getItem())) {
                if(!customItem.isMovable()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
