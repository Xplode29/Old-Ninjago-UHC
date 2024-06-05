package me.butteronmc.uhctemplate.items;

import me.butteronmc.uhctemplate.items.customItems.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public static List<CustomItem> customItems;
    public static MenuItem menuItem;

    public static void init() {
        customItems = new ArrayList<>();

        customItems.add(menuItem = new MenuItem());
    }
}
