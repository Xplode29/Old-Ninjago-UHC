package me.butteronmc.uhctemplate.items;

import me.butteronmc.uhctemplate.tasks.ItemCooldownTimer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CustomItem {
    public static ItemStack item;
    private final String name;
    private final List<String> lore;
    private final Material mat;
    private boolean movable;
    public List<Player> playersInCooldown;
    public ItemCooldownTimer cooldownTimer;

    public CustomItem(String name, List<String> lore, Material mat, boolean mov) {
        this.name = name;
        this.lore = lore;
        this.mat = mat;
        this.movable = mov;

        createItem();
        playersInCooldown = new ArrayList<>();
    }

    public void createItem() {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();

        List<String> newLore = new ArrayList<>();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        for(String line : lore) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(newLore);
        meta.spigot().setUnbreakable(true);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        item.setItemMeta(meta);
        setItem(item);
    }

    public void onClick(PlayerInteractEvent event) {}
    public void onHoldInHand(Player player) {}
    public void onHoldInInventory(Player player) {}

    public ItemStack getItem() {
        return item;
    }
    public void setItem(ItemStack item) {
        CustomItem.item = item;
    }
    public boolean isMovable() {
        return this.movable;
    }
    public void setMovable(boolean mov) {
        this.movable = mov;
    }
}
