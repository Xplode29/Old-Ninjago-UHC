package me.butteronmc.uhctemplate.utils.skulls;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkullsManager {
    public static List<ItemStack> skulls;

    public static ItemStack skullMinus = SkullsGenerator.createCustomSkull("Less", Collections.singletonList("Remove one item"), "https://textures.minecraft.net/texture/482c23992a02725d9ed1bcd90fd0307c8262d87e80ce6fac8078387de18d0851");
    public static ItemStack skullPlus = SkullsGenerator.createCustomSkull("More", Collections.singletonList("Add one item"), "https://textures.minecraft.net/texture/5d8604b9e195367f85a23d03d9dd503638fcfb05b0032535bc43734422483bde");

    public static void init() {
        skulls = new ArrayList<>();

        skulls.add(skullMinus);
        skulls.add(skullPlus);
    }
}
