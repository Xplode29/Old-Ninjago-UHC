package me.butteronmc.uhctemplate.inventories.customInventories.scenarios;

import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.inventories.CustomInventory;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import me.butteronmc.uhctemplate.scenarios.Scenario;
import me.butteronmc.uhctemplate.scenarios.ScenariosManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class ActivatedScenarioMenu extends CustomInventory {

    public ActivatedScenarioMenu() {
        super("Activated Scenarios", 27);
    }

    @Override
    public void update() {
        super.update();

        ItemStack item;

        item = createItem("Back", Material.ARROW, Collections.singletonList("Go to the scenarios menu"));
        getInventory().setItem(0, item);

        int count = 0;
        for(Scenario scenario : ScenariosManager.scenarios) {
            if(scenario.enabled) {
                item = createItem(scenario.name, scenario.icon, Collections.singletonList("Left Click to Disable / Right Click to settings"));
                getInventory().setItem(count + 9, item);
                count ++;
            }
        }
    }

    @Override
    public void onClickItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.isLeftClick()) {
            for(Scenario scenario : ScenariosManager.scenarios) {
                if(event.getCurrentItem().getData().getItemType() == scenario.icon) {
                    scenario.onToggle();
                    player.sendMessage(ChatPrefixes.INFO.getMessage("Scenario " + scenario.name + " Disabled"));

                    openInv(player);
                    return;
                }
            }
        } //Remove Scenario

        if(event.getCurrentItem().getData().getItemType() == Material.ARROW) {
            InventoriesManager.scenarioMenu.openInv(player);
        } //Go back
    }
}
