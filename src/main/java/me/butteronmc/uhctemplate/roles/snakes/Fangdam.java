package me.butteronmc.uhctemplate.roles.snakes;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import me.butteronmc.uhctemplate.UHCHandler;
import me.butteronmc.uhctemplate.chat.ChatPrefixes;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Fangdam extends AbstractRole {

    public Fangdam() {
        addPowers(
                new ChatCommand(0, -1)
        );
    }

    @Override
    public String getName() {
        return "Fangdam";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez force à moins de 15 blocks de Fangtom. ",
                "Vous conaissez l'identité de Fangtom et de Pythor."
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
        if(UHCHandler.roleList.contains(RoleEnum.FANGTOM) && RoleEnum.FANGTOM.getAbstractRole().getUhcPlayer() != null) {
            desc.add(ChatPrefixes.INFO.getMessage("Fangtom: " + RoleEnum.FANGTOM.getAbstractRole().getUhcPlayer().getPlayer().getName()));
        }
        else {
            desc.add(ChatPrefixes.ERROR.getMessage("Il n'y a pas de Fangtom dans cette partie !"));
        }
        return desc;
    }

    @Override
    public boolean inList() {
        return true;
    }

    @Override
    public RoleEnum getDuo() {
        return RoleEnum.FANGTOM;
    }

    @Override
    public void updateRole(int timer) {
        if(UHCHandler.roleList.contains(RoleEnum.FANGTOM) && RoleEnum.FANGTOM.getAbstractRole().getUhcPlayer() != null) {
            if(RoleEnum.FANGTOM.getAbstractRole().getUhcPlayer().getPlayer().getLocation().distance(getUhcPlayer().getPlayer().getLocation()) < 15) {
                getUhcPlayer().setStrengthEffect(20);
            }
            else {
                getUhcPlayer().setStrengthEffect(0);
            }
        }
    }

    private class ChatCommand extends CommandPower {

        public ChatCommand(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Chat";
        }

        @Override
        public String getDescription() {
            return "Vous permet de communiquer avec Fangtom";
        }

        @Override
        public String getArgument() {
            return "chat";
        }

        @Override
        public boolean onEnable(Player player, String[] args) {
            List<String> message = Lists.newArrayList(args);
            message.remove("chat");
            if(UHCHandler.roleList.contains(RoleEnum.FANGTOM) && RoleEnum.FANGTOM.getAbstractRole().getUhcPlayer() != null) {
                RoleEnum.FANGTOM.getAbstractRole().getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage(
                        "Fangdam: " + Joiner.on(" ").join(message)
                ));
            }
            else {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Il n'y a pas de Fangtom dans cette partie !"));
            }
            return false;
        }
    }
}
