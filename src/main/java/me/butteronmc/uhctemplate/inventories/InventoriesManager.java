package me.butteronmc.uhctemplate.inventories;

import me.butteronmc.uhctemplate.inventories.customInventories.MainMenu;
import me.butteronmc.uhctemplate.inventories.customInventories.roles.CampMenu;
import me.butteronmc.uhctemplate.inventories.customInventories.roles.RoleMenu;
import me.butteronmc.uhctemplate.inventories.customInventories.roles.ingame.PlayerinvMenu;
import me.butteronmc.uhctemplate.inventories.customInventories.scenarios.ActivatedScenarioMenu;
import me.butteronmc.uhctemplate.inventories.customInventories.scenarios.ScenarioMenu;
import me.butteronmc.uhctemplate.inventories.customInventories.startingInv.EnchantItemMenu;
import me.butteronmc.uhctemplate.inventories.customInventories.startingInv.PresetMenu;
import me.butteronmc.uhctemplate.inventories.customInventories.startingInv.StartInventoryMenu;
import me.butteronmc.uhctemplate.inventories.customInventories.startingInv.presets.PresetManager;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class InventoriesManager {
    public static List<CustomInventory> customInventories;
    public static MainMenu mainMenu;
    public static ScenarioMenu scenarioMenu;
    public static ActivatedScenarioMenu activatedScenarioMenu;

    public static StartInventoryMenu startInventoryMenu;
    public static PresetMenu presetMenu;
    public static EnchantItemMenu enchantItemMenu;

    public static CampMenu campMenu;
    public static RoleMenu roleMenu;
    public static PlayerinvMenu playeInvMenu;

    public static void init() {
        PresetManager.init();

        customInventories = new ArrayList<>();

        customInventories.add(mainMenu = new MainMenu());
        customInventories.add(scenarioMenu = new ScenarioMenu());
        customInventories.add(activatedScenarioMenu = new ActivatedScenarioMenu());

        //start inventory
        customInventories.add(startInventoryMenu = new StartInventoryMenu());
        customInventories.add(presetMenu = new PresetMenu());
        customInventories.add(enchantItemMenu = new EnchantItemMenu());

        customInventories.add(campMenu = new CampMenu());
        customInventories.add(roleMenu = new RoleMenu());
        customInventories.add(playeInvMenu = new PlayerinvMenu());
    }

    public static void onClickItem(InventoryClickEvent event) {
        for(CustomInventory customInventory : customInventories) {
            if(event.getInventory().equals(customInventory.getInventory())) {
                customInventory.onClickItem(event);
            }
        }
    }
}
