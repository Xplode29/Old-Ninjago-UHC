package me.butteronmc.uhctemplate.roles;

import org.bukkit.Material;

public enum CampEnum {
    NINJA("Ninjas", "§a", Material.IRON_SWORD),
    SERPENT("Serpents", "§1", Material.SOUL_SAND),
    MAITRES("Maitres des éléments", "§3", Material.NETHER_STAR),
    SOLO("Solitaires", "§e", Material.DIAMOND);

    private final String name;
    private final String color;
    private final Material icon;

    CampEnum(String name, String color, Material icon) {
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Material getIcon() {
        return icon;
    }
}
