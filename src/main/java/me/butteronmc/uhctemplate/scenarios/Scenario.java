package me.butteronmc.uhctemplate.scenarios;

import me.butteronmc.uhctemplate.Main;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class Scenario {
    public String name;
    public Material icon;
    public boolean enabled;

    public Scenario(String name, Material icon) {
        this.name = name;
        this.icon = icon;
    }

    public void onUpdate() {

    }

    public void onToggle() {
        enabled = !enabled;
        if(enabled) {
            Main.getPlugin(Main.class).getServer().getPluginManager().registerEvents((Listener) this, Main.getPlugin(Main.class));
        }
        else {
            HandlerList.unregisterAll((Listener) this);
        }
    }
}
