package me.butteronmc.uhctemplate.roles.api.powers;

import me.butteronmc.uhctemplate.UHCHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class RightClickItemPower extends ItemPower {
    public RightClickItemPower(int cooldown, int maxUses) {
        super(cooldown, maxUses);
    }

    public void onPowerUsed(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if(canUsePower(event.getPlayer()) && onEnable(event)) {
                uses++;
                lastTimeUsed = UHCHandler.mainTimer.timer;
            }
        }
    }
}
