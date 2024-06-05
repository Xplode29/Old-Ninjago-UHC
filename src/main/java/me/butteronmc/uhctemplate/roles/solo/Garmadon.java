package me.butteronmc.uhctemplate.roles.solo;

import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.roles.api.powers.RightClickItemPower;
import me.butteronmc.uhctemplate.utils.ParticlesEffects;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.List;

public class Garmadon extends AbstractRole {

    boolean killedLloyd = false;
    boolean killedWu = false;

    public Garmadon() {
        addPowers(
                new SwordPower(0, -1)
        );
    }

    @Override
    public String getName() {
        return "Garmadon";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez speed, résistance (20%) et 12 coeurs permanents et force de nuit. ",
                "Lorsque vous tuez Wu, vous obtenez force le jour. ",
                "Lorsque vous tuez Lloyd, vous obtenez le spinjitzu. Vous avez le droit d'utiliser la pièce p3 de lloyd"
        };
    }

    @Override
    public void onGiveRole() {
        getUhcPlayer().setSpeedEffect(20);
        getUhcPlayer().setResiEffect(20);
        getUhcPlayer().getPlayer().setMaxHealth(24);
    }

    @Override
    public void onDay() {
        super.onDay();
        if(killedWu) {
            getUhcPlayer().setStrengthEffect(20);
        }
        else {
            getUhcPlayer().setStrengthEffect(0);
        }
    }

    @Override
    public void onNight() {
        super.onNight();
        getUhcPlayer().setStrengthEffect(20);
    }

    @Override
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(event.getVictim().getRoleEnum() == null) return;

        if(event.getVictim().getRoleEnum() == RoleEnum.LLOYD) {
            killedLloyd = true;
            ItemPower spinjitzu = new SpinjitzuPower(10 * 60, -1);
            addPowers(spinjitzu);
            getUhcPlayer().getPlayer().getInventory().addItem(spinjitzu.getItemStack());
        }

        if(event.getVictim().getRoleEnum() == RoleEnum.WU) {
            killedWu = true;
            getUhcPlayer().setStrengthEffect(20);
        }
    }

    private class SwordPower extends ItemPower {

        public SwordPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.YELLOW + "Bâton§r";
        }

        @Override
        public String getDescription() {
            return "Une épée enchantée sharpness 4";
        }

        @Override
        public boolean cancelEvent() {
            return false;
        }

        @Override
        public boolean isVanilla() {
            return true;
        }

        @Override
        public ItemStack getItemStack() {
            ItemStack stickSword = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta itemMeta = stickSword.getItemMeta();

            itemMeta.spigot().setUnbreakable(true);
            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
            itemMeta.setDisplayName(ChatColor.YELLOW + "Bâton");
            stickSword.setItemMeta(itemMeta);
            return stickSword;
        }
    }

    private class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.DARK_BLUE + "Spinjitzu§r";
        }

        @Override
        public String getDescription() {
            return "À l'activation, repousse de 5 blocks tous les joueurs dans un rayon de 4 blocks";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.NETHER_STAR, ChatColor.DARK_BLUE + "Spinjitzu");
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

            ParticlesEffects.createSpinjitzuEffect(event.getPlayer(), Color.fromRGB(214, 28, 214));

            return true;
        }
    }
}
