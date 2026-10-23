package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.module.Module;

public class Disabler extends Module {

    public Disabler() {
        super("Disabler", "Best bypass trust.");
    }

    private float yaw;
    int speed = 10;

    @Override
    public void onTick() {
        yaw += speed;
        assert mc.player != null;
        mc.player.setYRot(yaw);
    }
}
