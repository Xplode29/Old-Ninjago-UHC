package me.butteronmc.uhctemplate.roles.snakes;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Bytar extends AbstractRole {

    @Override
    public String getName() {
        return "Bytar";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous avez force 1 la nuit. ",
                "Lorsque vous faites un kill, vous obtenez 3% de résistance.",
                "A l'annonce des roles, vous obtenez le pseudo de Skalidor."
        };
    }

    @Override
    public List<String> additionalDescription() {
        List<String> desc = new ArrayList<>();
        if(UHCHandler.roleList.contains(RoleEnum.SKALIDOR) && RoleEnum.SKALIDOR.getAbstractRole().getUhcPlayer() != null) {
            desc.add(ChatPrefixes.INFO.getMessage("Skalidor: " + RoleEnum.SKALIDOR.getAbstractRole().getUhcPlayer().getPlayer().getName()));
        }
        else {
            desc.add(ChatPrefixes.ERROR.getMessage("Il n'y a pas de Skalidor dans cette partie !"));
        }
        return desc;
    }

    @Override
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        getUhcPlayer().addResiEffect(3);
    }

    @Override
    public void onDay() {
        super.onDay();
        getUhcPlayer().setStrengthEffect(0);
        getUhcPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * 20, 0));
    }

    @Override
    public void onNight() {
        super.onNight();
        getUhcPlayer().setStrengthEffect(20);
    }
}
