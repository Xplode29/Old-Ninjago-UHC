package me.butteronmc.uhctemplate.roles.maitres;

import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.utils.WorldUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Lumiere extends AbstractRole {

    public Lumiere() {
        addPowers(
                new BlindPower(3 * 60, -1)
        );
    }

    @Override
    public String getName() {
        return "Maitre de la Lumière";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Vous avez 12 coeurs permanents"};
    }

    @Override
    public void onGiveRole() {
        getUhcPlayer().getPlayer().setMaxHealth(24);
    }

    @Override
    public void updateRole(int timer) {
        for(ItemStack item : getUhcPlayer().getPlayer().getInventory().getArmorContents()) {
            if(item.getType() != Material.AIR) {
                getUhcPlayer().setSpeedEffect(0);
                getUhcPlayer().hasNoFall = false;
                return;
            }
        }

        getUhcPlayer().setSpeedEffect(20);
        getUhcPlayer().hasNoFall = true;
        getUhcPlayer().getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
        getUhcPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2 * 20, 0, false, false));
    }

    private class BlindPower extends ItemPower {

        public BlindPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.WHITE + "Rayonnement§r";
        }

        @Override
        public String getDescription() {
            return "Faire un clique droit en visant un joueur avec l'item lui donnera blindness pendant 10 secondes (Distance maximum: 20 blocks)";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.NETHER_STAR, ChatColor.WHITE + "Rayonnement§r");
        }

        @Override
        public boolean onEnable(PlayerInteractEvent event) {
            UHCPlayer targetEntity = WorldUtils.getTargetPlayer(event.getPlayer(), 20);
            if(targetEntity != null) {
                targetEntity.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 0));
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Vous avez ébloui " + targetEntity.getPlayer().getName()));
                return true;
            }
            else {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Veuillez viser un joueur !"));
                return false;
            }
        }
    }
}
