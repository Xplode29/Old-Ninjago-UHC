package me.butteronmc.uhctemplate.scenarios;

import me.butteronmc.uhctemplate.items.CustomItem;
import me.butteronmc.uhctemplate.items.ItemManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class HasteyBoys extends Scenario implements Listener {
    public HasteyBoys() {
        super("Hastey Boys", Material.IRON_PICKAXE);
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
        if((
                item.toString().toLowerCase().contains("pickaxe") ||
                item.toString().toLowerCase().contains("axe") ||
                item.toString().toLowerCase().contains("spade")
        )) {
            if(item.getEnchantmentLevel(Enchantment.DIG_SPEED) < 3) {
                item.addEnchantment(Enchantment.DIG_SPEED, 3);
            }
        }
    }
}
