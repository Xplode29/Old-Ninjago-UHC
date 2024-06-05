package me.butteronmc.uhctemplate.roles.api;

import me.butteronmc.uhctemplate.events.custom.UHCPlayerDeathEvent;
import me.butteronmc.uhctemplate.player.UHCPlayer;
import me.butteronmc.uhctemplate.roles.RoleEnum;
import me.butteronmc.uhctemplate.roles.api.powers.CommandPower;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import me.butteronmc.uhctemplate.roles.api.powers.Power;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractRole {

    private List<Power> powers = new ArrayList<>();
    private UHCPlayer uhcPlayer;

    private boolean infected = false;

    public void addPowers(Power... powers) {
        this.powers.addAll(Arrays.asList(powers));
    }

    public List<Power> getPowers() {
        return powers;
    }

    public String getName() {
        return "Role Template";
    }

    //At the start of the description (before the list of powers)
    public String[] getDescription() {
        return new String[0];
    }

    //At the end of the description (after the list of powers)
    public List<String> additionalDescription() {
        return new ArrayList<>();
    }

    public RoleEnum getDuo() {
        return null;
    }

    public boolean inList() {
        return false;
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    public void onGiveRole() {

    }

    public void onDay() {
        if(isInfected()) {
            getUhcPlayer().addStrenghtEffect(-20);
        }
    }

    public void onNight() {
        if(isInfected()) {
            getUhcPlayer().addStrenghtEffect(20);
        }
    }

    public boolean hasItems() {
        for(Power power : getPowers()) {
            if(power instanceof ItemPower) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCommands() {
        for(Power power : getPowers()) {
            if(power instanceof CommandPower) {
                if(!((CommandPower) power).isHidden()) {
                    return true;
                }
            }
        }
        return false;
    }

    public UHCPlayer getUhcPlayer() {
        return uhcPlayer;
    }

    public void setUhcPlayer(UHCPlayer uhcPlayer) {
        this.uhcPlayer = uhcPlayer;
    }

    public void onHitPlayer(UHCPlayer target) {

    }

    public void onGetHitByPlayer(UHCPlayer target) {

    }

    public void onKillPlayer(UHCPlayerDeathEvent event) {

    }

    public void onGetKilled(UHCPlayerDeathEvent event) {}

    public void onPlayerDie(UHCPlayerDeathEvent event) {

    }

    public void updateRole(int timer) {

    }
}
