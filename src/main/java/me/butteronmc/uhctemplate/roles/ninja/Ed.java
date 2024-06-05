package me.butteronmc.uhctemplate.roles.ninja;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;

import java.util.ArrayList;
import java.util.List;

public class Ed extends AbstractRole {

    @Override
    public String getName() {
        return "Ed";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous apparaissez dans la liste de Pythor. ",
                "A l'annonce des roles, vous obtenez le pseudo de Jay. ",
                "Vous obtenez force à coté de celui-ci"
        };
    }

    @Override
    public List<String> additionalDescription() {
        List<String> desc = new ArrayList<>();
        if(UHCHandler.roleList.contains(RoleEnum.JAY) && RoleEnum.JAY.getAbstractRole().getUhcPlayer() != null) {
            desc.add(ChatPrefixes.INFO.getMessage("Jay: " + RoleEnum.JAY.getAbstractRole().getUhcPlayer().getPlayer().getName()));
        }
        else {
            desc.add(ChatPrefixes.ERROR.getMessage("Il n'y a pas de Jay dans cette partie !"));
        }
        return desc;
    }

    @Override
    public boolean inList() {
        return true;
    }

    @Override
    public void updateRole(int timer) {
        if(RoleEnum.JAY.getAbstractRole().getUhcPlayer() != null) {
            boolean isNya = (RoleEnum.JAY.getAbstractRole().getUhcPlayer().getPlayer().getLocation().distance(getUhcPlayer().getPlayer().getLocation()) < 15);
            if(isNya && getUhcPlayer().getStrengthEffect() == 0) {
                getUhcPlayer().setStrengthEffect(10);
            }
            else if(!isNya && getUhcPlayer().getStrengthEffect() > 0) {
                getUhcPlayer().setStrengthEffect(0);
            }
        }
    }
}
