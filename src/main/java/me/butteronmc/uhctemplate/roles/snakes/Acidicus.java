package me.butteronmc.uhctemplate.roles.snakes;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Acidicus extends AbstractRole {

    private boolean poisonActive = false;

    public Acidicus() {
        addPowers(
                new PoisonCommand(0, -1)
        );
    }

    @Override
    public String getName() {
        return "Acidicus";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous n'êtes pas affecté par les effets négatifs (poison, faiblesse, lenteur, mining fatigue). ",
                "Vous conaissez l'identité de Pythor"
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
        if(getUhcPlayer().getPlayer().hasPotionEffect(PotionEffectType.POISON))
            getUhcPlayer().getPlayer().removePotionEffect(PotionEffectType.POISON);
        if(getUhcPlayer().getPlayer().hasPotionEffect(PotionEffectType.SLOW))
            getUhcPlayer().getPlayer().removePotionEffect(PotionEffectType.SLOW);
        if(getUhcPlayer().getPlayer().hasPotionEffect(PotionEffectType.SLOW_DIGGING))
            getUhcPlayer().getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        if(getUhcPlayer().getPlayer().hasPotionEffect(PotionEffectType.WEAKNESS))
            getUhcPlayer().getPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
    }

    @Override
    public void onHitPlayer(UHCPlayer target) {
        Player targetPlayer = target.getPlayer();
        if(poisonActive) {
            if((new Random()).nextInt(100) < 25) {
                targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 2 * 20, 0));
            }
        }
    }

    private class PoisonCommand extends CommandPower {

        public PoisonCommand(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Venin";
        }

        @Override
        public String getDescription() {
            return "Active / désactive votre passif. Lorsqu'il est activé, vous avez 25% de chances d'infliger poison 1 à la personne frappée";
        }

        @Override
        public String getArgument() {
            return "venin";
        }

        @Override
        public boolean onEnable(Player player, String[] args) {
            poisonActive = !poisonActive;
            if(poisonActive) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Passif activé"));
            }
            else {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Passif désactivé"));
            }
            return false;
        }
    }
}
