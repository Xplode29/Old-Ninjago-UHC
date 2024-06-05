package me.butteronmc.uhctemplate.roles.ninja;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import me.butteronmc.uhctemplate.roles.api.powers.RightClickItemPower;
import me.butteronmc.uhctemplate.utils.ParticlesEffects;
import me.butteronmc.uhctemplate.utils.WorldUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Zane extends AbstractRole {

    private boolean gelZonePlaced;
    private Location gelZoneCoords;
    private final int zoneTimer = 2 * 60;

    public Zane() {
        addPowers(
                new FauconCommand(UHCHandler.dayTime / 2, -1),
                new FreezePower(UHCHandler.dayTime, -1),
                new SpinjitzuPower(UHCHandler.dayTime, -1)
        );
    }

    @Override
    public String getName() {
        return "Zane";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Vous n'avez pas d'effets particuliers"};
    }

    @Override
    public void updateRole(int timer) {
        if(gelZonePlaced && gelZoneCoords != null) {
            if(WorldUtils.isInZone(getUhcPlayer().getPlayer(), gelZoneCoords, 30, 30)) {
                getUhcPlayer().setStrengthEffect(20);
            }
            else {
                getUhcPlayer().setStrengthEffect(0);
            }
        }
    }

    private class FauconCommand extends CommandPower {

        public FauconCommand(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Faucon";
        }

        @Override
        public String getDescription() {
            return "A l'activation, vous obtenez le nombre de pommes en or ainsi que le nombre de kills du joueur ciblé";
        }

        @Override
        public String getArgument() {
            return "faucon";
        }

        @Override
        public boolean onEnable(Player player, String[] args) {
            if(args.length < 2) {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Usage: /ni faucon <joueur>"));
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

            int gapples = 0;
            for(ItemStack item : target.getInventory().getContents()) {
                if(item != null) {
                    if(item.getType() == Material.GOLDEN_APPLE) {
                        gapples += item.getAmount();
                    }
                }
            }

            player.sendMessage(ChatPrefixes.INFO.getMessage(
                    "Le joueur " + target.getName() + " a " + gapples + " pommes d'or et a tué " + uhcTarget.kills + " joueurs."
            ));

            return true;
        }
    }

    private class FreezePower extends RightClickItemPower {

        FreezeZoneCooldown freezeZoneRunnable;
        List<Block> icedBlocks;

        public FreezePower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.BLUE + "Déluge§r";
        }

        @Override
        public String getDescription() {
            return "À l'activation, vous recouvrez de glace une zone de 30x30 où vous possédez Force 1";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.PACKED_ICE, ChatColor.BLUE + "Déluge");
        }

        @Override
        public boolean onEnable(PlayerInteractEvent event) {
            if(gelZonePlaced) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Vous avez déjà placé votre zone !"));
                return false;
            }

            if(freezeZoneRunnable == null) {
                freezeZoneRunnable = new FreezeZoneCooldown();
            }
            else {
                freezeZoneRunnable.reset();
            }

            gelZoneCoords = getUhcPlayer().getPlayer().getLocation();

            icedBlocks = new ArrayList<>();
            for(int x = -14; x <= 14; x++) {
                for(int z = -14; z <= 14; z++) {
                    icedBlocks.add(gelZoneCoords.getWorld().getHighestBlockAt((int) (gelZoneCoords.getX() + x), (int) (gelZoneCoords.getZ() + z)).getLocation().add(0, -1, 0).getBlock());
                    gelZoneCoords.getWorld().getHighestBlockAt((int) (gelZoneCoords.getX() + x), (int) (gelZoneCoords.getZ() + z)).getLocation().add(0, -1, 0).getBlock().setType(Material.PACKED_ICE);
                }
            }
            gelZonePlaced = true;

            return true;
        }

        private class FreezeZoneCooldown extends BukkitRunnable {
            int timer;
            boolean isRunning;

            public FreezeZoneCooldown() {
                timer = 0;
                isRunning = true;
                this.runTaskTimer(Main.getInstance(), 0, 20);
            }

            @Override
            public void run() {
                if(isRunning) {
                    if(timer > zoneTimer) {
                        for(Block block : icedBlocks) {
                            if(
                                    block.getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR ||
                                    block.getLocation().add(0, -1, 0).getBlock().getType() == Material.LEAVES ||
                                    block.getLocation().add(0, -1, 0).getBlock().getType() == Material.LEAVES_2 ||
                                    block.getLocation().add(0, -1, 0).getBlock().getType() == Material.LOG ||
                                    block.getLocation().add(0, -1, 0).getBlock().getType() == Material.LOG_2
                            ) {
                                block.setType(Material.LEAVES);
                            }
                            else {
                                block.setType(Material.GRASS);
                            }
                        }
                        isRunning = false;
                        gelZonePlaced = false;
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

    private class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.WHITE + "Spinjitzu§r";
        }

        @Override
        public String getDescription() {
            return "À l'activation, repousse de 5 blocks tous les joueurs dans un rayon de 4 blocks";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.NETHER_STAR, ChatColor.WHITE + "Spinjitzu");
        }

        @Override
        public boolean onEnable(PlayerInteractEvent event) {
            List<Entity> nearbyEntities = event.getPlayer().getNearbyEntities(4, 2, 4);
            Location center = event.getPlayer().getLocation();
            for(Entity entity : nearbyEntities) {
                double angle = Math.atan2(entity.getLocation().getZ() - center.getZ(), entity.getLocation().getX() - center.getX());
                Vector newVelocity = new Vector(
                        1.5 * Math.cos(angle),
                        0.5, //* Math.signum(entity.getLocation().getY() - center.getY()),
                        1.5 * Math.sin(angle)
                );
                entity.setVelocity(newVelocity);
            }

            ParticlesEffects.createSpinjitzuEffect(event.getPlayer(), Color.fromRGB(214, 214, 214));

            return true;
        }
    }
}
