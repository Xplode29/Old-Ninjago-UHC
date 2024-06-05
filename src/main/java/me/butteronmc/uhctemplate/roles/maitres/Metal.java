package me.butteronmc.uhctemplate.roles.maitres;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.CampEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import org.bukkit.entity.Entity;

public class Metal extends AbstractRole {

    @Override
    public String getName() {
        return "Maitre du Métal";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez force (15%) et résistance (40%) permanent",
                "Vous avez un passif: pour chaque maitre présent dans un rayon de 50 blocks, vous perdez 5% de force et de résistance"
        };
    }

    @Override
    public void updateRole(int timer) {
        int maitres = 0;
        for(Entity entity : getUhcPlayer().getPlayer().getNearbyEntities(50, 50, 50)) {
            if(entity instanceof org.bukkit.entity.Player) {
                if(getUhcPlayer().getPlayer().equals(entity)) continue;
                UHCPlayer uhcPlayer = UHCHandler.getUHCPlayer((org.bukkit.entity.Player) entity);
                if(uhcPlayer != null && uhcPlayer.getRoleEnum() != null){
                    if(uhcPlayer.getRoleEnum().getCamp() == CampEnum.MAITRES) {
                        maitres++;
                    }
                }
            }
        }

        getUhcPlayer().setStrengthEffect(15 - maitres * 5);
        getUhcPlayer().setResiEffect(40 - maitres * 5);
    }
}
