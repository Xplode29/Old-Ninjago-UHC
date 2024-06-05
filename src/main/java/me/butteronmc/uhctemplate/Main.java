package me.butteronmc.uhctemplate;

import me.butteronmc.uhctemplate.commands.GameModeCommand;
import me.butteronmc.uhctemplate.commands.HostCommands;
import me.butteronmc.uhctemplate.events.*;
import me.butteronmc.uhctemplate.events.custom.DeathEvent;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import me.butteronmc.uhctemplate.items.ItemManager;
import me.butteronmc.uhctemplate.scenarios.ScenariosManager;
import me.butteronmc.uhctemplate.utils.skulls.SkullsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static ChatColor mainColor = ChatColor.BLUE;
    public static String commandPrefix = "ni";
    public static String gamemodeName = ChatColor.RED + "Ninjago UHC";

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("The plugin has started");

        //Listeners
        getServer().getPluginManager().registerEvents(new DamageFixerEvent(), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);

        getServer().getPluginManager().registerEvents(new blockEvents(), this);
        getServer().getPluginManager().registerEvents(new damageEvents(), this);
        getServer().getPluginManager().registerEvents(new joinEvents(), this);
        getServer().getPluginManager().registerEvents(new playerEvents(), this);
        getServer().getPluginManager().registerEvents(new clickEvents(), this);
        getServer().getPluginManager().registerEvents(new inventoryEvents(), this);

        //Commands
        getCommand("host").setExecutor(new HostCommands());
        getCommand("ni").setExecutor(new GameModeCommand());

        //Scenarios
        ScenariosManager.init();

        //Items
        ItemManager.init();

        //Inventories
        InventoriesManager.init();

        //Inventories
        SkullsManager.init();

        UHCHandler.initGame();
    }

    @Override
    public void onDisable() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(player);
            }
        }
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }
}