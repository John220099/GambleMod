package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.module.Module;

public class HitBox extends Module {

    private float currentExpansion = 0.0f;

    public HitBox() {
        super("HitBox", "XXL is still to small. (Ban SpeedRun Grim AC)");
    }

    public float getExpansion() {
        return currentExpansion;
    }

    @Override
    public void onTick() {
        currentExpansion = (float) (Math.random() * 0.15f);
    }
}