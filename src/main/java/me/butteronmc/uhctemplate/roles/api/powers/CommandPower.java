package me.butteronmc.uhctemplate.roles.api.powers;

import me.butteronmc.uhctemplate.UHCHandler;
import org.bukkit.entity.Player;

public abstract class CommandPower extends Power {
    public CommandPower(int cooldown, int maxUses) {
        super(cooldown, maxUses);
    }

    public String getArgument() {
        return "test";
    }

    public boolean onEnable(Player player, String[] args) {
        return false;
    }

    public boolean isHidden() {
        return false;
    }

    public void onPowerUsed(Player player, String[] args) {
        if(canUsePower(player) && onEnable(player, args)) {
            uses++;
            lastTimeUsed = UHCHandler.mainTimer.timer;
        }
    }
}
