package me.butteronmc.uhctemplate.roles.snakes;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.RightClickItemPower;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Pythor extends AbstractRole {

    int powerUsedTimer = 0;
    boolean powerActive = false;

    public Pythor() {
        addPowers(
                new DevoreurPower(UHCHandler.dayTime * 2, 2)
        );
    }

    @Override
    public String getName() {
        return "Pythor";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez force 1 et speed 1 permanents. ",
                "A l'annonce des roles, vous obtenez la liste des chefs serpents. Attention, il y a un traitre dans cette liste (Ed)."
        };
    }


    @Override
    public List<String> additionalDescription() {
        List<String> namesList = new ArrayList<>();
        namesList.add(ChatPrefixes.LIST_HEADER.getMessage("Liste des chefs serpents:"));
        for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers) {
            if(uhcPlayer.getRoleEnum() != null) {
                if(uhcPlayer.getRoleEnum().getAbstractRole().inList()) {
                    namesList.add(ChatPrefixes.LIST_ELEMENT.getMessage(uhcPlayer.getPlayer().getName()));
                }
            }
        }
        return namesList;
    }

    @Override
    public void onGiveRole() {
        getUhcPlayer().setStrengthEffect(20);
        getUhcPlayer().setSpeedEffect(20);
    }

    @Override
    public void updateRole(int timer) {
        if(powerActive && timer > powerUsedTimer + 2 * 60) {
            getUhcPlayer().addSpeedEffect(-20);
            getUhcPlayer().addResiEffect(-20);
            powerActive = false;
            getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Vous avez perdu votre forme Grand Dévoreur !"));
        }
    }

    private class DevoreurPower extends RightClickItemPower {

        public DevoreurPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.DARK_PURPLE + "Grand Dévoreur§r";
        }

        @Override
        public String getDescription() {
            return "À l'activation, vous obtenez speed 2 et résistance 1 pendant 2 minutes";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.NETHER_STAR, getName());
        }

        @Override
        public boolean onEnable(PlayerInteractEvent event) {
            if(powerActive) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Attendez la fin de votre pouvoir !"));
            }
            else {
                powerActive = true;
                powerUsedTimer = UHCHandler.mainTimer.timer;

                getUhcPlayer().addSpeedEffect(20);
                getUhcPlayer().addResiEffect(20);
            }
            return true;
        }
    }
}
