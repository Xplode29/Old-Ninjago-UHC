package me.butteronmc.uhctemplate.roles.maitres;

import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import org.bukkit.entity.Player;

public class Vitesse extends AbstractRole {

    public Vitesse() {
        addPowers(
                new SpeedCommand(0, -1)
        );
    }

    @Override
    public String getName() {
        return "Maitre de la Vitesse";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Vous n'avez pas d'effets particuliers"};
    }

    private class SpeedCommand extends CommandPower {

        public SpeedCommand(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Vitesse";
        }

        @Override
        public String getDescription() {
            return "Vous pouvez choisir votre pourcentage de vitesse, entre 0% et 50%";
        }

        @Override
        public String getArgument() {
            return "vitesse";
        }

        @Override
        public boolean onEnable(Player player, String[] args) {
            if(args.length < 2) {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Usage: /ni vitesse <nombre>"));
                return false;
            }

            int speed = Integer.parseInt(args[1]);
            if(0 <= speed &&  speed <= 50) {
                getUhcPlayer().setSpeedEffect(speed);
            }
            else {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Veuillez entrer un nombre entre 0 et 50"));
            }

            return false;
        }
    }

}
