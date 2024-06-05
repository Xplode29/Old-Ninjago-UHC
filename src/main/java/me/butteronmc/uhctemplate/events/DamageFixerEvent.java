package me.butteronmc.uhctemplate.events;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffectType;

public class DamageFixerEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player && event.getDamager() instanceof Player)) return;

        UHCPlayer damager = UHCHandler.getUHCPlayer((Player) event.getDamager());
        UHCPlayer damaged = UHCHandler.getUHCPlayer((Player) event.getEntity());

        if(damager == null || damaged == null) return;

        if (damager.getPlayer().hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            event.setDamage(event.getDamage() / 1.875f);
        }

        if (damaged.getPlayer().hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            event.setDamage(event.getDamage() / 0.800000011920929D);
        }
        boolean flag = damager.getPlayer().getFallDistance() > 0.0F && !damager.getPlayer().isOnGround() && damager.getPlayer() instanceof EntityLiving && !damager.getPlayer().hasPotionEffect(PotionEffectType.BLINDNESS) && damager.getPlayer().getVehicle() == null;
        if(flag) {
            event.setDamage(event.getDamage() / 1.5);
        }

        event.setDamage(event.getDamage() * (1 + (damager.getStrengthEffect() - damaged.getResiEffect()) / 100f));
    }

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
        if(event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN)
            event.setCancelled(true);
    }
}
