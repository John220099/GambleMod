package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.module.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

import static dev.john220099.GambleMod.utils.Maths.randomFloat;

public class BridgeAssist extends Module {
    public BridgeAssist() {
        super("BridgeAssist", "Me to lazy to shift.");
    }

    private int sneakDelay = 0;

    @Override
    public void onTick() {
        assert mc.player != null;
        boolean overAir = mc.player.onGround() && playerOverAir();

        if (overAir && sneakDelay <= 0) {
            mc.options.keyShift.setDown(true);
            sneakDelay = (int) randomFloat(1f, 4f);
        } else if (!overAir && mc.player.isShiftKeyDown()) {
            sneakDelay--;
            if (sneakDelay <= 0) {
                mc.options.keyShift.setDown(false);
            }
        } else if (overAir) {
            sneakDelay--;
        }
    }

    private boolean playerOverAir() {
        assert mc.player != null;
        double x = mc.player.getX();
        double y = mc.player.getY() - 1.0D;
        double z = mc.player.getZ();

        BlockPos p = new BlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));

        assert mc.level != null;
        return mc.level.getBlockState(p).isAir();
    }

    @Override
    protected void onDisable() {
        if (mc.player != null) {
            mc.options.keyShift.setDown(false);
        }
    }
}