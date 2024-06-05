package me.butteronmc.uhctemplate.roles.ninja;

import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.inventories.InventoriesManager;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Misako extends AbstractRole {

    public Misako() {
        addPowers(
                new InventoryCommand(UHCHandler.dayTime * 2, 3)
        );
    }

    @Override
    public String getName() {
        return "Misako";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez 10% de speed permanent. "
        };
    }

    @Override
    public void onGiveRole() {
        getUhcPlayer().setSpeedEffect(10);
    }

    private class InventoryCommand extends CommandPower {

        public InventoryCommand(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Inventaire";
        }

        @Override
        public String getDescription() {
            return "A l'execution, vous pouvez voir l'inventaire du joueur ciblé";
        }

        @Override
        public String getArgument() {
            return "inventory";
        }

        @Override
        public boolean onEnable(Player player, String[] args) {
            if(args.length < 2) {
                player.sendMessage(ChatPrefixes.ERROR.getMessage("Usage: /ni inventory <joueur>"));
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

            InventoriesManager.playeInvMenu.setPlayer(uhcTarget);
            InventoriesManager.playeInvMenu.openInv(getUhcPlayer().getPlayer());

            return true;
        }
    }
}
