package me.butteronmc.uhctemplate.inventories.customInventories.startingInv.presets;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Preset {
    public String name;
    public Material icon;

    public Preset(String name, Material icon) {
        this.name = name;
        this.icon = icon;
    }

    public ItemStack createItem(String name, Material mat, List<String> lore) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public List<ItemStack> getInventory() {
        List<ItemStack> items = new ArrayList<>();
        items.add(createItem("Test", Material.REDSTONE_BLOCK, Collections.singletonList("Test")));
        return items;
    }
}
