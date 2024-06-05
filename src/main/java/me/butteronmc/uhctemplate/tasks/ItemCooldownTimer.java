package me.butteronmc.uhctemplate.tasks;

import me.butteronmc.uhctemplate.items.CustomItem;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemCooldownTimer extends BukkitRunnable {
    CustomItem customItem;
    public int timer, cooldown;
    Player player;

    public ItemCooldownTimer(Player player, CustomItem item, int cooldown) {
        this.cooldown = cooldown;
        this.timer = 0;
        this.customItem = item;
        this.player = player;
        customItem.playersInCooldown.add(player);
    }

    @Override
    public void run() {
        timer++;
        if(timer >= cooldown) {
            cancel();
            customItem.playersInCooldown.remove(player);
        }
    }
}
