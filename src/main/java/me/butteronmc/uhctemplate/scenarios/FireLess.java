package me.butteronmc.uhctemplate.scenarios;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FireLess extends Scenario implements Listener {
    public FireLess() {
        super("FireLess", Material.FLINT_AND_STEEL);
    }

    @EventHandler
    public void onTakeDamage(EntityDamageEvent event) {
        if(event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
            event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
            event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            event.setCancelled(true);
        }
    }
}
