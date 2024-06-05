package me.butteronmc.uhctemplate.roles.ninja;

import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.api.powers.ItemPower;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Dareth extends AbstractRole {

    public Dareth() {
        addPowers(
            new BowPunch(0, -1)
        );
    }

    @Override
    public String getName() {
        return "Dareth";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez faiblesse le jour."
        };
    }

    @Override
    public void onDay() {
        getUhcPlayer().setStrengthEffect(-20);
        super.onDay();
    }

    @Override
    public void onNight() {
        getUhcPlayer().setStrengthEffect(0);
        super.onNight();
    }

    private class BowPunch extends ItemPower {

        public BowPunch(int cooldown, int maxUses) {
            super(cooldown, maxUses);
        }

        @Override
        public ItemStack getItemStack() {
            ItemStack book = new ItemStack(Material.BOW);
            ItemMeta itemMeta = book.getItemMeta();
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 3, true);
            itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
            book.setItemMeta(itemMeta);
            return book;
        }

        @Override
        public String getName() {
            return "§cArc de Dareth§r";
        }

        @Override
        public String getDescription() {
            return "Un arc enchanté power 3 et punch";
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
}
