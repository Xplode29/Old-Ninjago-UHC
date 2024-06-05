package me.butteronmc.uhctemplate.utils;

public class RandomHelper {
    public static int getRandomInt(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static float getRandomFloat(float min, float max) {
        return min + (float)(Math.random() * ((max - min) + 1));
    }

    public static double getRandomDouble(double min, double max) {
        return min + (Math.random() * ((max - min) + 1));
    }
}
