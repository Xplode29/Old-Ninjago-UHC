package me.butteronmc.uhctemplate.roles.maitres;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.CampEnum;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Fumee extends AbstractRole {

    private boolean smokePlaced;
    private Location smokeCoords;
    private int zoneTimer = 2 * 60;

    public Fumee() {
        addPowers(
                new SmokePower(10 * 60, -1),
                new HideKillCommand(10 * 60, -1)
        );
    }

    @Override
    public String getName() {
        return "Maitre de la Fumee";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez Force (20%) et 12 coeurs permanents"
        };
    }

    @Override
    public void onGiveRole() {
        getUhcPlayer().getPlayer().setMaxHealth(24);
        getUhcPlayer().setStrengthEffect(20);
    }

    private class SmokePower extends ItemPower {

        SmokeZoneCooldown smokeZoneCooldown;

        public SmokePower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.GRAY + "Ecran de Fumée§r";
        }

        @Override
        public String getDescription() {
            return "À l'activation, vous recouvrez de fumée une zone de 25x25. Dans cette zone, les joueurs ne peuvent pas voir les maitres à plus de 5 blocks";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.NETHER_STAR, ChatColor.GRAY + "Ecran de Fumée§r");
        }

        @Override
        public boolean onEnable(PlayerInteractEvent event) {
            if(smokePlaced) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Vous avez déjà placé votre zone !"));
                return false;
            }

            if(smokeZoneCooldown == null) {
                smokeZoneCooldown = new SmokeZoneCooldown();
            }
            else {
                smokeZoneCooldown.reset();
            }

            smokeCoords = getUhcPlayer().getPlayer().getLocation();
            smokePlaced = true;
            getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Vous avez placé votre zone !"));

            return true;
        }

        private class SmokeZoneCooldown extends BukkitRunnable {
            int timer;
            boolean isRunning;

            public SmokeZoneCooldown() {
                timer = 0;
                isRunning = true;
                this.runTaskTimer(Main.getInstance(), 0, 20);
            }

            @Override
            public void run() {
                if(isRunning) {
                    if(timer > zoneTimer) {
                        isRunning = false;
                        smokePlaced = false;
                    }
                    else {
                        for(RoleEnum roleEnum : UHCHandler.getCampList(CampEnum.MAITRES)) {
                            if(roleEnum.getAbstractRole().getUhcPlayer() == null) continue;
                            if(WorldUtils.isInZone(roleEnum.getAbstractRole().getUhcPlayer().getPlayer(), smokeCoords, 25, 25)) {
                                for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers) {
                                    if(uhcPlayer.getPlayer() == null) continue;
                                    if(uhcPlayer == roleEnum.getAbstractRole().getUhcPlayer()) continue;
                                    if(uhcPlayer.getPlayer().getLocation().distance(roleEnum.getAbstractRole().getUhcPlayer().getPlayer().getLocation()) < 5) {
                                        uhcPlayer.getPlayer().showPlayer(roleEnum.getAbstractRole().getUhcPlayer().getPlayer());
                                    }
                                    else {
                                        uhcPlayer.getPlayer().hidePlayer(roleEnum.getAbstractRole().getUhcPlayer().getPlayer());
                                    }
                                }
                            }
                            else {
                                for(org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                                    p.showPlayer(roleEnum.getAbstractRole().getUhcPlayer().getPlayer());
                                }
                            }
                        }
                    }
                    timer++;
                }
            }

            public void reset() {
                this.timer = 0;
                this.isRunning = true;
            }
        }
    }

    private class HideKillCommand extends CommandPower {

        public HideKillCommand(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Brouillard";
        }

        @Override
        public String getDescription() {
            return "Active / désactive votre passif. Lorsqu'il est activé, vous cachez les kills que vous faites. Les membres de votre camp seront quand même informés";
        }

        @Override
        public String getArgument() {
            return "brouillard";
        }

        @Override
        public boolean onEnable(org.bukkit.entity.Player player, String[] args) {
            getUhcPlayer().hideKills = !getUhcPlayer().hideKills;
            if(getUhcPlayer().hideKills) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Passif activé"));
            }
            else {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Passif désactivé"));
            }
            return false;
        }
    }
}
