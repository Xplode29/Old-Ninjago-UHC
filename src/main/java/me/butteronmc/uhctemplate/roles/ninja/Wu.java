package me.butteronmc.uhctemplate.roles.ninja;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wu extends AbstractRole {

    boolean hadNinja = false;

    public Wu() {
        addPowers(
                new StickPower(0, -1)
        );
    }

    @Override
    public void onPlayerDie(UHCPlayerDeathEvent event) {
        UHCPlayer killed = event.getVictim();

        if(
            killed.getRoleEnum() == RoleEnum.LLOYD ||
            killed.getRoleEnum() == RoleEnum.KAI ||
            killed.getRoleEnum() == RoleEnum.JAY ||
            killed.getRoleEnum() == RoleEnum.ZANE ||
            killed.getRoleEnum() == RoleEnum.COLE
        ){
            getUhcPlayer().setSpeedEffect(getUhcPlayer().getSpeedEffect() + 7);
            if(!hadNinja) {
                List<String> pseudos = getAliveNinjas();

                if(pseudos.isEmpty()) {
                    getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage(
                            "Il n'y a pas de ninja en vie"
                    ));
                    return;
                }
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage(
                        pseudos.get((new Random()).nextInt(pseudos.size())) + " est un ninja"
                ));
                hadNinja = true;
            }
        }
    }

    private List<String> getAliveNinjas() {
        List<String> pseudos = new ArrayList<>();
        for(UHCPlayer uhcPlayer : UHCHandler.alivePlayers) {
            if(
                uhcPlayer.getRoleEnum() == RoleEnum.LLOYD ||
                uhcPlayer.getRoleEnum() == RoleEnum.KAI ||
                uhcPlayer.getRoleEnum() == RoleEnum.JAY ||
                uhcPlayer.getRoleEnum() == RoleEnum.ZANE ||
                uhcPlayer.getRoleEnum() == RoleEnum.COLE
            ) {
                pseudos.add(uhcPlayer.getPlayer().getName());
            }
        }
        return pseudos;
    }

    @Override
    public String getName() {
        return "Wu";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Lorsqu'un ninja meurt, vous obtenez 7% de speed",
                "À la mort du premier ninja, vous obtenez le pseudo de l'un des 5 autres ninjas ( Zane, Lloyd, Cole, Kai, Jay )"
        };
    }

    private class StickPower extends ItemPower {

        public StickPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return ChatColor.YELLOW + "Bâton§r";
        }

        @Override
        public String getDescription() {
            return "Une épée enchantée sharpness 4";
        }

        @Override
        public boolean cancelEvent() {
            return false;
        }

        @Override
        public boolean isVanilla() {
            return true;
        }

        @Override
        public ItemStack getItemStack() {
            ItemStack stickSword = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta itemMeta = stickSword.getItemMeta();

            itemMeta.spigot().setUnbreakable(true);
            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
            itemMeta.setDisplayName(ChatColor.YELLOW + "Bâton");
            stickSword.setItemMeta(itemMeta);
            return stickSword;
        }
    }
}
