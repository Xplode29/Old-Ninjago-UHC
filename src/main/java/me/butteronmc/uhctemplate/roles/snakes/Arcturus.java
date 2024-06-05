package me.butteronmc.uhctemplate.roles.snakes;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Arcturus extends AbstractRole {

    @Override
    public String getName() {
        return "Bytar";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous avez force 1 la nuit. ",
                "Lorsque vous enlevez votre armure, vous obtenez invisibilité et no fall. ",
                "A l'annonce des roles, vous obtenez le pseudo de Pythor. "
        };
    }

    @Override
    public List<String> additionalDescription() {
        List<String> desc = new ArrayList<>();
        if(UHCHandler.roleList.contains(RoleEnum.PYTHOR) && RoleEnum.PYTHOR.getAbstractRole().getUhcPlayer() != null) {
            desc.add(ChatPrefixes.INFO.getMessage("Pythor: " + RoleEnum.PYTHOR.getAbstractRole().getUhcPlayer().getPlayer().getName()));
        }
        else {
            desc.add(ChatPrefixes.ERROR.getMessage("Il n'y a pas de Pythor dans cette partie !"));
        }
        return desc;
    }

    @Override
    public boolean inList() {
        return true;
    }

    @Override
    public void updateRole(int timer) {

        for(ItemStack item : getUhcPlayer().getPlayer().getInventory().getArmorContents()) {
            if(item.getType() != Material.AIR) {
                getUhcPlayer().hasNoFall = false;
                return;
            }
        }

        getUhcPlayer().hasNoFall = true;
        getUhcPlayer().getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
        getUhcPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2 * 20, 0, false, false));
    }

    @Override
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        getUhcPlayer().addResiEffect(3);
    }

    @Override
    public void onDay() {
        super.onDay();
        getUhcPlayer().setStrengthEffect(0);
    }

    @Override
    public void onNight() {
        super.onNight();
        getUhcPlayer().setStrengthEffect(20);
    }
}
