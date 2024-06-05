package me.butteronmc.uhctemplate.inventories.customInventories;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.inventories.CustomInventory;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class MainMenu extends CustomInventory implements Listener {

    public MainMenu() {
        super("Main Menu", 54);
    }

    @Override
    public void update() {
        super.update();

        ItemStack item;

        //Inventory
        item = createItem("Inventory", Material.CHEST, Collections.singletonList("Click to modify the starting inventory"));
        getInventory().setItem(12, item);

        //Rules
        item = createItem("Roles", Material.REDSTONE_BLOCK, Collections.singletonList("Click to modify the roles"));
        getInventory().setItem(14, item);

        //Rules
        item = createItem("Rules", Material.BOOK, Collections.singletonList("Click to see the rules"));
        getInventory().setItem(20, item);

        //Scenarios
        item = createItem("Scenarios", Material.REDSTONE_ORE, Collections.singletonList("Click to configure the scenarios"));
        getInventory().setItem(24, item);

        //Start
        item = createItem("Start", Material.SLIME_BALL, Collections.singletonList("Starts the game"));
        getInventory().setItem(31, item);

        //Generate
        item = createItem("Generation", Material.DIRT, Collections.singletonList("Starts the generation"));
        getInventory().setItem(39, item);

        //Pregen
        item = createItem("PreGeneration", Material.LEAVES, Collections.singletonList("Starts the pregen"));
        getInventory().setItem(41, item);
    }

    @Override
    public void onClickItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getCurrentItem().getData().getItemType() == Material.BOOK) { //Rules
            player.sendMessage("Hi man !");
        } //Rules

        if(event.getCurrentItem().getData().getItemType() == Material.CHEST) { //Inventory
            InventoriesManager.startInventoryMenu.openInv(player);
        } //Starting inventory

        if(event.getCurrentItem().getData().getItemType() == Material.REDSTONE_BLOCK) { //Inventory
            InventoriesManager.campMenu.openInv(player);
        } //Roles

        if(event.getCurrentItem().getData().getItemType() == Material.SLIME_BALL) { //Start
            if(UHCHandler.mapGenerated && UHCHandler.mapPregenerated) {
                UHCHandler.launchGame();
                player.sendMessage(ChatPrefixes.INFO.getMessage("Game Launched !"));
            }
            else {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Please genererate / pregen the map first !"));
            }

            player.closeInventory();
        } //Launch Game

        if(event.getCurrentItem().getData().getItemType() == Material.DIRT) {
            if(!UHCHandler.isGenerating) {
                player.sendMessage(ChatPrefixes.INFO.getMessage("Starting Generation..."));
                UHCHandler.generateMap();
            }
            else {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Please wait the active task to finish first ! !"));
            }

            player.closeInventory();
        } //Generate

        if(event.getCurrentItem().getData().getItemType() == Material.LEAVES) {
            if(UHCHandler.mapGenerated && !UHCHandler.isGenerating) {
                player.sendMessage(ChatPrefixes.INFO.getMessage("Starting Pregeneration..."));
                UHCHandler.pregenMap();
            } else if (UHCHandler.isGenerating) {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Please wait the active task to finish first ! !"));
            } else {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Please genererate the map first !"));
            }

            player.closeInventory();
        } //Generate

        if(event.getCurrentItem().getData().getItemType() == Material.REDSTONE_ORE) { //Scenarios
            InventoriesManager.scenarioMenu.openInv(player);
        } //Scenarios
    }
}
