package me.butteronmc.uhctemplate.roles;

import me.butteronmc.uhctemplate.roles.api.AbstractRole;
import me.butteronmc.uhctemplate.roles.maitres.Fumee;
import me.butteronmc.uhctemplate.roles.maitres.Lumiere;
import me.butteronmc.uhctemplate.roles.maitres.Metal;
import me.butteronmc.uhctemplate.roles.maitres.Vitesse;
import me.butteronmc.uhctemplate.roles.ninja.*;
import me.butteronmc.uhctemplate.roles.snakes.*;
import me.butteronmc.uhctemplate.roles.solo.Garmadon;
import me.butteronmc.uhctemplate.roles.solo.Morro;
import me.butteronmc.uhctemplate.roles.solo.Skylor;
import org.bukkit.Material;

public enum RoleEnum {
    LLOYD(new Lloyd(), CampEnum.NINJA, Material.GOLD_SWORD),
    KAI(new Kai(), CampEnum.NINJA, Material.FIREBALL),
    NYA(new Nya(), CampEnum.NINJA, Material.WATER_BUCKET),
    ZANE(new Zane(), CampEnum.NINJA, Material.ICE),
    COLE(new Cole(), CampEnum.NINJA, Material.BEDROCK),
    WU(new Wu(), CampEnum.NINJA, Material.STICK),
    JAY(new Jay(), CampEnum.NINJA, Material.LAPIS_ORE),
    PIXAL(new PIXAL(), CampEnum.NINJA, Material.DIODE),
    MITSAKE(new Mitsake(), CampEnum.NINJA, Material.POTION),
    DARETH(new Dareth(), CampEnum.NINJA, Material.BOW),
    ED(new Ed(), CampEnum.NINJA, Material.CLAY),
    FACTEUR(new Facteur(), CampEnum.NINJA, Material.WOOD_DOOR),
    MISAKO(new Misako(), CampEnum.NINJA, Material.FEATHER),

    PYTHOR(new Pythor(), CampEnum.SERPENT, Material.WATER_LILY),
    SKALES(new Skales(), CampEnum.SERPENT, Material.BEACON),
    ACIDICUS(new Acidicus(), CampEnum.SERPENT, Material.POISONOUS_POTATO),
    SKALIDOR(new Skalidor(), CampEnum.SERPENT, Material.STONE),
    FANGTOM(new Fangtom(), CampEnum.SERPENT, Material.TORCH),
    FANGDAM(new Fangdam(), CampEnum.SERPENT, Material.REDSTONE_TORCH_ON),
    BYTAR(new Bytar(), CampEnum.SERPENT, Material.DIRT),
    LIZARU(new Lizaru(), CampEnum.SERPENT, Material.POTATO_ITEM),
    SLITHRAA(new Slithraa(), CampEnum.SERPENT, Material.STAINED_GLASS),

    VITESSE(new Vitesse(), CampEnum.MAITRES, Material.SUGAR),
    LUMIERE(new Lumiere(), CampEnum.MAITRES, Material.GLOWSTONE),
    METAL(new Metal(), CampEnum.MAITRES, Material.IRON_CHESTPLATE),
    FUMEE(new Fumee(), CampEnum.MAITRES, Material.FEATHER),

    MORRO(new Morro(), CampEnum.SOLO, Material.SLIME_BALL),
    GARMADON(new Garmadon(), CampEnum.SOLO, Material.OBSIDIAN),
    SKYLOR(new Skylor(), CampEnum.SOLO, Material.FIREWORK)
    ;

    private final AbstractRole role;
    private final CampEnum camp;
    private final Material icon;

    RoleEnum(AbstractRole role, CampEnum camp, Material icon) {
        this.role = role;
        this.camp = camp;
        this.icon = icon;
    }

    public AbstractRole getAbstractRole() {
        return role;
    }

    public CampEnum getCamp() {
        return camp;
    }

    public Material getIcon() {
        return icon;
    }
}
