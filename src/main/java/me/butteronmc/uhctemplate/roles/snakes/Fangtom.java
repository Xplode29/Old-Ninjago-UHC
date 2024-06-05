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

public class Fangtom extends AbstractRole {

    public Fangtom() {
        addPowers(
                new ChatCommand(0, -1)
        );
    }

    @Override
    public String getName() {
        return "Fangtom";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez résistance à moins de 15 blocks de Fangdam. ",
                "Vous conaissez l'identité de Fangdam et de Pythor."
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
        if(UHCHandler.roleList.contains(RoleEnum.FANGDAM) && RoleEnum.FANGDAM.getAbstractRole().getUhcPlayer() != null) {
            desc.add(ChatPrefixes.INFO.getMessage("Fangdam: " + RoleEnum.FANGDAM.getAbstractRole().getUhcPlayer().getPlayer().getName()));
        }
        else {
            desc.add(ChatPrefixes.ERROR.getMessage("Il n'y a pas de Fangdam dans cette partie !"));
        }
        return desc;
    }

    @Override
    public boolean inList() {
        return true;
    }

    @Override
    public RoleEnum getDuo() {
        return RoleEnum.FANGDAM;
    }

    @Override
    public void updateRole(int timer) {
        if(UHCHandler.roleList.contains(RoleEnum.FANGDAM) && RoleEnum.FANGDAM.getAbstractRole().getUhcPlayer() != null) {
            if(RoleEnum.FANGDAM.getAbstractRole().getUhcPlayer().getPlayer().getLocation().distance(getUhcPlayer().getPlayer().getLocation()) < 15) {
                getUhcPlayer().setResiEffect(20);
            }
            else {
                getUhcPlayer().setResiEffect(0);
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
            return "Vous permet de communiquer avec Fangdam";
        }

        @Override
        public String getArgument() {
            return "chat";
        }

        @Override
        public boolean onEnable(Player player, String[] args) {
            List<String> message = Lists.newArrayList(args);
            message.remove("chat");
            if(UHCHandler.roleList.contains(RoleEnum.FANGDAM) && RoleEnum.FANGDAM.getAbstractRole().getUhcPlayer() != null) {
                RoleEnum.FANGDAM.getAbstractRole().getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.INFO.getMessage(
                        "Fangtom: " + Joiner.on(" ").join(message)
                ));
            }
            else {
                getUhcPlayer().getPlayer().sendMessage(ChatPrefixes.ERROR.getMessage("Il n'y a pas de Fangdam dans cette partie !"));
            }
            return false;
        }
    }
}
