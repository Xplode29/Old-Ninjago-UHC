package me.butteronmc.uhctemplate.roles.ninja;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Facteur extends AbstractRole {

    int messageTime = UHCHandler.dayTime;
    boolean canSendMessage = false;
    int messageCount = 0;

    public Facteur() {
        addPowers(
                new LetterCommand(0, -1)
        );
    }

    @Override
    public String getName() {
        return "Facteur";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous avez speed 1 permanent. ",
                "Lorsque vous enlevez votre armure, vous obtenez invisibilité et no fall. "
        };
    }

    @Override
    public void onGiveRole() {
        getUhcPlayer().setSpeedEffect(20);
    }

    @Override
    public void updateRole(int timer) {
        canSendMessage = (timer < (timer / UHCHandler.dayTime) * UHCHandler.dayTime + messageTime);

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
    public void onDay() {
        super.onDay();
        messageCount = 0;
    }

    private class LetterCommand extends CommandPower {

        public LetterCommand(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Lettre";
        }

        @Override
        public String getDescription() {
            return "A chaque début d'épisode, vous avez 5 minutes pour envoyer 2 messages à n'importe quel joueur de la partie";
        }

        @Override
        public String getArgument() {
            return "lettre";
        }

        @Override
        public boolean onEnable(Player player, String[] args) {
            if(canSendMessage && messageCount < 2) {
                if(args.length < 3) {
                    player.sendMessage(ChatPrefixes.ERROR.getMessage("Usage: /ni lettre <joueur> [message]"));
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                UHCPlayer uhcTarget = UHCHandler.getUHCPlayer(target);

                if(uhcTarget == null) {
                    player.sendMessage(ChatPrefixes.ERROR.getMessage("Veuillez indiquer un joueur correct !"));
                    return false;
                }

                List<String> message = Lists.newArrayList(args);
                message.remove(0);
                message.remove(0);

                uhcTarget.getPlayer().sendMessage(ChatPrefixes.INFO.getMessage(
                        "Facteur: " + Joiner.on(" ").join(message)
                ));

                messageCount ++;
            }
            else {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Vous avez déjà envoyé vos deux lettres !"));
            }
            return false;
        }
    }
}
