package me.butteronmc.uhctemplate.utils;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GraphicUtils {

    public static void sendActionText(Player player, String message){
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor, ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return com.google.common.base.Strings.repeat("" + completedColor + symbol, progressBars)
                + com.google.common.base.Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }

    public static String convertToAccurateTime(int time) {
        if(time % 60 == 0 && time > 0) {
            return (int)(time / 60) + " minutes";
        }
        return time + " secondes";
    }
}
