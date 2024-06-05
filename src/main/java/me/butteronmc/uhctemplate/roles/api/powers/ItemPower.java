package me.butteronmc.uhctemplate.roles.api.powers;

import me.butteronmc.uhctemplate.UHCHandler;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ItemPower extends Power {
    public ItemPower(int cooldown, int maxUses) {
        super(cooldown, maxUses);
    }

    public ItemStack getItemStack() {
        return new ItemStack(Material.DIRT);
    }

    public boolean shouldGiveAtDistribution() {
        return true;
    }

    public boolean cancelEvent() {
        return true;
    }

    public boolean isVanilla() {
        return false;
    }

    public boolean onEnable(PlayerInteractEvent event) {
        return false;
    }

    public void onPowerUsed(PlayerInteractEvent event) {
        if(!isVanilla()) {
            if(canUsePower(event.getPlayer()) && onEnable(event)) {
                uses++;
                lastTimeUsed = UHCHandler.mainTimer.timer;
            }
        }
    }

    public void forceCooldown() {
        uses++;
        lastTimeUsed = UHCHandler.mainTimer.timer;
    }

    protected ItemStack format(Material itemMat, String name) {
        ItemStack itemStack = new ItemStack(itemMat);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§r" + name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
