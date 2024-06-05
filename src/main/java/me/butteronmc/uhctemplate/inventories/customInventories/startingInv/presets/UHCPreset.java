package me.butteronmc.uhctemplate.inventories.customInventories.startingInv.presets;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UHCPreset extends Preset{
    public UHCPreset() {
        super("Classic UHC", Material.IRON_PICKAXE);
    }

    @Override
    public List<ItemStack> getInventory() {
        List<ItemStack> items = new ArrayList<>();

        ItemStack item;
        Map<Enchantment, Integer> enchantments;

        //pickaxe
        item = new ItemStack(Material.IRON_PICKAXE, 1);
        enchantments = new HashMap<>();
        enchantments.put(Enchantment.DIG_SPEED, 3); //Efficiency 3
        enchantments.put(Enchantment.DURABILITY, 2); //Unbreaking 2
        item.addEnchantments(enchantments);
        items.add(item);

        //apples
        item = new ItemStack(Material.APPLE, 64);
        items.add(item);

        //feather
        item = new ItemStack(Material.FEATHER, 16);
        items.add(item);

        //food
        item = new ItemStack(Material.COOKED_BEEF, 64);
        items.add(item);

        //logs
        item = new ItemStack(Material.LOG, 64);
        items.add(item);

        //logs
        item = new ItemStack(Material.LOG, 64);
        items.add(item);

        //food
        item = new ItemStack(Material.WATER_BUCKET, 1);
        items.add(item);

        return items;
    }
}
