package me.butteronmc.uhctemplate.roles.snakes;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;

import java.util.ArrayList;
import java.util.List;

public class Skalidor extends AbstractRole {

    @Override
    public String getName() {
        return "Skalidor";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez résistance permanent et force 1 la nuit. ",
                "A l'annonce des roles, vous obtenez le pseudo de Pythor."
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
    public void onGiveRole() {
        getUhcPlayer().setResiEffect(20);
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
