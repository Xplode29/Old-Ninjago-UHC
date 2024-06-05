package me.butteronmc.uhctemplate.roles.ninja;

import me.butteronmc.uhctemplate.Main;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.roles.api.powers.RightClickItemPower;
import me.butteronmc.uhctemplate.utils.GraphicUtils;
import me.butteronmc.uhctemplate.utils.ParticlesEffects;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

public class Lloyd extends AbstractRole {

    boolean explosionNextHit = false;

    public Lloyd() {
        addPowers(
                new GoldenSwordPower(5 * 60, -1),
                new ProtectionBook(0, -1),
                new SpinjitzuPower(UHCHandler.dayTime, -1)
        );
    }

    @Override
    public String getName() {
        return "Lloyd";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Force 1 (20%) ainsi que 13 coeurs permanents"
        };
    }

    @Override
    public void onGiveRole() {
        getUhcPlayer().setStrengthEffect(20);
        getUhcPlayer().getPlayer().setMaxHealth(26);
    }

    @Override
    public void onHitPlayer(UHCPlayer target) {
        Player targetPlayer = target.getPlayer();
        if(explosionNextHit) {
            if(
                    getUhcPlayer().getPlayer().getInventory().getItemInHand().getType() == Material.DIAMOND_SWORD &&
                    getUhcPlayer().getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName().equals("§6Golden Sword")
            ) {
                targetPlayer.getWorld().playSound(targetPlayer.getLocation(), Sound.EXPLODE, 3.0f, 1.0f);
                targetPlayer.getWorld().playEffect(targetPlayer.getLocation().add(0, 1, 0), Effect.EXPLOSION_LARGE, 1);

                targetPlayer.setHealth(Math.max(0, targetPlayer.getHealth() - 2.0));
                explosionNextHit = false;
            }
        }
    }

    private class GoldenSwordPower extends RightClickItemPower {

        LoadRunnable loadRunnable = null;

        public GoldenSwordPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "§6Golden Sword§r";
        }

        @Override
        public String getDescription() {
            return "En restant appuyé sur votre clic droit, vous chargez une barre d'énergie. " +
                    "Lorsqu'elle est pleine, le prochain coup créera une explosion infligeant 2 coeurs a la cible";
        }

        public boolean cancelEvent() {
            return false;
        }

        @Override
        public ItemStack getItemStack() {
            ItemStack goldenSword = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta itemMeta = goldenSword.getItemMeta();

            itemMeta.spigot().setUnbreakable(true);
            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
            itemMeta.setDisplayName("§6Golden Sword");
            goldenSword.setItemMeta(itemMeta);
            return goldenSword;
        }

        @Override
        public boolean onEnable(PlayerInteractEvent event) {
            if(loadRunnable == null) {
                loadRunnable = new LoadRunnable(getUhcPlayer().getPlayer());
            }
            else {
                loadRunnable.reset();
            }
            return false;
        }

        private class LoadRunnable extends BukkitRunnable {
            UUID playerUUID;
            int timer;
            boolean isRunning;

            public LoadRunnable(Player player) {
                this.playerUUID = player.getUniqueId();
                this.timer = 0;
                this.isRunning = true;
                this.runTaskTimer(Main.getInstance(), 0, 1);
            }

            public void reset() {
                this.timer = 0;
                this.isRunning = true;
            }

            @Override
            public void run() {
                if(isRunning) {
                    Player player = Bukkit.getPlayer(playerUUID);
                    if(player == null) {
                        isRunning = false;
                        return;
                    }
                    if(timer < 3 * 20) {
                        if(!player.isBlocking()) {
                            player.sendMessage(ChatPrefixes.ERROR.getMessage("Vous avez relaché trop tot !"));
                            isRunning = false;
                            return;
                        }
                        timer++;
                        float progress = (timer / (20f * 3));
                        player.playSound(player.getLocation(), Sound.NOTE_STICKS, 3.0F, (float)Math.pow(2.0, ((double)(progress) * 12 - 12.0) / 12.0));
                        GraphicUtils.sendActionText(player,  GraphicUtils.getProgressBar((int) (progress * 100), 100, 20, '|', ChatColor.GREEN, ChatColor.GRAY));
                    }
                    else {
                        forceCooldown();
                        explosionNextHit = true;
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, (float)Math.pow(2.0, ((double)12 - 12.0) / 12.0));
                        player.sendMessage(ChatPrefixes.INFO.getMessage("Le prochain coup fera exploser le joueur frappé"));
                        isRunning = false;
                    }
                }
            }
        }
    }

    private class ProtectionBook extends ItemPower {

        public ProtectionBook(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public ItemStack getItemStack() {
            ItemStack protectionBook = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta itemMeta = protectionBook.getItemMeta();
            ((EnchantmentStorageMeta) itemMeta).addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
            protectionBook.setItemMeta(itemMeta);
            return protectionBook;
        }

        @Override
        public String getName() {
            return "§5Livre Enchanté§r";
        }

        @Override
        public String getDescription() {
            return "Un livre enchanté protection 3. Il est possible de le fusionner avec une pièce en diamant.";
        }

        @Override
        public boolean cancelEvent() {
            return false;
        }

        @Override
        public boolean isVanilla() {
            return true;
        }
    }

    private class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.GREEN + "Spinjitzu§r";
        }

        @Override
        public String getDescription() {
            return "À l'activation, repousse de 5 blocks tous les joueurs dans un rayon de 4 blocks";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.NETHER_STAR, ChatColor.GREEN + "Spinjitzu");
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

            ParticlesEffects.createSpinjitzuEffect(event.getPlayer(), Color.fromRGB(120, 214, 28));

            return true;
        }
    }
}
