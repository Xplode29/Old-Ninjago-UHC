package me.butteronmc.uhctemplate.roles.ninja;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.utils.GraphicUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Nya extends AbstractRole {

    boolean samuraiActive = false;
    int samuraiTimer = 0;
    int maxTimer = 2 * 60;

    boolean bookGiven = false;
    int maxTimerNextKai = 10 * 60;
    int lastKaiTimer = 0;

    public Nya() {
        addPowers(
                new SamuraiPower(0, -1)
        );
    }

    @Override
    public String getName() {
        return "Nya";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous obtenez une liste de 3 pseudos contenant celui de Kai à 50 minutes de jeu",
                "En restant 10 minutes à 10 blocs de celui-ci, vous obtiendrez un livre depth rider 3"
        };
    }

    @Override
    public RoleEnum getDuo() {
        return RoleEnum.KAI;
    }

    @Override
    public void updateRole(int timer) {
        if(samuraiActive) {
            samuraiTimer++;
            if(samuraiTimer > maxTimer) {
                samuraiActive = false;
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Vous n'avez plus assez de temps, votre samurai X s'est désactivé"));
            }
        }
        if(timer == 50 * 60) {
            if(UHCHandler.roleList.contains(RoleEnum.KAI)) {
                List<UHCPlayer> alivePlayers = new ArrayList<>(UHCHandler.alivePlayers);
                alivePlayers.remove(RoleEnum.KAI.getAbstractRole().getUhcPlayer());

                Collections.shuffle(alivePlayers);
                List<UHCPlayer> playersList = alivePlayers.subList(0, 2);
                playersList.add(RoleEnum.KAI.getAbstractRole().getUhcPlayer());
                Collections.shuffle(playersList);

                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage(
                        "Le pseudo du joueur Kai est parmis les suivants :"
                ));
                for(UHCPlayer uhcPlayer : playersList) {
                    getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.LIST_ELEMENT.getColoredMessage("§e",
                            uhcPlayer.getPlayer().getName()
                    ));
                }
            }
        }

        if(!bookGiven) {
            if(RoleEnum.KAI.getAbstractRole().getUhcPlayer() != null) {
                boolean isNextKai = (RoleEnum.KAI.getAbstractRole().getUhcPlayer().getPlayer().getLocation().distance(getUhcPlayer().getPlayer().getLocation()) < 15);

                if (!isNextKai) {
                    lastKaiTimer = timer;
                } else if (timer >= lastKaiTimer + maxTimerNextKai) {
                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta itemMeta = book.getItemMeta();
                    itemMeta.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
                    book.setItemMeta(itemMeta);
                    getUhcPlayer().getPlayer().getInventory().addItem(book);
                    bookGiven = true;
                    getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Vous avez recu votre livre depth rider !"));
                }
            }
        }
    }

    private class SamuraiPower extends ItemPower {

        public SamuraiPower(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "§3Samurai X§r";
        }

        @Override
        public String getDescription() {
            return "Lorsque cet item est activé, vous obtenez 20% de speed et 20% de force. " +
                    "Vous avez 2 minutes d'utilisation. " +
                    "Lorsque vous faites un kill, vous obtenez 30 secondes d'utilisation supplémentaires" +
                    "Vous pouvez vérifier le temps qu'il vous reste avec un clic gauche";
        }

        @Override
        public ItemStack getItemStack() {
            return format(Material.NETHER_STAR, "§3Samurai X");
        }

        @Override
        public boolean onEnable(PlayerInteractEvent event) {
            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(samuraiTimer > maxTimer) {
                    getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Vous n'avez plus assez de temps !"));
                }
                else {
                    samuraiActive = !samuraiActive;
                    if(samuraiActive) {
                        getUhcPlayer().setStrengthEffect(20);
                        getUhcPlayer().setSpeedEffect(20);
                        getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Samurai X activé"));
                    }
                    else {
                        getUhcPlayer().setStrengthEffect(0);
                        getUhcPlayer().setSpeedEffect(0);
                        getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage("Samurai X désactivé"));
                    }
                }
            }
            else {
                if(samuraiTimer > maxTimer) {
                    getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Vous n'avez plus assez de temps !"));
                }
                else {
                    getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Il vous reste " + GraphicUtils.convertToAccurateTime(maxTimer - samuraiTimer) + " d'utilisation"));
                }
            }
            return false;
        }
    }
}