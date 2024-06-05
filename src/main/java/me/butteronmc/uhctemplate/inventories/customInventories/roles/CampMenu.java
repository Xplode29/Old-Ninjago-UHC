package me.butteronmc.uhctemplate.inventories.customInventories.roles;

import me.butteronmc.uhctemplate.inventories.CustomInventory;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import me.butteronmc.uhctemplate.roles.CampEnum;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CampMenu extends CustomInventory {

    public CampMenu() {
        super("Camps", 27);
    }

    @Override
    public void update() {
        super.update();
        ItemStack item;

        item = createItem("Back", Material.ARROW, Collections.singletonList("Go to the main menu"));
        getInventory().setItem(0, item);

        List<CampEnum> camps = Arrays.asList(CampEnum.values());
        for(CampEnum campEnum : camps) {
            item = createItem(
                    campEnum.getColor() + campEnum.getName(),
                    campEnum.getIcon(),
                    Collections.singletonList("Clique pour modifier les " + campEnum.getName())
            );
            getInventory().setItem(2 * (camps.indexOf(campEnum)) + 10, item);
        }
    }

    @Override
    public void onClickItem(InventoryClickEvent event) {
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getWhoClicked();

        for(CampEnum campEnum : CampEnum.values()) {
            if(campEnum != null) {
                if(event.getCurrentItem().getType() == campEnum.getIcon()) {
                    InventoriesManager.roleMenu.setCampEnum(campEnum);
                    InventoriesManager.roleMenu.openInv(player);
                }
            }
        }

        if(event.getCurrentItem().getData().getItemType() == Material.ARROW) {
            InventoriesManager.mainMenu.openInv(player);
        } //Go back
    }
}
