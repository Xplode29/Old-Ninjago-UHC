package me.butteronmc.uhctemplate.roles.ninja;

import me.butteronmc.uhctemplate.UHCHandler;
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
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

public class Kai extends AbstractRole {
    public Kai() {
        addPowers(
                new FireAspectBook(0, -1),
                new BowFlame(0, -1),
                new SpinjitzuPower(UHCHandler.dayTime, -1)
        );
    }

    @Override
    public String getName() {
        return "Kai";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Fire Resistance permanent ainsi que 10% de force a 10 blocs de nya"
        };
    }

    @Override
    public RoleEnum getDuo() {
        return RoleEnum.NYA;
    }

    @Override
    public void updateRole(int timer) {
        getUhcPlayer().getPlayer().removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        getUhcPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2 * 20, 0));

        if(RoleEnum.NYA.getAbstractRole().getUhcPlayer() != null) {
            boolean isNya = (RoleEnum.NYA.getAbstractRole().getUhcPlayer().getPlayer().getLocation().distance(getUhcPlayer().getPlayer().getLocation()) < 15);
            if(isNya && getUhcPlayer().getStrengthEffect() == 0) {
                getUhcPlayer().setStrengthEffect(10);
            }
            else if(!isNya && getUhcPlayer().getStrengthEffect() > 0) {
                getUhcPlayer().setStrengthEffect(0);
            }
        }
    }

    private class FireAspectBook extends ItemPower {

        public FireAspectBook(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public ItemStack getItemStack() {
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta itemMeta = book.getItemMeta();
            ((EnchantmentStorageMeta) itemMeta).addStoredEnchant(Enchantment.FIRE_ASPECT, 1, true);
            book.setItemMeta(itemMeta);
            return book;
        }

        @Override
        public String getName() {
            return "§5Livre Enchanté§r";
        }

        @Override
        public String getDescription() {
            return "Un livre enchanté fire aspect 1. Il est possible de le fusionner avec son épée en diamant.";
        }

        @Override
        public boolean cancelEvent() {
            return false;
        }

        @Override
        public boolean isVanilla() {
            return true;
        }
    }

    private class BowFlame extends ItemPower {

        public BowFlame(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public ItemStack getItemStack() {
            ItemStack book = new ItemStack(Material.BOW);
            ItemMeta itemMeta = book.getItemMeta();
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 3, true);
            itemMeta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
            book.setItemMeta(itemMeta);
            return book;
        }

        @Override
        public String getName() {
            return "§cArc de feu§r";
        }

        @Override
        public String getDescription() {
            return "Un arc enchanté power 3 et flame";
        }

        @Override
        public boolean cancelEvent() {
            return false;
        }

        @Override
        public boolean isVanilla() {
            return true;
        }
    }

    private class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.RED + "Spinjitzu§r";
        }

        @Override
        public String getDescription() {
            return "À l'activation, repousse de 5 blocks tous les joueurs dans un rayon de 4 blocks";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.NETHER_STAR, ChatColor.RED + "Spinjitzu");
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

            ParticlesEffects.createSpinjitzuEffect(event.getPlayer(), Color.fromRGB(214, 28, 28));

            return true;
        }
    }
}
