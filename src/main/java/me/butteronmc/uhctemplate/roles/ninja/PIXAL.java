package me.butteronmc.uhctemplate.roles.ninja;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class PIXAL extends AbstractRole {

    public PIXAL() {
        addPowers(
                new EffectsCommand(UHCHandler.dayTime * 2, -1)
        );
    }

    @Override
    public String getName() {
        return "PIXAL";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "A l'annonce des roles, vous obtenez le pseudo de Zane."
        };
    }

    @Override
    public List<String> additionalDescription() {
        List<String> desc = new ArrayList<>();
        if(UHCHandler.roleList.contains(RoleEnum.ZANE) && RoleEnum.ZANE.getAbstractRole().getUhcPlayer() != null) {
            desc.add(ChatPrefixes.INFO.getMessage("Zane: " + RoleEnum.ZANE.getAbstractRole().getUhcPlayer().getPlayer().getName()));
        }
        else {
            desc.add(ChatPrefixes.ERROR.getMessage("Il n'y a pas de Zane dans cette partie !"));
        }
        return desc;
    }

    private class EffectsCommand extends CommandPower {

        public EffectsCommand(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Analyse";
        }

        @Override
        public String getDescription() {
            return "A l'execution, vous obtenez les effets du joueur ciblé";
        }

        @Override
        public String getArgument() {
            return "analyser";
        }

        @Override
        public boolean onEnable(Player player, String[] args) {
            if(args.length < 2) {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Usage: /ni analyser <joueur>"));
                return false;
            }
            Player target = Bukkit.getPlayer(args[1]);

            if(target == null) {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Veuillez indiquer un joueur !"));
                return false;
            }

            UHCPlayer uhcTarget = UHCHandler.getUHCPlayer(target);

            if(uhcTarget == null) {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Veuillez indiquer un joueur !"));
                return false;
            }

            getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.LIST_HEADER.getMessage("Voici les effets de " + target.getName()));

            getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.LIST_ELEMENT.getMessage("Coeurs: " + target.getMaxHealth()));
            if(uhcTarget.getSpeedEffect() > 0) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.LIST_ELEMENT.getMessage("Speed"));
            }
            for(PotionEffect potionEffect : target.getActivePotionEffects()) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.LIST_ELEMENT.getMessage(potionEffect.getType().getName()));
            }
            return true;
        }
    }
}
