package me.butteronmc.uhctemplate.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CustomInventory implements InventoryHolder {
    public Inventory inv;
    public int size;
    public String title;
    public InventoryType[] inventoryType;

    public CustomInventory(String title, int size, InventoryType... inventoryType) {
        this.title = title;
        this.size = size;
        this.inventoryType = inventoryType;
        update();
    }

    public void update() {
        if(inventoryType.length > 0) {
            inv = Bukkit.createInventory(this, this.inventoryType[0], this.title);
        }
        else {
            inv = Bukkit.createInventory(this, this.size, this.title);
        }
    }

    public ItemStack createItem(String name, Material mat, List<String> lore) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();

        List<String> newLore = new ArrayList<>();
        meta.setDisplayName("§r" + name);

        for(String line : lore) {
            newLore.add("§r" + line);
        }
        meta.setLore(newLore);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void openInv(Player player) {
        update();
        player.openInventory(getInventory());
    }

    public void onClickItem(InventoryClickEvent event) {

    }
}
