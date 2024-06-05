package me.butteronmc.uhctemplate.player;

import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class UHCPlayer {
    private UUID playerUUID;
    private Player player;
    private RoleEnum roleEnum;

    private float speedEffect = 0f;
    private float strengthEffect = 0f;
    private float resiEffect = 0f;

    public int kills = 0;
    public boolean hasNoFall = false;
    public boolean hideKills = false;

    public UHCPlayer(Player player) {
        this.playerUUID = player.getUniqueId();
        this.player = getPlayer();
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public RoleEnum getRoleEnum() {
        return roleEnum;
    }

    public void setRoleEnum(RoleEnum newRole) {
        this.roleEnum = newRole;
    }

    public Player getPlayer() {
        if(Bukkit.getPlayer(playerUUID) == null) {
            return player;
        }
        else {
            return Bukkit.getPlayer(playerUUID);
        }
    }

    public float getResiEffect() {
        return resiEffect;
    }

    public void setResiEffect(float resiEffect) {
        this.resiEffect = resiEffect;
    }

    public void addResiEffect(float amount) {
        this.resiEffect += amount;
    }

    public float getStrengthEffect() {
        return strengthEffect;
    }

    public void setStrengthEffect(float strengthEffect) {
        this.strengthEffect = strengthEffect;
    }

    public void addStrenghtEffect(float amount) {
        this.strengthEffect += amount;
    }

    public float getSpeedEffect() {
        return speedEffect;
    }

    public void setSpeedEffect(float speedEffect) {
        this.speedEffect = speedEffect;
        getPlayer().setWalkSpeed(0.2f * (1 + speedEffect / 100));
    }

    public void addSpeedEffect(float amount) {
        this.speedEffect += amount;
        getPlayer().setWalkSpeed(0.2f * (1 + speedEffect / 100));
    }

    public void updatePlayer(int timer) {
        if(getPlayer() == null) return;
        if(getStrengthEffect() > 0) {
            getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 0));
        }

        if(getResiEffect() > 0) {
            getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2 * 20, 0));
        }

        if(getRoleEnum() != null) {
            getRoleEnum().getAbstractRole().updateRole(timer);
        }
    }

    public void onHitPlayer(UHCPlayer target) {
        if(getRoleEnum() == null) return;
        getRoleEnum().getAbstractRole().onHitPlayer(target);
    }

    public void onGetHitByPlayer(UHCPlayer target) {
        if(getRoleEnum() == null) return;
        getRoleEnum().getAbstractRole().onGetHitByPlayer(target);
    }

    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(getRoleEnum() == null) return;
        getRoleEnum().getAbstractRole().onKillPlayer(event);
        kills++;
    }

    public void onGetKilled(UHCPlayerDeathEvent event) {
        if(getRoleEnum() == null) return;
        getRoleEnum().getAbstractRole().onGetKilled(event);
    }

    public void onPlayerDie(UHCPlayerDeathEvent event) {
        if(getRoleEnum() == null) return;
        getRoleEnum().getAbstractRole().onPlayerDie(event);
    }
}
