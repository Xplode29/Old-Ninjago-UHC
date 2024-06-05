package me.butteronmc.uhctemplate.inventories.customInventories.startingInv;

import me.butteronmc.uhctemplate.inventories.CustomInventory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EnchantItemMenu extends CustomInventory {

    private ItemStack itemToEnchant;
    private List<Enchantment> enchants;
    int page;

    public EnchantItemMenu() {
        super("Enchant an item", 45);

        enchants = Arrays.asList(Enchantment.values());
        page = 0;
    }

    @Override
    public void update() {
        super.update();

        if(itemToEnchant == null) {
            return;
        }

        ItemStack item;

        getInventory().setItem(4, itemToEnchant);

        if(page > 0) {
            item = createItem("Page Précédente", Material.ARROW, Collections.singletonList(""));
            getInventory().setItem(36, item);
        }

        if(enchants.size() > (page + 1) * 27) {
            item = createItem("Page Suivante", Material.ARROW, Collections.singletonList(""));
            getInventory().setItem(44, item);
        }

        for(Enchantment enchantment : enchants) {
            int position = enchants.indexOf(enchantment) - (page * 27);
            if(0 <= position && position < 27) {
                item = createItem(
                        enchantment.getName(),
                        Material.ENCHANTED_BOOK,
                        Collections.singletonList("Niveau: " + itemToEnchant.getItemMeta().getEnchantLevel(enchantment))
                );
                getInventory().setItem(position + 9, item);
            }
        }
    }

    @Override
    public void onClickItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();


        if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§rPage Précédente")) {
            page--;
            openInv(player);
        }

        if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§rPage Suivante")) {
            page++;
            openInv(player);
        }

        if(event.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
            for(Enchantment enchantment : enchants) {
                if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§r" + enchantment.getName())) {
                    ItemMeta itemMeta = itemToEnchant.getItemMeta();
                    itemMeta.removeEnchant(enchantment);
                    if(event.isLeftClick()) {
                        itemMeta.addEnchant(enchantment, itemToEnchant.getEnchantmentLevel(enchantment) + 1, false);
                    }
                    else {
                        if(itemToEnchant.getEnchantmentLevel(enchantment) > 1) {
                            itemMeta.addEnchant(enchantment, itemToEnchant.getEnchantmentLevel(enchantment) - 1, true);
                        }
                    }
                    itemToEnchant.setItemMeta(itemMeta);
                    openInv(player);
                }
            }
        }
    }

    public void setItemToEnchant(ItemStack item) {
        itemToEnchant = item;
    }
}
