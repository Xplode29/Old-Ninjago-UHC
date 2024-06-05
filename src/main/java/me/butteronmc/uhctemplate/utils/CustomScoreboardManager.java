package me.butteronmc.uhctemplate.utils;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class CustomScoreboardManager {
    public static void setupScoreboard(Player player, String string) {
        if(player == null) return;
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective obj = board.registerNewObjective(Main.gamemodeName, "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score info = obj.getScore( " ");
        info.setScore(6);

        Score hostName = obj.getScore(UHCHandler.hostPlayer == null ? ChatColor.RED + "No host defined" : "Host: " + Main.mainColor + UHCHandler.hostPlayer.getPlayer().getDisplayName());
        hostName.setScore(5);

        Score separator1 = obj.getScore(String.valueOf(ChatColor.WHITE));
        separator1.setScore(4);

        Score playersLeft = obj.getScore("Players: " + Main.mainColor + UHCHandler.alivePlayers.size() + "/" + UHCHandler.maxPlayers);
        playersLeft.setScore(3);

        Score timerText = obj.getScore(string);
        timerText.setScore(2);

        Score separator2 = obj.getScore(String.valueOf(ChatColor.RESET));
        separator2.setScore(1);

        Score author = obj.getScore(Main.mainColor + String.valueOf(ChatColor.ITALIC) + "@ButterOnPancakes");
        author.setScore(0);

        player.setScoreboard(board);
    }

}
