package me.butteronmc.uhctemplate.events;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.items.CustomItem;
import me.butteronmc.uhctemplate.items.ItemManager;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.roles.api.powers.Power;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class clickEvents implements Listener {

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if(!event.hasItem() || event.getPlayer() == null || event.getAction() == null) return;

        ItemStack holdItem = event.getItem();
        for(CustomItem customItem : ItemManager.customItems) {
            if(holdItem.equals(customItem.getItem())) {
                customItem.onClick(event);
            }
        }

        if(UHCHandler.mainTimer.phase > 1) {
            UHCPlayer uhcPlayer = UHCHandler.getUHCPlayer(event.getPlayer());
            if(uhcPlayer == null) {
                event.getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Vous n'êtes pas enregistré comme joueur !"));
                return;
            }

            if(uhcPlayer.getRoleEnum() == null) return;

            ItemPower powerUsed = null;
            for(Power power : uhcPlayer.getRoleEnum().getAbstractRole().getPowers()) {
                if(power instanceof ItemPower) {
                    if(((ItemPower) power).getItemStack().equals(event.getItem())) {
                        ((ItemPower) power).onPowerUsed(event);
                        powerUsed = ((ItemPower) power);
                        break;
                    }
                }
            }
            if(powerUsed != null && powerUsed.cancelEvent()) {
                event.setCancelled(true);
            }
        }
    }
}
