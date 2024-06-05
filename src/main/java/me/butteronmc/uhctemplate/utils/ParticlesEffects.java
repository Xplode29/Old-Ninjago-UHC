package me.butteronmc.uhctemplate.utils;

import me.butteronmc.uhctemplate.Main;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticlesEffects {

    public static void createSpinjitzuEffect(Player player, Color color) {
        new BukkitRunnable() {
            double height = 0;

            public void run() {
                for(int i = 0; i < 32; i++) {
                    double alpha = i * (Math.PI / 16);

                    Location loc = player.getLocation().add(
                            (0.5 + height) * Math.cos(alpha) / 2,
                            height,
                            (0.5 + height) * Math.sin(alpha) / 2
                    );

                    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                            EnumParticle.REDSTONE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), (float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) 1, 0
                    );
                    for(Entity entity : player.getNearbyEntities(50, 50, 50)) {
                        if(entity instanceof Player) {
                            ((CraftPlayer)entity).getHandle().playerConnection.sendPacket(packet);
                        }
                    }
                    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
                }
                height += 0.2;

                if(height >= 2.0) {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }
}
