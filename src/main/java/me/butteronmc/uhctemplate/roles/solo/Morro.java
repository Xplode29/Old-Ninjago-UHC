package me.butteronmc.uhctemplate.roles.solo;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Morro extends AbstractRole {

    boolean hasZanePower = false;
    boolean freezeActivated = false;

    boolean visible = true;

    int timeToGetNinja = UHCHandler.dayTime;

    Map<RoleEnum, Integer> ninjaTimers = new HashMap<>();

    public Morro() {
        addPowers(
                new ZaneCommand(0, -1),
                new GhostItem(5 * 60, -1)
        );
    }

    @Override
    public String getName() {
        return "Morro";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous connaissez l'identité de Wu. ",
                "Lorque vous restez 10 minutes à coté d'un ninja, vous êtes informé de son pseudo. ",
                "Vous obtenez des effets selon les ninjas tués:",
                ChatPrefixes.LIST_ELEMENT.getMessage("§9Jay§r: Vous obtenez speed 2"),
                ChatPrefixes.LIST_ELEMENT.getMessage("§cKai§r: Vous obtenez fire aspect sur votre épée et flame sur votre arc"),
                ChatPrefixes.LIST_ELEMENT.getMessage("§8Cole§r: Vous obtenez résistance 1"),
                ChatPrefixes.LIST_ELEMENT.getMessage("§3Zane§r: Vous obtenez un passif (/ni freeze).")
        };
    }

    @Override
    public void onGiveRole() {
        getUhcPlayer().setStrengthEffect(20);
        getUhcPlayer().getPlayer().setMaxHealth(26);
    }

    @Override
    public List<String> additionalDescription() {
        for(RoleEnum roleEnum : UHCHandler.roleList) {
            if(
                    roleEnum == RoleEnum.JAY ||
                    roleEnum == RoleEnum.COLE ||
                    roleEnum == RoleEnum.KAI ||
                    roleEnum == RoleEnum.ZANE
            ) {
                if(roleEnum.getAbstractRole().getUhcPlayer() != null) {
                    ninjaTimers.put(roleEnum, 0);
                }
            }
        }
        return super.additionalDescription();
    }

    @Override
    public void updateRole(int timer) {
        for(RoleEnum ninja : ninjaTimers.keySet()) {
            if(ninja.getAbstractRole().getUhcPlayer().getPlayer().getLocation().distance(getUhcPlayer().getPlayer().getLocation()) < 15) {
                ninjaTimers.replace(ninja, ninjaTimers.get(ninja) + 1);
            }
            if(ninjaTimers.get(ninja) > timeToGetNinja) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage(
                        ninja.getAbstractRole().getUhcPlayer().getPlayer().getName() + " est un ninja."
                ));
            }
        }
    }

    @Override
    public void onHitPlayer(UHCPlayer target) {
        if(freezeActivated) {
            if((new Random()).nextInt(100) < 20) {
                target.setSpeedEffect(-20);
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Vous avez ralenti " + target.getPlayer().getName()));
            }
        }
        if(!visible) {
            getPowers().get(1).onPowerUsed(getUhcPlayer().getPlayer());
        }
    }

    @Override
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(event.getVictim().getRoleEnum() == null) return;

        if(event.getVictim().getRoleEnum() == RoleEnum.JAY) {
            getUhcPlayer().setSpeedEffect(40);
        }
        if(event.getVictim().getRoleEnum() == RoleEnum.KAI) {
            ItemStack bow = new ItemStack(Material.BOW);
            ItemMeta bowMeta = bow.getItemMeta();
            bowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 3, true);
            bowMeta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
            bow.setItemMeta(bowMeta);

            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            ((EnchantmentStorageMeta) bookMeta).addStoredEnchant(Enchantment.FIRE_ASPECT, 1, true);
            book.setItemMeta(bookMeta);

            getUhcPlayer().getPlayer().getInventory().addItem(bow, book);
        }
        if(event.getVictim().getRoleEnum() == RoleEnum.COLE) {
            getUhcPlayer().setResiEffect(20);
        }
        if(event.getVictim().getRoleEnum() == RoleEnum.ZANE) {
            hasZanePower = true;
        }
    }

    private class ZaneCommand extends CommandPower {

        public ZaneCommand(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.BLUE + "Freeze§r";
        }

        @Override
        public String getDescription() {
            return "Active / désactive votre passif (uniquement après avoir tué Zane). " +
                    "Lorqu'il est activé, vous avez 5% de chance d'infliger slowness 1 lorsque vous tapez un joueur";
        }

        @Override
        public String getArgument() {
            return "freeze";
        }

        @Override
        public boolean onEnable(Player player, String[] args) {
            if(hasZanePower) {
                freezeActivated = !freezeActivated;
                if(freezeActivated) {
                    getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Passif activé"));
                }
                else {
                    getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Passif désactivé"));
                }
            }
            else {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Vous n'avez pas tué Zane !"));
            }
            return false;
        }
    }

    private class GhostItem extends ItemPower {

        public GhostItem(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.GREEN + "Fantome§r";
        }

        @Override
        public String getDescription() {
            return "Active / désactive votre passif. Lorqu'il est activé, vous êtes invisible, même avec votre armure";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.NETHER_STAR, ChatColor.GREEN + "Fantome");
        }

        @Override
        public boolean onEnable(PlayerInteractEvent event) {
            visible = !visible;
            if(visible) {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Vous êtes maintenant visible"));
                for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers){
                    uhcPlayer.getPlayer().showPlayer(getUhcPlayer().getPlayer());
                }
                return true;
            }
            else {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Vous êtes maintenant invisible"));
                for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers) {
                    uhcPlayer.getPlayer().hidePlayer(getUhcPlayer().getPlayer());
                }
                return false;
            }
        }
    }
}
