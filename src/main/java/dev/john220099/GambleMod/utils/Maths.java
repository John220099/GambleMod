package dev.john220099.GambleMod.utils;

public class Maths {
    public static float randomFloat(float min, float max) {
        return min + (float) java.lang.Math.random() * (max - min);
    }
}