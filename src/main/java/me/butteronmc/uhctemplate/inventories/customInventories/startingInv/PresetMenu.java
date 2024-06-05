package me.butteronmc.uhctemplate.inventories.customInventories.startingInv;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.inventories.CustomInventory;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import me.butteronmc.uhctemplate.inventories.customInventories.startingInv.presets.PresetManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class PresetMenu extends CustomInventory {

    public PresetMenu() {
        super("Load a preset", 27);
    }

    @Override
    public void update() {
        super.update();

        ItemStack item;

        item = createItem("Back", Material.ARROW, Collections.singletonList("Go to the main menu"));
        getInventory().setItem(0, item);

        for(int index = 0; index < PresetManager.presetList.size(); index++) {
            item = createItem(PresetManager.presetList.get(index).name, PresetManager.presetList.get(index).icon, Collections.singletonList("Click to load this preset"));
            getInventory().setItem(index + 9, item);
        }
    }

    @Override
    public void onClickItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        for(int index = 0; index < PresetManager.presetList.size(); index++) {
            if(event.getCurrentItem().getType() == PresetManager.presetList.get(index).icon) {
                UHCHandler.startingItems = PresetManager.presetList.get(index).getInventory();

                InventoriesManager.startInventoryMenu.openInv(player);
                return;
            }
        } //Load a preset

        if(event.getCurrentItem().getType() == Material.ARROW) {
            InventoriesManager.startInventoryMenu.openInv(player);
        } //Go back
    }
}
