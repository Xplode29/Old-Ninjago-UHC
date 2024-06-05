package me.butteronmc.uhctemplate.roles.ninja;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import me.butteronmc.uhctemplate.roles.api.powers.RightClickItemPower;
import me.butteronmc.uhctemplate.utils.ParticlesEffects;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class Jay extends AbstractRole {

    private boolean foudreActive = false;

    public Jay() {
        addPowers(
                new LightningCommand(0, -1),
                new SpinjitzuPower(UHCHandler.dayTime, -1)
        );
    }

    @Override
    public String getName() {
        return "Jay";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez 30% de speed permanent"
        };
    }

    @Override
    public void onHitPlayer(UHCPlayer target) {
        Player targetPlayer = target.getPlayer();
        if(foudreActive) {
            if((new Random()).nextInt(100) <= 15) {
                targetPlayer.getWorld().playSound(targetPlayer.getLocation(), Sound.EXPLODE, 3.0f, 1.0f);
                targetPlayer.getWorld().strikeLightningEffect(targetPlayer.getLocation());

                targetPlayer.setHealth(Math.max(0, targetPlayer.getHealth() - 1.5));
            }
        }
    }

    @Override
    public void onGiveRole() {
        getUhcPlayer().setSpeedEffect(30);
    }

    private class LightningCommand extends CommandPower {

        public LightningCommand(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Foudre";
        }

        @Override
        public String getDescription() {
            return "Active / désactive votre passif. Lorsqu'il est activé, vous avez 15% de chances de faire tomber un éclair qui inflige 1,5 coeurs de dégâts sur la personne frappé";
        }

        @Override
        public String getArgument() {
            return "foudre";
        }

        @Override
        public boolean onEnable(Player player, String[] args) {
            foudreActive = !foudreActive;
            if(foudreActive) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Passif activé"));
            }
            else {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Passif désactivé"));
            }
            return false;
        }
    }

    private class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.BLUE + "Spinjitzu§r";
        }

        @Override
        public String getDescription() {
            return "À l'activation, repousse de 5 blocks tous les joueurs dans un rayon de 4 blocks";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.NETHER_STAR, ChatColor.BLUE + "Spinjitzu");
        }

        @Override
        public boolean onEnable(PlayerInteractEvent event) {
            List<Entity> nearbyEntities = event.getPlayer().getNearbyEntities(4, 2, 4);
            Location center = event.getPlayer().getLocation();
            for(Entity entity : nearbyEntities) {
                double angle = Math.atan2(entity.getLocation().getZ() - center.getZ(), entity.getLocation().getX() - center.getX());
                Vector newVelocity = new Vector(
                        1.5 * Math.cos(angle),
                        0.5, //* Math.signum(entity.getLocation().getY() - center.getY()),
                        1.5 * Math.sin(angle)
                );
                entity.setVelocity(newVelocity);
            }

            ParticlesEffects.createSpinjitzuEffect(event.getPlayer(), Color.fromRGB(28, 28, 214));

            return true;
        }
    }
}
