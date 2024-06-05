package me.butteronmc.uhctemplate.tasks;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatModule;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.items.CustomItem;
import me.butteronmc.uhctemplate.items.ItemManager;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.utils.CustomScoreboardManager;
import me.butteronmc.uhctemplate.utils.GraphicUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Score;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainTimer extends BukkitRunnable {

    public int phase; //0: before launch, 1: before teleport, 2: teleporting, 3: after teleporting, 4: after dropped, 5: after role given.
    public int timer;
    int actualPlayer;
    int prevPhaseTime;

    public MainTimer() {
        phase = 0;
        timer = 0;
        prevPhaseTime = 0;
        actualPlayer = 0;
        this.runTaskTimer(Main.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        switch (phase) {
            case 0: //Before launch
                for(Player p : Bukkit.getOnlinePlayers()) {
                    CustomScoreboardManager.setupScoreboard(p, ChatColor.RED + (UHCHandler.alivePlayers.size() < UHCHandler.maxPlayers ? "Waiting for players" : "Waiting for launch"));
                }
                break;

            case 1: //Countdown
                for(Player p : Bukkit.getOnlinePlayers()) {
                    CustomScoreboardManager.setupScoreboard(p, ChatColor.RED + (UHCHandler.alivePlayers.size() < UHCHandler.maxPlayers ? "Waiting for players" : "Waiting for launch"));
                    p.setLevel(UHCHandler.startTime - timer);
                    p.setExp((float) (UHCHandler.startTime - timer) / UHCHandler.startTime);
                    p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1, 1);
                }
                if(timer >= UHCHandler.startTime) {
                    nextPhase();
                }
                break;

            case 2: //Teleport
                for(Player p : Bukkit.getOnlinePlayers()) {
                    CustomScoreboardManager.setupScoreboard(p, ChatColor.RED + (UHCHandler.alivePlayers.size() < UHCHandler.maxPlayers ? "Waiting for players" : "Waiting for launch"));

                    GraphicUtils.sendActionText(p, (actualPlayer + 1) + " / " + UHCHandler.alivePlayers.size());
                }
                Player player = UHCHandler.alivePlayers.get(actualPlayer).getPlayer();
                UHCHandler.teleportInGame(player);
                actualPlayer++;

                if(actualPlayer >= UHCHandler.alivePlayers.size()) {
                    nextPhase();
                }
                break;

            case 3: //Drop
                for(Player p : Bukkit.getOnlinePlayers()) {
                    CustomScoreboardManager.setupScoreboard(p, ChatColor.RED + (UHCHandler.alivePlayers.size() < UHCHandler.maxPlayers ? "Waiting for players" : "Waiting for launch"));

                    if(UHCHandler.getUHCPlayer(p) != null && UHCHandler.alivePlayers.contains(UHCHandler.getUHCPlayer(p))) {
                        p.sendTitle(String.valueOf(UHCHandler.teleportTime - timer), "");
                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 1);
                    }
                }

                if(timer >= UHCHandler.teleportTime) {
                    UHCHandler.dropPlayers();
                    nextPhase();
                }
                break;

            case 4: //Mining
                updateBeforeRoles();
                break;

            case 5: //Pvp
                updateAfterRoles();
                break;
        }

        timer++;
        updateAdditional();
    }

    public void nextPhase() {
        phase++;
        prevPhaseTime = timer;
        timer = 0;
    }

    void updateAdditional() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player == null) { return; }

            //Hold item
            List<ItemStack> itemsInventory = Stream.concat(Arrays.stream(player.getInventory().getContents()), Arrays.stream(player.getInventory().getArmorContents())).collect(Collectors.toList());
            if(player.getInventory().getItemInHand() != null) {
                ItemStack holdItem = player.getInventory().getItemInHand();
                for(CustomItem customItem : ItemManager.customItems) {
                    if(holdItem.equals(customItem.getItem())) {
                        customItem.onHoldInHand(player);
                    }
                    if(itemsInventory.contains(customItem.getItem())) {
                        customItem.onHoldInInventory(player);
                    }
                }
            }
        }

        for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers) {
            uhcPlayer.updatePlayer(timer);
        }
    }

    void updateBeforeRoles() {
        String timeInText = String.format("%dh %dm %ds", (UHCHandler.mainTimer.timer) / 3600, ((UHCHandler.mainTimer.timer) / 60) % 60, (UHCHandler.mainTimer.timer) % 60);
        for(Player p : Bukkit.getOnlinePlayers()) {
            CustomScoreboardManager.setupScoreboard(p, "Time: " + Main.mainColor + timeInText);
        }

        if(timer == UHCHandler.invincibilityTime) { //End of invincibility
            for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers) {
                uhcPlayer.getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("You are no longer invincible !"));
            }
        }

        if(timer >= UHCHandler.rolesTime) { //Give Roles
            List<Integer> range = IntStream.rangeClosed(0, Math.min(UHCHandler.alivePlayers.size(), UHCHandler.roleList.size()) - 1).boxed().collect(Collectors.toList());
            Collections.shuffle(range);
            Collections.shuffle(UHCHandler.alivePlayers);
            Collections.shuffle(UHCHandler.roleList);
            for(int i : range) {
                UHCHandler.givePlayerRole(UHCHandler.alivePlayers.get(i), UHCHandler.roleList.get(i));
            }

            for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers) {
                if(uhcPlayer.getRoleEnum() == null) {
                    uhcPlayer.getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Vous n'avez pas de role ? veuillez le signaler sur discord à ButterOnPancakes"));
                }
                else {
                    ChatModule.rolePresentation(uhcPlayer);
                }
            }
            nextPhase();
        }
    }

    void updateAfterRoles() {
        String timeInText = String.format("%dh %dm %ds", (timer + prevPhaseTime) / 3600, ((timer + prevPhaseTime) / 60) % 60, (timer + prevPhaseTime) % 60);
        for(Player p : Bukkit.getOnlinePlayers()) {
            CustomScoreboardManager.setupScoreboard(p, "Time: " + Main.mainColor + timeInText);
        }

        if(timer % UHCHandler.dayTime == 0) {
            if(timer % (2 * UHCHandler.dayTime) == 0) { //day
                Bukkit.broadcastMessage(
                        ChatColor.BOLD + "" + ChatColor.YELLOW + "Episode " + timer / (2 * UHCHandler.dayTime)
                );
                Bukkit.broadcastMessage(
                        ChatColor.BOLD + "" + ChatColor.YELLOW + "☀ LE JOUR SE LEVE ☀"
                );
                for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers) {
                    if(uhcPlayer.getRoleEnum() == null) continue;
                    uhcPlayer.getRoleEnum().getAbstractRole().onDay();
                }
            }
            else { //night
                Bukkit.broadcastMessage(
                        ChatColor.BOLD + "" + ChatColor.DARK_BLUE + "☽ LA NUIT SE COUCHE ☽"
                );
                for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers) {
                    if(uhcPlayer.getRoleEnum() == null) continue;
                    uhcPlayer.getRoleEnum().getAbstractRole().onNight();
                }
            }
        }
    }
}
