package me.butteronmc.uhctemplate.roles.solo;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Skylor extends AbstractRole {

    boolean clickedOnChat = true;

    public Skylor() {
        addPowers(
                new AddEffectCommand(0, -1)
        );
    }

    @Override
    public String getName() {
        return "Skylor";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez Force et speed permanent",
                "A chaque début d'épisode, vous obtenez les effets et le pseudo d'un joueur aléatoire dans un rayon de 100 blocks. ",
                "A chaque kill, vous pouvez choisir 5% d'un effet de votre choix (avec maximum 60% d'un effet)"
        };
    }

    @Override
    public void onGiveRole() {
        getUhcPlayer().setSpeedEffect(20);
        getUhcPlayer().setStrengthEffect(20);
    }

    @Override
    public void onDay() {
        super.onDay();
        List<UHCPlayer> uhcPlayers = new ArrayList<>();

        for(Entity entity : getUhcPlayer().getPlayer().getNearbyEntities(100, 100, 100)) {
            if(entity instanceof Player) {
                UHCPlayer uhcPlayer = UHCHandler.getUHCPlayer((Player) entity);
                if(uhcPlayer != null && uhcPlayer.getRoleEnum() != null && uhcPlayer != getUhcPlayer()){
                    uhcPlayers.add(uhcPlayer);
                }
            }
        }

        if(uhcPlayers.isEmpty()) {
            getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Il n'y a personne autour de vous"));
            return;
        }

        Collections.shuffle(uhcPlayers);

        UHCPlayer uhcPlayer = uhcPlayers.get(0);
        getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.LIST_HEADER.getMessage("Voici les effets de " + uhcPlayer.getPlayer().getName()));
        for(PotionEffect potionEffect : uhcPlayer.getPlayer().getActivePotionEffects()) {
            getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.LIST_ELEMENT.getMessage(potionEffect.getType().getName()));
        }
    }

    @Override
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        clickedOnChat = false;
        TextComponent speedMessage = new TextComponent(ChatPrefixes.LIST_ELEMENT.getMessage("5% de speed"));
        speedMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ni azxdgfr 0 5"));
        getUhcPlayer().getPlayer().spigot().sendMessage(speedMessage);

        TextComponent strengthMessage = new TextComponent(ChatPrefixes.LIST_ELEMENT.getMessage("5% de force"));
        strengthMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ni azxdgfr 1 5"));
        getUhcPlayer().getPlayer().spigot().sendMessage(strengthMessage);

        TextComponent resiMessage = new TextComponent(ChatPrefixes.LIST_ELEMENT.getMessage("5% de résistance"));
        resiMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ni azxdgfr 2 5"));
        getUhcPlayer().getPlayer().spigot().sendMessage(resiMessage);
    }

    private class AddEffectCommand extends CommandPower {

        public AddEffectCommand(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getArgument() {
            return "azxdgfr";
        }

        @Override
        public boolean isHidden() {
            return true;
        }

        @Override
        public boolean onEnable(Player player, String[] args) {
            if(args.length < 3) {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Vous ne pouvez pas utiliser cette commande !"));
                return false;
            }

            if(clickedOnChat) {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Vous avez déjà choisi un pouvoir !"));
                return false;
            }

            int effect = Integer.parseInt(args[1]);
            int amount = Integer.parseInt(args[2]);

            switch (effect) {
                case 0:
                    getUhcPlayer().addSpeedEffect(amount);
                    break;
                case 1:
                    getUhcPlayer().addStrenghtEffect(amount);
                    break;
                case 2:
                    getUhcPlayer().addResiEffect(amount);
                    break;
            }
            clickedOnChat = true;
            return false;
        }
    }
}
