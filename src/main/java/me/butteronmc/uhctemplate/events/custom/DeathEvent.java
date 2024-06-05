package me.butteronmc.uhctemplate.events.custom;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DeathEvent implements Listener {

    @EventHandler
    public void onPlayerDie(UHCPlayerDeathEvent event) {
        if(event.getKiller() != null) {
            event.getVictim().onGetKilled(event);
            event.getKiller().onKillPlayer(event);

            UHCHandler.onPlayerDeath(event);
        }
        else {
            UHCHandler.onPlayerDeath(event);
        }

        for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers) {
            uhcPlayer.onPlayerDie(event);
            uhcPlayer.getPlayer().showPlayer(event.getVictim().getPlayer());
        }
    }
}
