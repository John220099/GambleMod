package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.module.Module;

public class AutoSprint extends Module {
    public AutoSprint() {
        super("AutoSprint", "Me to lazy to press sprint.");
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.isWindowActive()) {
            mc.options.keySprint.setDown(true);
        }
    }

    @Override
    protected void onDisable() {
        if (mc.player != null) {
            mc.options.keySprint.setDown(false);
        }
    }
}