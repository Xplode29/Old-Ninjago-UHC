package me.butteronmc.uhctemplate.roles.snakes;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.CampEnum;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import me.butteronmc.uhctemplate.utils.GraphicUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Skales extends AbstractRole {

    int timeToInfect = UHCHandler.dayTime;

    public Skales() {
        addPowers(
                new InfectPower(0, 1)
        );
    }

    @Override
    public String getName() {
        return "Skales";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez force 1 la nuit. ",
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
    public void onDay() {
        super.onDay();
        getUhcPlayer().setStrengthEffect(0);
    }

    @Override
    public void onNight() {
        super.onNight();
        getUhcPlayer().setStrengthEffect(20);
    }

    private class InfectPower extends CommandPower {
        public InfectPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Infection";
        }

        @Override
        public String getDescription() {
            return "En restant pendant une journée à 12 blocs du joueur ciblé, vous pouvez le faire rejoindre votre camp. " +
                    "Vous ne pouvez pas infecter les solitaires et Lloyd. Vous serez informé si l'infection a échoué. ";
        }

        @Override
        public String getArgument() {
            return "infection";
        }

        @Override
        public boolean onEnable(org.bukkit.entity.Player player, String[] args) {
            if(args.length < 2) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Usage: /ni infection <joueur>"));
                return false;
            }

            org.bukkit.entity.Player target = Bukkit.getPlayer(args[1]);
            if(target == null || args[1].equals(getUhcPlayer().getPlayer().getName())) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Veuillez entrer un joueur correct !"));
                return false;
            }

            UHCPlayer targetedPlayer = UHCHandler.getUHCPlayer(target);
            if(targetedPlayer == null || targetedPlayer.getRoleEnum() == null) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Ce joueur n'est pas dans la partie !"));
                return false;
            }

            new InfectRunnable(targetedPlayer);
            getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Vous avez lancé votre infection sur " + targetedPlayer.getPlayer().getName()));

            return true;
        }

        private class InfectRunnable extends BukkitRunnable {

            UHCPlayer targetedPlayer;
            int timer;

            public InfectRunnable(UHCPlayer target) {
                targetedPlayer = target;

                this.runTaskTimer(Main.getInstance(), 0, 20);
            }

            @Override
            public void run() {
                float progress = (float) timer / timeToInfect;
                GraphicUtils.sendActionText(getUhcPlayer().getPlayer(),  GraphicUtils.getProgressBar((int) (progress * 100), 100, 20, '|', ChatColor.GREEN, ChatColor.GRAY));

                if(targetedPlayer.getPlayer().getLocation().distance(getUhcPlayer().getPlayer().getLocation()) < 15) {
                    timer ++;
                }

                if(timer >= timeToInfect) {
                    if(targetedPlayer.getRoleEnum().getCamp() == CampEnum.SOLO || targetedPlayer.getRoleEnum() == RoleEnum.LLOYD) { //Ratée
                        getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("L'infection de " + targetedPlayer.getPlayer().getName() + " a échoué."));
                    }
                    else { //Marche
                        targetedPlayer.getRoleEnum().getAbstractRole().setInfected(true);
                        targetedPlayer.getPlayer().sendMessage("Vous avez été infecté par Skales (" + getUhcPlayer().getPlayer().getName() + "). Vous devez maintenant gagner avec les " + ChatColor.DARK_BLUE + "Serpents§r.");
                        getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("L'infection de " + targetedPlayer.getPlayer().getName() + " est terminée."));
                    }
                    cancel();
                }
            }
        }
    }
}
