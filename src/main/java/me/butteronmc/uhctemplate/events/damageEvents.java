package me.butteronmc.uhctemplate.events;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.events.custom.EventUtils;
import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class damageEvents implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if (!(event.getEntity() instanceof Player)) return;
        Player damagedPlayer = (Player) event.getEntity();

        UHCPlayer damaged = UHCHandler.getUHCPlayer(damagedPlayer);
        if(damaged == null) {
            event.setCancelled(true);
            return;
        }

        if(UHCHandler.mainTimer.phase < 4 || UHCHandler.mainTimer.timer < UHCHandler.invincibilityTime) { // not pvp
            event.setCancelled(true);
            return;
        }

        UHCPlayer damager = null;
        if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if(((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
                damager = UHCHandler.getUHCPlayer((Player) ((EntityDamageByEntityEvent) event).getDamager());
            }
        }
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if(damaged.hasNoFall) {
                event.setCancelled(true);
                return;
            }
        }

        if(damager != null) {
            if(UHCHandler.mainTimer.phase < 5) { // not pvp
                event.setCancelled(true);
                return;
            }

            damaged.onGetHitByPlayer(damager);
            damager.onHitPlayer(damaged);
        }
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event) {
        Player damagedPlayer = event.getEntity();

        UHCPlayer damaged = UHCHandler.getUHCPlayer(damagedPlayer);
        if(damaged == null) {
            return;
        }

        UHCPlayer damager = null;
        if(damagedPlayer.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if(((EntityDamageByEntityEvent) damagedPlayer.getLastDamageCause()).getDamager() instanceof Player) {
                damager = UHCHandler.getUHCPlayer((Player) ((EntityDamageByEntityEvent) damagedPlayer.getLastDamageCause()).getDamager());
            }
        }

        event.setKeepInventory(true);

        UHCPlayerDeathEvent deathEvent = new UHCPlayerDeathEvent(damaged, damager, event);
        EventUtils.callEventInModule(deathEvent);
        Location loc = event.getEntity().getLocation();
        event.getEntity().spigot().respawn();
        event.getEntity().teleport(loc);

        event.setDeathMessage("");
    }
}
