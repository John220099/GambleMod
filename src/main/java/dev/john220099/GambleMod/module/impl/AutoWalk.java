package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.module.Module;

public class AutoWalk extends Module {

    public AutoWalk() {
        super("AutoWalk", "Me to lazy to to press walk");
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.level != null) {
            mc.options.keyUp.setDown(true);
        }
    }
}

