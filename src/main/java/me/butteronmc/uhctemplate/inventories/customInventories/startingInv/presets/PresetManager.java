package me.butteronmc.uhctemplate.inventories.customInventories.startingInv.presets;

import java.util.ArrayList;
import java.util.List;

public class PresetManager {
    public static List<Preset> presetList;
    public static MeetupPreset meetupPreset;
    public static UHCPreset uhcPreset;

    public static void init() {
        presetList = new ArrayList<>();

        presetList.add(meetupPreset = new MeetupPreset());
        presetList.add(uhcPreset = new UHCPreset());
    }
}
