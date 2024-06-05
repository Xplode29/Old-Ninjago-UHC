package me.butteronmc.uhctemplate.inventories.customInventories.roles.ingame;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.inventories.CustomInventory;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerinvMenu extends CustomInventory {

    UHCPlayer targettedPlayer;

    List<ItemStack> inventory;

    public PlayerinvMenu() {
        super("Inventaire", 45);
        inventory = new ArrayList<>();
    }

    public void setPlayer(UHCPlayer player) {
        this.targettedPlayer = player;
        for(ItemStack item : player.getPlayer().getInventory().getArmorContents()) {
            if(item == null) {
                inventory.add(new ItemStack(Material.AIR));
            }
            else {
                inventory.add(item);
            }
        }
        for(ItemStack item : player.getPlayer().getInventory().getContents()) {
            if(item == null) {
                inventory.add(new ItemStack(Material.AIR));
            }
            else {
                inventory.add(item);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        ItemStack item;

        item = createItem("Back", Material.ARROW, Collections.singletonList("Go to the main menu"));
        getInventory().setItem(0, item);

        for(int i = 4; i < 9; i++) {
            item = createItem(" ", Material.STAINED_GLASS_PANE, Collections.singletonList(""));
            getInventory().setItem(i, item);
        }

        if(inventory == null) return;

        if(!inventory.isEmpty()) {
            for(int index = 0; index < inventory.size(); index++) {
                if(index < 4) { //Armor
                    getInventory().setItem(index, inventory.get(index));
                }
                else if(index < 13) { //Hotbar
                    getInventory().setItem(index + 36 - 4, inventory.get(index));
                }
                else {
                    getInventory().setItem(index - 4, inventory.get(index));
                }
            }
        }
    }
}
