package me.butteronmc.uhctemplate.roles.api.powers;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.utils.GraphicUtils;
import org.bukkit.entity.Player;

public abstract class Power {
    int cooldown; //Cooldown in seconds
    int maxUses; //-1 for infinity
    int uses;

    int lastTimeUsed;

    public Power(int cooldown, int maxUses) {
        this.cooldown = cooldown;
        this.maxUses = maxUses;
        this.uses = 0;
        this.lastTimeUsed = -cooldown;
    }

    public String getName() {
        return "Power Template";
    }

    public String getDescription() {
        return "Power Template";
    }

    public boolean onEnable() {
        return false;
    }

    public boolean canUsePower(Player player) {
        if(uses >= maxUses & maxUses > 0) {
            player.sendMessage(ChatPrefixes.ERROR.getMessage("Tu as déjà utilisé " + maxUses + " fois ton pouvoir !"));
            return false;
        }
        if(lastTimeUsed + cooldown > UHCHandler.mainTimer.timer) {
            player.sendMessage(ChatPrefixes.ERROR.getMessage("Attends " + GraphicUtils.convertToAccurateTime(lastTimeUsed + cooldown - UHCHandler.mainTimer.timer) + " avant de réutiliser ton pouvoir !"));
            return false;
        }
        return true;
    }

    public void onPowerUsed(Player player) {
        if(canUsePower(player) && onEnable()) {
            uses++;
            lastTimeUsed = UHCHandler.mainTimer.timer;
        }
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public void resetPower() {
        this.uses = 0;
        this.lastTimeUsed = -cooldown;
    }
}
