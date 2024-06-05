package me.butteronmc.uhctemplate;

import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.items.ItemManager;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.CampEnum;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.roles.api.powers.Power;
import me.butteronmc.uhctemplate.tasks.CleanTreeTask;
import me.butteronmc.uhctemplate.tasks.KillTimer;
import me.butteronmc.uhctemplate.tasks.MainTimer;
import me.butteronmc.uhctemplate.tasks.PreGenTask;
import me.butteronmc.uhctemplate.utils.RandomHelper;
import me.butteronmc.uhctemplate.utils.WorldUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UHCHandler {

    public static List<UHCPlayer> alivePlayers;
    public static List<UHCPlayer> deadPlayers;

    public static UHCPlayer hostPlayer;

    public static Location spawn, center;
    public static World playWorld;
    public static boolean mapGenerated = false, mapPregenerated = false;

    public static boolean isGenerating = false;

    public static MainTimer mainTimer;
    public static List<RoleEnum> roleList = new ArrayList<>(); //Arrays.asList(RoleEnum.values())
    public static List<ItemStack> startingItems = new ArrayList<>();

    public static int
            sizeMap = 500,
            sizeRoofed = 300,
            maxPlayers = 39,
            startTime = 5,
            teleportTime = 5,
            timeToDie = 5,
            invincibilityTime = 10,
            dayTime = 5 * 60,
            rolesTime = 20; // 6 to 60

    public static UHCPlayer modifyInvPlayer;

    public static void initGame() {
        //Stops all tasks
        for(BukkitTask task : Bukkit.getScheduler().getPendingTasks()) {
            task.cancel();
        }

        alivePlayers = new ArrayList<>();
        deadPlayers = new ArrayList<>();

        mainTimer = new MainTimer();

        if(Bukkit.getWorld("spawn") == null) {
            WorldCreator worldCreator = new WorldCreator("spawn");
            worldCreator.createWorld();
        }
        spawn = new Location(Bukkit.getWorld("spawn"), 0, 150, 0);
        //Build the spawn
        int spawnSize = 20, spawnHeight = 5;
        WorldUtils.fillBlocks(spawn.getWorld(), spawn.getBlockX() - spawnSize/2, spawn.getBlockY() - 1, spawn.getBlockZ() - spawnSize/2, spawnSize, spawnHeight, spawnSize, Material.BARRIER);
        WorldUtils.fillBlocks(spawn.getWorld(), spawn.getBlockX() - spawnSize/2 + 1, spawn.getBlockY(), spawn.getBlockZ() - spawnSize/2 + 1, spawnSize - 2, spawnHeight - 2, spawnSize - 2, Material.AIR);

        for(Entity entity : spawn.getWorld().getEntities()) {
            if(!(entity instanceof org.bukkit.entity.Player)) {
                entity.remove();
            }
        }

        for(org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()){
            clearPlayer(p);
            teleportToSpawn(p);

            UHCHandler.alivePlayers.add(new UHCPlayer(p));
        }
    }

    public static void resetGame() {
        //Stops all tasks
        for(BukkitTask task : Bukkit.getScheduler().getPendingTasks()) {
            task.cancel();
        }

        mainTimer = new MainTimer();

        for(org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()){
            clearPlayer(p);
            teleportToSpawn(p);

            UHCPlayer uhcPlayer = getUHCPlayer(p);
            if(uhcPlayer == null) uhcPlayer = new UHCPlayer(p);
            if(deadPlayers.contains(uhcPlayer)) {
                deadPlayers.remove(uhcPlayer);
                alivePlayers.add(uhcPlayer);
            }
            else if (!alivePlayers.contains(uhcPlayer)) {
                alivePlayers.add(uhcPlayer);
            }

            if(uhcPlayer.getRoleEnum() != null) {
                removePlayerRole(uhcPlayer);
            }
        }
    }

    public static void generateMap() {
        if(Bukkit.getWorld("arena") != null) {
            for(Chunk c : Bukkit.getWorld("arena").getLoadedChunks()) {
                c.unload();
            }
            Bukkit.unloadWorld(Bukkit.getWorld("arena"), true);
        }
        try {
            File folder = new File("arena");
            FileUtils.deleteDirectory(folder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        WorldCreator worldCreator = new WorldCreator("arena");
        playWorld = worldCreator.createWorld();

        center = new Location(playWorld, 0, 150, 0);

        isGenerating = true;
        new CleanTreeTask(sizeMap, playWorld);
    }

    public static void pregenMap() {
        isGenerating = true;
        new PreGenTask(sizeMap, playWorld);
    }

    public static void launchGame() {
        playWorld.getWorldBorder().setCenter(0, 0);
        playWorld.getWorldBorder().setSize(2 * sizeMap);

        playWorld.setTime(0);

        for(Entity entity : playWorld.getEntities()) {
            if(!(entity instanceof org.bukkit.entity.Player)) {
                entity.remove();
            }
        }

        mainTimer.nextPhase();
    }

    public static void teleportInGame(org.bukkit.entity.Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);

        Location tpLoc = new Location(
                UHCHandler.playWorld,
                RandomHelper.getRandomInt(-UHCHandler.sizeMap + 50, UHCHandler.sizeMap - 50) + 0.5,
                150,
                RandomHelper.getRandomInt(-UHCHandler.sizeMap + 50, UHCHandler.sizeMap - 50) + 0.5
        );
        player.teleport(tpLoc);
        player.setCompassTarget(center);

        //Build the platform
        int platformSize = 3;
        WorldUtils.fillBlocks(UHCHandler.playWorld, player.getLocation().getBlockX() - platformSize/2, player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ() - platformSize/2, platformSize, 1, platformSize, Material.STAINED_GLASS);
    }

    public static void dropPlayers() {
        for(UHCPlayer uhcPlayer : alivePlayers){
            org.bukkit.entity.Player player = uhcPlayer.getPlayer();
            //Erase the platform
            int platformSize = 3;
            WorldUtils.fillBlocks(playWorld, player.getLocation().getBlockX() - platformSize/2, player.getLocation().getBlockY() - 2, player.getLocation().getBlockZ() - platformSize/2, platformSize, 2, platformSize, Material.AIR);

            for(int index = 0; index < startingItems.size(); index++) {
                player.getInventory().setItem(index, startingItems.get(index));
            }

            player.sendTitle("Good Luck !", "");
            player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
        }
    }

    public static void stopGame() {
        resetGame();
        hostPlayer.getPlayer().getInventory().setItem(4, ItemManager.menuItem.getItem());
    }

    public static void teleportToSpawn(org.bukkit.entity.Player player) {
        if(player == null) return;
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setWalkSpeed(0.2f);

        if(mainTimer.phase > 2) {
            player.setGameMode(GameMode.SPECTATOR);

            if(center != null) {
                (new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.teleport(center);
                    }
                }).runTaskLater(Main.getInstance(), 1);
            }
        }
        else {
            player.setGameMode(GameMode.SURVIVAL);

            if(spawn != null) {
                (new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.teleport(spawn);

                        if(hostPlayer != null) {
                            if(player.getUniqueId().equals(hostPlayer.getPlayerUUID())) {
                                hostPlayer = getUHCPlayer(player);
                                player.getInventory().setItem(4, ItemManager.menuItem.getItem());
                            }
                        }
                    }
                }).runTaskLater(Main.getInstance(), 1);
            }
        }
    }

    public static void clearPlayer(org.bukkit.entity.Player player) {
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        for(PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
        player.setLevel(0);
        player.setTotalExperience(0);
        player.setExp(0);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

    public static void setPlayerHost(UHCPlayer uhcPlayer) {
        org.bukkit.entity.Player player = uhcPlayer.getPlayer();

        if (UHCHandler.hostPlayer != null && UHCHandler.hostPlayer != player) {
            org.bukkit.entity.Player PrevHost = UHCHandler.hostPlayer.getPlayer();

            PrevHost.getInventory().remove(ItemManager.menuItem.getItem());
        }

        player.getInventory().setItem(4, ItemManager.menuItem.getItem());

        UHCHandler.hostPlayer = uhcPlayer;
    }

    public static void onPlayerDeath(UHCPlayerDeathEvent event) {
        if(mainTimer.phase < 5) {
            revivePlayer(event.getVictim());
        }
        else {
            if(!deadPlayers.contains(event.getVictim())) {
                deadPlayers.add(event.getVictim());
            }
            alivePlayers.remove(event.getVictim());

            event.getVictim().getPlayer().setGameMode(GameMode.SPECTATOR);

            if(event.getKiller() != null) {
                new KillTimer(event.getVictim(), event.getKiller().hideKills);
            }
            else {
                new KillTimer(event.getVictim(), false);
            }
        }

        if(event.getKiller() != null) {
            UHCHandler.hostPlayer.getPlayer().sendMessage(ChatPrefixes.INFO.getMessage(
                    "Player " + event.getVictim().getPlayer().getDisplayName() + " has been killed by " + event.getKiller().getPlayer().getName()
            ));
        }
        else {
            UHCHandler.hostPlayer.getPlayer().sendMessage(ChatPrefixes.INFO.getMessage(
                    "Player " + event.getVictim().getPlayer().getDisplayName() + " has been killed by " + event.getVictim().getPlayer().getLastDamageCause().getCause()
            ));
        }
    }

    public static void revivePlayer(UHCPlayer uhcPlayer) {
        uhcPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
        uhcPlayer.getPlayer().setHealth(uhcPlayer.getPlayer().getMaxHealth());
        uhcPlayer.getPlayer().setFoodLevel(20);

        if(!deadPlayers.contains(uhcPlayer)) {
            alivePlayers.add(uhcPlayer);
        }
        deadPlayers.remove(uhcPlayer);
    }

    public static UHCPlayer getUHCPlayer(org.bukkit.entity.Player player) {
        for(UHCPlayer uhcPlayer : alivePlayers) {
            if(uhcPlayer.getPlayerUUID().equals(player.getUniqueId())) {
                return uhcPlayer;
            }
        }
        for(UHCPlayer uhcPlayer : deadPlayers) {
            if(uhcPlayer.getPlayerUUID().equals(player.getUniqueId())) {
                return uhcPlayer;
            }
        }
        return null;
    }

    public static void givePlayerRole(UHCPlayer uhcPlayer, RoleEnum newRole) {
        removePlayerRole(uhcPlayer);

        for(Power power : newRole.getAbstractRole().getPowers()) {
            if(power instanceof ItemPower) {
                if(((ItemPower) power).shouldGiveAtDistribution()) {
                    uhcPlayer.getPlayer().getInventory().addItem(((ItemPower) power).getItemStack());
                }
            }
        }
        uhcPlayer.setRoleEnum(newRole);
        newRole.getAbstractRole().setUhcPlayer(uhcPlayer);

        newRole.getAbstractRole().onGiveRole();
    }

    public static void removePlayerRole(UHCPlayer uhcPlayer) {
        if(uhcPlayer.getRoleEnum() == null) {
            return;
        }

        for(Power power : uhcPlayer.getRoleEnum().getAbstractRole().getPowers()) {
            if(power instanceof ItemPower) {
                if(((ItemPower) power).shouldGiveAtDistribution()) {
                    uhcPlayer.getPlayer().getInventory().remove(((ItemPower) power).getItemStack());
                }
            }
        }

        uhcPlayer.setSpeedEffect(0);
        uhcPlayer.setStrengthEffect(0);
        uhcPlayer.setResiEffect(0);
        uhcPlayer.getPlayer().setMaxHealth(20);

        for(org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(uhcPlayer.getPlayer());
        }

        uhcPlayer.getRoleEnum().getAbstractRole().setUhcPlayer(null);
        uhcPlayer.setRoleEnum(null);
    }

    public static List<RoleEnum> getCampList(CampEnum campEnum) {
        List<RoleEnum> campList = new ArrayList<>();
        for(RoleEnum roleEnum : roleList) {
            if(roleEnum.getCamp() == campEnum) {
                campList.add(roleEnum);
            }
        }
        return campList;
    }
}
