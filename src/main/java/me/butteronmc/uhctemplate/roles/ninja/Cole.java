package me.butteronmc.uhctemplate.roles.ninja;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.roles.api.powers.RightClickItemPower;
import me.butteronmc.uhctemplate.utils.ParticlesEffects;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class Cole extends AbstractRole {

    private boolean rockActive = false;
    int coups = 25;

    public Cole() {
        addPowers(
                new RockPower(0, -1),
                new SpinjitzuPower(UHCHandler.dayTime, -1)
        );
    }

    @Override
    public String getName() {
        return "Cole";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Force 1 (20%) permanent"
        };
    }

    @Override
    public void onGiveRole() {
        getUhcPlayer().setStrengthEffect(20);
    }

    @Override
    public void onHitPlayer(UHCPlayer target) {
        if(rockActive && coups > 0) {
            coups--;
        } else if (rockActive) {
            getUhcPlayer().setResiEffect(0);
            rockActive = false;
        }
    }

    @Override
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        coups += 15;
        if(coups > 60) {
            coups = 60;
        }
    }

    private class RockPower extends ItemPower {

        public RockPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.GRAY + "Rock§r";
        }

        @Override
        public String getDescription() {
            return "Lorsque cet item est activé, vous obtenez 20% de résistance. " +
                    "Cet item se désactive après avoir infligé 25 coups. " +
                    "Lorsque vous faites un kill, vous obtenez 15 coups supplémentaires, avec un maximum de 60 coups. " +
                    "Vous pouvez vérifier les coups qu'il vous reste avec un clic gauche.";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.STONE, ChatColor.GRAY + "Rock");
        }

        @Override
        public boolean onEnable(PlayerInteractEvent event) {
            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                rockActive = !rockActive;
                if(rockActive) {
                    if(coups > 0) {
                        getUhcPlayer().setResiEffect(20);
                        getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Passif activé"));
                    }
                    else {
                        getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Vous avez infligé trop de coups"));
                    }
                }
                else {
                    getUhcPlayer().setResiEffect(0);
                    getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Passif désactivé"));
                }
            }
            else {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Il vous reste: " + coups + " coups"));
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
            return ChatColor.BLACK + "Spinjitzu§r";
        }

        @Override
        public String getDescription() {
            return "À l'activation, repousse de 5 blocks tous les joueurs dans un rayon de 4 blocks";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.NETHER_STAR, ChatColor.BLACK + "Spinjitzu");
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

            ParticlesEffects.createSpinjitzuEffect(event.getPlayer(), Color.fromRGB(28, 28, 28));

            return true;
        }
    }
}
