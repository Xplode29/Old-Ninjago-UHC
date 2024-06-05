package me.butteronmc.uhctemplate.items.customItems;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import me.butteronmc.uhctemplate.items.CustomItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class MenuItem extends CustomItem {
    public ItemStack menuItem;

    public MenuItem() {
        super(Main.mainColor + "Menu", Collections.singletonList("Right Click to open the menu"), Material.NETHER_STAR, false);
    }

    @Override
    public ItemStack getItem() {
        return this.menuItem;
    }
    @Override
    public void setItem(ItemStack item) {
        this.menuItem = item;
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(UHCHandler.mainTimer.phase > 1) {
            player.sendMessage(ChatPrefixes.ERROR.getMessage("Game already Launched !"));
            return;
        }

        if(UHCHandler.getUHCPlayer(player) != null) {
            if(UHCHandler.hostPlayer == UHCHandler.getUHCPlayer(player)) {
                player.openInventory(InventoriesManager.mainMenu.getInventory());
            }
        }
    }
}
