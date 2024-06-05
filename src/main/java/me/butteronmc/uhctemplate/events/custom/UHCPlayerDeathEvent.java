package me.butteronmc.uhctemplate.events.custom;

import me.butteronmc.uhctemplate.player.UHCPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

public class UHCPlayerDeathEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private UHCPlayer victim;
    private UHCPlayer killer;

    private PlayerDeathEvent event;
    private boolean cancelled;

    public UHCPlayerDeathEvent(UHCPlayer victim, UHCPlayer killer, PlayerDeathEvent event) {
        this.victim = victim;
        this.killer = killer;
        this.event = event;
    }

    public PlayerDeathEvent getOriginalEvent() {
        return event;
    }

    public UHCPlayer getVictim() {
        return victim;
    }

    public UHCPlayer getKiller() {
        return killer;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
