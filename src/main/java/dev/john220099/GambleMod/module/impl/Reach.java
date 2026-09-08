package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.module.Module;

public class Reach extends Module {

    private float currentReach = 0.0f;

    public Reach() {
        super("Reach", "Long arms (Ban SpeedRun Grim AC & Karhu AC)");
    }

    public float getReach() {
        return currentReach;
    }

    @Override
    public void onTick() {
        currentReach = (float) (Math.random() * 0.15f);
    }
}