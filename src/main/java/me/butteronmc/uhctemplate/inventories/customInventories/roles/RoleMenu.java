package me.butteronmc.uhctemplate.inventories.customInventories.roles;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.inventories.CustomInventory;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import me.butteronmc.uhctemplate.roles.CampEnum;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoleMenu extends CustomInventory {

    CampEnum campEnum;

    List<RoleEnum> roles;

    public RoleMenu() {
        super("Roles", 54);
        roles = new ArrayList<>();
    }

    public void setCampEnum(CampEnum campEnum) {
        this.campEnum = campEnum;
        this.roles = new ArrayList<>();
        for(RoleEnum roleEnum : RoleEnum.values()) {
            if(roleEnum.getCamp() == campEnum) {
                roles.add(roleEnum);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        ItemStack item;

        item = createItem("Back", Material.ARROW, Collections.singletonList("Go to the main menu"));
        getInventory().setItem(0, item);

        if(roles == null) return;

        for(RoleEnum roleEnum : roles) {
            item = createItem(roleEnum.getAbstractRole().getName(),
                    roleEnum.getIcon(),
                    Collections.singletonList("Click to " + (UHCHandler.roleList.contains(roleEnum) ? "Disable" : "Enable")));
            if(UHCHandler.roleList.contains(roleEnum)) {
                ItemMeta meta = item.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                item.setItemMeta(meta);
            }
            getInventory().setItem(roles.indexOf(roleEnum) + 9, item);
        }
    }

    @Override
    public void onClickItem(InventoryClickEvent event) {
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getWhoClicked();

        for(RoleEnum roleEnum : roles) {
            if(roleEnum != null) {
                if(event.getCurrentItem().getData().getItemType() == roleEnum.getIcon()) {
                    if(UHCHandler.roleList.contains(roleEnum)) {
                        UHCHandler.roleList.remove(roleEnum);
                        player.sendMessage(ChatPrefixes.INFO.getMessage("Role " + roleEnum.getAbstractRole().getName() + " Disabled"));
                        if(roleEnum.getAbstractRole().getDuo() != null) {
                            UHCHandler.roleList.remove(roleEnum.getAbstractRole().getDuo());
                            player.sendMessage(ChatPrefixes.INFO.getMessage("Role " + roleEnum.getAbstractRole().getDuo().getAbstractRole().getName() + " Disabled"));
                        }
                    }
                    else {
                        UHCHandler.roleList.add(roleEnum);
                        player.sendMessage(ChatPrefixes.INFO.getMessage("Role " + roleEnum.getAbstractRole().getName() + " Enabled"));
                        if(roleEnum.getAbstractRole().getDuo() != null) {
                            UHCHandler.roleList.add(roleEnum.getAbstractRole().getDuo());
                            player.sendMessage(ChatPrefixes.INFO.getMessage("Role " + roleEnum.getAbstractRole().getDuo().getAbstractRole().getName() + " Enabled"));
                        }
                    }

                    openInv(player);
                }
            }
        } //Add Roles

        if(event.getCurrentItem().getData().getItemType() == Material.ARROW) {
            InventoriesManager.campMenu.openInv(player);
        } //Go back
    }
}
