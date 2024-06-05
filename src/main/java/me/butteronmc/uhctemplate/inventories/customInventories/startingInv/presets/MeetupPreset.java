package me.butteronmc.uhctemplate.inventories.customInventories.startingInv.presets;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeetupPreset extends Preset{
    public MeetupPreset() {
        super("Meetup", Material.IRON_SWORD);
    }

    @Override
    public List<ItemStack> getInventory() {
        List<ItemStack> items = new ArrayList<>();

        ItemStack item;
        Map<Enchantment, Integer> enchantments;

        //sword
        item = new ItemStack(Material.DIAMOND_SWORD, 1);
        enchantments = new HashMap<>();
        enchantments.put(Enchantment.DAMAGE_ALL, 3); //Sharpness 3
        item.addEnchantments(enchantments);
        items.add(item);

        //gapples
        item = new ItemStack(Material.GOLDEN_APPLE, 16);
        items.add(item);

        //bow
        item = new ItemStack(Material.BOW, 1);
        enchantments = new HashMap<>();
        enchantments.put(Enchantment.ARROW_DAMAGE, 3); //Power 3
        item.addEnchantments(enchantments);
        items.add(item);

        //lava bucket
        item = new ItemStack(Material.LAVA_BUCKET, 1);
        items.add(item);

        //lava bucket
        item = new ItemStack(Material.WATER_BUCKET, 1);
        items.add(item);

        //Food
        item = new ItemStack(Material.COOKED_BEEF, 64);
        items.add(item);

        //Blocks
        item = new ItemStack(Material.WOOD, 64);
        items.add(item);

        //lily pads
        item = new ItemStack(Material.WATER_LILY, 64);
        items.add(item);

        //Blocks
        item = new ItemStack(Material.WOOD, 64);
        items.add(item);

        //helmet
        item = new ItemStack(Material.DIAMOND_HELMET, 1);
        enchantments = new HashMap<>();
        enchantments.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2); //Protection 2
        item.addEnchantments(enchantments);
        items.add(item);

        //chestplate
        item = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        enchantments = new HashMap<>();
        enchantments.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2); //Protection 2
        item.addEnchantments(enchantments);
        items.add(item);

        //leggings
        item = new ItemStack(Material.IRON_LEGGINGS, 1);
        enchantments = new HashMap<>();
        enchantments.put(Enchantment.PROTECTION_ENVIRONMENTAL, 3); //Protection 3
        item.addEnchantments(enchantments);
        items.add(item);

        //boots
        item = new ItemStack(Material.IRON_BOOTS, 1);
        enchantments = new HashMap<>();
        enchantments.put(Enchantment.PROTECTION_ENVIRONMENTAL, 3); //Protection 3
        item.addEnchantments(enchantments);
        items.add(item);

        //Logs
        item = new ItemStack(Material.ARROW, 64);
        items.add(item);

        //Logs
        item = new ItemStack(Material.LOG, 64);
        items.add(item);

        //Logs
        item = new ItemStack(Material.LOG, 64);
        items.add(item);

        //Logs
        item = new ItemStack(Material.LOG, 64);
        items.add(item);

        //Compass
        item = new ItemStack(Material.COMPASS, 1);
        items.add(item);

        //Pickaxe
        item = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        enchantments = new HashMap<>();
        enchantments.put(Enchantment.DIG_SPEED, 3); //Efficiency 3
        enchantments.put(Enchantment.DURABILITY, 2); //Unbreaking 2
        item.addEnchantments(enchantments);
        items.add(item);

        //Axe
        item = new ItemStack(Material.DIAMOND_AXE, 1);
        enchantments = new HashMap<>();
        enchantments.put(Enchantment.DIG_SPEED, 3); //Efficiency 3
        enchantments.put(Enchantment.DURABILITY, 2); //Unbreaking 2
        item.addEnchantments(enchantments);
        items.add(item);

        //Anvil
        item = new ItemStack(Material.ANVIL, 8);
        items.add(item);

        //lava bucket
        item = new ItemStack(Material.LAVA_BUCKET, 1);
        items.add(item);

        //lava bucket
        item = new ItemStack(Material.WATER_BUCKET, 1);
        items.add(item);

        //Exp bottles
        item = new ItemStack(Material.EXP_BOTTLE, 64);
        items.add(item);

        //lava bucket
        item = new ItemStack(Material.LAVA_BUCKET, 1);
        items.add(item);

        //lava bucket
        item = new ItemStack(Material.WATER_BUCKET, 1);
        items.add(item);

        return items;
    }
}
