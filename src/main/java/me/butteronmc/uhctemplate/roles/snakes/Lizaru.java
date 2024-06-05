package me.butteronmc.uhctemplate.roles.snakes;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.utils.WorldUtils;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Lizaru extends AbstractRole {

    public Lizaru() {
        addPowers(
                new BitePower(UHCHandler.dayTime, -1)
        );
    }

    @Override
    public String getName() {
        return "Lizaru";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Lorsque vous faites un kill, vous obtenez 3% de force.",
                "A l'annonce des roles, vous obtenez le pseudo de Acidicus."
        };
    }

    @Override
    public List<String> additionalDescription() {
        List<String> desc = new ArrayList<>();
        if(UHCHandler.roleList.contains(RoleEnum.ACIDICUS) && RoleEnum.ACIDICUS.getAbstractRole().getUhcPlayer() != null) {
            desc.add(ChatPrefixes.INFO.getMessage("Acidicus: " + RoleEnum.ACIDICUS.getAbstractRole().getUhcPlayer().getPlayer().getName()));
        }
        else {
            desc.add(ChatPrefixes.ERROR.getMessage("Il n'y a pas de Acidicus dans cette partie !"));
        }
        return desc;
    }

    @Override
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        getUhcPlayer().addStrenghtEffect(3);
    }

    private class BitePower extends ItemPower {

        public BitePower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "§3Morsure§r";
        }

        public String getDescription() {
            return "Faire un clique droit en visant un joueur avec l'item lui donnera poison pendant 10 secondes (Distance maximum: 20 blocks)";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.NETHER_STAR, "§3Morsure");
        }

        @Override
        public boolean onEnable(PlayerInteractEvent event) {
            UHCPlayer targetEntity = WorldUtils.getTargetPlayer(event.getPlayer(), 20);
            if(targetEntity != null) {
                targetEntity.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 10, 0));
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Vous avez mordu " + targetEntity.getPlayer().getName()));
                return true;
            }
            else {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Veuillez viser un joueur !"));
                return false;
            }
        }
    }
}
