package me.butteronmc.uhctemplate.roles.ninja;

import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class Mitsake extends AbstractRole {

    public Mitsake() {
        addPowers(
            new HealthPotions(0, -1),
            new PoisonPotion(0, -1),
            new SpeedPotions(0, -1)
        );
    }

    @Override
    public String getName() {
        return "Mitsake";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez faiblesse la nuit."
        };
    }

    @Override
    public void onDay() {
        getUhcPlayer().setStrengthEffect(0);
        super.onDay();
    }

    @Override
    public void onNight() {
        getUhcPlayer().setStrengthEffect(-20);
        super.onNight();
    }

    private class HealthPotions extends ItemPower {

        public HealthPotions(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Potions d'instant health (*3)";
        }

        @Override
        public ItemStack getItemStack() {
            Potion pot = new Potion(PotionType.INSTANT_HEAL, 2, true);
            return pot.toItemStack(3);
        }

        @Override
        public boolean isVanilla() {
            return true;
        }

        @Override
        public boolean cancelEvent() {
            return false;
        }
    }

    private class PoisonPotion extends ItemPower {

        public PoisonPotion(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Potion de poison (45s)";
        }

        @Override
        public ItemStack getItemStack() {
            Potion pot = new Potion(PotionType.POISON, 1, true);
            return pot.toItemStack(1);
        }

        @Override
        public boolean isVanilla() {
            return true;
        }

        @Override
        public boolean cancelEvent() {
            return false;
        }
    }

    private class SpeedPotions extends ItemPower {

        public SpeedPotions(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public String getName() {
            return "Potions de speed (*2)";
        }

        @Override
        public ItemStack getItemStack() {
            Potion pot = new Potion(PotionType.SPEED, 0, true);
            return pot.toItemStack(1);
        }

        @Override
        public boolean isVanilla() {
            return true;
        }

        @Override
        public boolean cancelEvent() {
            return false;
        }
    }
}
