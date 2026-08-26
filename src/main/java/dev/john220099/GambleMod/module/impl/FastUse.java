package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.mixin.accessors.MinecraftAccessor;
import dev.john220099.GambleMod.module.Module;

import static dev.john220099.GambleMod.utils.Maths.randomFloat;

public class FastUse extends Module {
    public FastUse() {
        super("FastUse", "Use stuff faster.");
    }

    @Override
    public void onTick() {
        if (mc.player == null || !mc.options.keyUse.isDown()) return;

        int delay = (int) randomFloat(0.3f, 4f);

        if (delay < 3f) {
            ((MinecraftAccessor) mc).setRightClickDelay(delay);
        }
    }
}