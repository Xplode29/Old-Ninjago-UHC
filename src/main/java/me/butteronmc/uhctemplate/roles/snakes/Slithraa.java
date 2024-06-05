package me.butteronmc.uhctemplate.roles.snakes;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;

import java.util.ArrayList;
import java.util.List;

public class Slithraa extends AbstractRole {

    boolean hasStrength = false;

    @Override
    public String getName() {
        return "Slithraa";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Lorsque vous faites un kill, vous obtenez 3% de force. ",
                "A l'annonce des roles, vous obtenez le pseudo de Skales. ",
                "Lorqu'il meurt, vous obtenez force la nuit."
        };
    }

    @Override
    public List<String> additionalDescription() {
        List<String> desc = new ArrayList<>();
        if(UHCHandler.roleList.contains(RoleEnum.SKALES) && RoleEnum.SKALES.getAbstractRole().getUhcPlayer() != null) {
            desc.add(ChatPrefixes.INFO.getMessage("Skales: " + RoleEnum.SKALES.getAbstractRole().getUhcPlayer().getPlayer().getName()));
        }
        else {
            desc.add(ChatPrefixes.ERROR.getMessage("Il n'y a pas de Skales dans cette partie !"));
        }
        return desc;
    }

    @Override
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        getUhcPlayer().addStrenghtEffect(3);
    }

    @Override
    public void onDay() {
        super.onDay();
        getUhcPlayer().setStrengthEffect(0);
    }

    @Override
    public void onNight() {
        super.onNight();
        if(hasStrength) {
            getUhcPlayer().setStrengthEffect(20);
        }
    }

    @Override
    public void onPlayerDie(UHCPlayerDeathEvent event) {
        if(event.getVictim().getRoleEnum() == RoleEnum.SLITHRAA) {
            hasStrength = true;
            getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Vous avez obtenu force la nuit !"));
        }
    }
}
