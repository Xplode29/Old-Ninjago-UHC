package me.butteronmc.uhctemplate.inventories.customInventories.scenarios;

import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.inventories.CustomInventory;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import me.butteronmc.uhctemplate.scenarios.Scenario;
import me.butteronmc.uhctemplate.scenarios.ScenariosManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class ScenarioMenu extends CustomInventory {

    public ScenarioMenu() {
        super("Scenarios", 27);
    }

    @Override
    public void update() {
        super.update();
        ItemStack item;

        item = createItem("Back", Material.ARROW, Collections.singletonList("Go to the main menu"));
        getInventory().setItem(0, item);

        for(Scenario scenario : ScenariosManager.scenarios) {
            item = createItem(scenario.name, scenario.icon, Collections.singletonList("Click to " + (scenario.enabled ? "Disable" : "Enable")));
            if(scenario.enabled) {
                ItemMeta meta = item.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                item.setItemMeta(meta);
            }
            getInventory().setItem(ScenariosManager.scenarios.indexOf(scenario) + 9, item);
        }

        item = createItem("Activated Scenarios", Material.REDSTONE_BLOCK, Collections.singletonList("View Activated scenarios"));
        getInventory().setItem(4, item);
    }

    @Override
    public void onClickItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        for(Scenario scenario : ScenariosManager.scenarios) {
            if(event.getCurrentItem().getData().getItemType() == scenario.icon) {
                scenario.onToggle();
                player.sendMessage(ChatPrefixes.INFO.getMessage("Scenario " + scenario.name + (scenario.enabled ? " Enabled" : " Disabled")));

                openInv(player);
            }
        } //Add Scenarios

        if(event.getCurrentItem().getData().getItemType() == Material.ARROW) {
            InventoriesManager.mainMenu.openInv(player);
        } //Go back

        if(event.getCurrentItem().getData().getItemType() == Material.REDSTONE_BLOCK) {
            InventoriesManager.activatedScenarioMenu.openInv(player);
        } //See activated scenarios
    }
}
