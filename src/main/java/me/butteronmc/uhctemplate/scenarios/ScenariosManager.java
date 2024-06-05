package me.butteronmc.uhctemplate.scenarios;

import java.util.ArrayList;
import java.util.List;

public class ScenariosManager {
    public static List<Scenario> scenarios;
    public static HasteyBoys hasteyBoys;
    public static CutClean cutClean;
    public static FireLess fireLess;
    public static NoRod noRod;
    public static NoExplosion noExplosion;
    public static TimberPvp timberPvp;

    public static void init() {
        scenarios = new ArrayList<>();

        scenarios.add(hasteyBoys = new HasteyBoys());
        scenarios.add(cutClean = new CutClean());
        scenarios.add(fireLess = new FireLess());
        scenarios.add(noRod = new NoRod());
        scenarios.add(noExplosion = new NoExplosion());
        scenarios.add(timberPvp = new TimberPvp());
    }
}
