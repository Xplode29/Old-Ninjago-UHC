package me.butteronmc.uhctemplate.inventories.customInventories.startingInv;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.inventories.CustomInventory;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class StartInventoryMenu extends CustomInventory {

    public StartInventoryMenu() {
        super("Starting Inventory", 54);
    }

    @Override
    public void update() {
        super.update();

        ItemStack item;

        item = createItem("Back", Material.ARROW, Collections.singletonList("Go to the main menu"));
        getInventory().setItem(0, item);

        item = createItem("Modifier l'inventaire", Material.ANVIL, Collections.singletonList(""));
        getInventory().setItem(4, item);

        item = createItem("Load a preset", Material.REDSTONE_BLOCK, Collections.singletonList("Click see presets"));
        getInventory().setItem(8, item);

        for(int i = 0; i < 9; i++) {
            item = createItem(" ", Material.STAINED_GLASS_PANE, Collections.singletonList(""));
            getInventory().setItem(i + 9, item);
        }

        if(UHCHandler.startingItems == null) return;

        if(!UHCHandler.startingItems.isEmpty()) {
            for(int index = 0; index < UHCHandler.startingItems.size(); index++) {
                if(index < 9) { //Hotbar
                    getInventory().setItem(index + 45, UHCHandler.startingItems.get(index));
                }
                else {
                    getInventory().setItem(index + 9, UHCHandler.startingItems.get(index));
                }
            }
        }
    }

    @Override
    public void onClickItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getCurrentItem().getType() == Material.ARROW) {
            InventoriesManager.mainMenu.openInv(player);
        } //Go back

        if(event.getCurrentItem().getType() == Material.ANVIL) {
            if(UHCHandler.getUHCPlayer(player) != null) {
                player.setGameMode(GameMode.CREATIVE);
                player.closeInventory();
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);

                for(ItemStack item : UHCHandler.startingItems) {
                    player.getInventory().addItem(item);
                }

                UHCHandler.modifyInvPlayer = UHCHandler.getUHCPlayer(player);
                player.sendMessage(ChatPrefixes.INFO.getMessage("Quant vous avez fini, entrez /host save pour sauvegarder l'inventaire."));
            }
        } //Add an item

        if(event.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
            InventoriesManager.presetMenu.openInv(player);
        } //Open preset menu
    }
}
