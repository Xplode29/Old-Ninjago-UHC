package me.butteronmc.uhctemplate.events.custom;

import me.butteronmc.uhctemplate.Main;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.HandlerList;

public class EventUtils {

    public static void callEventInModule(Event event) {
        HandlerList.getRegisteredListeners(Main.getInstance()).forEach(registeredListener -> {
            try {
                registeredListener.callEvent(event);
            } catch (EventException e) {
                e.printStackTrace();
            }
        });
    }
}