package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.module.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;

public class Jesus extends Module {

    public Jesus() {
        super("Jesus", "Walk on h2O.");
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.level != null) {
            mc.options.keyJump.setDown(playerInWater());
        }
    }

    private boolean playerInWater() {
        assert mc.player != null;
        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();

        BlockPos p = new BlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));

        assert mc.level != null;
        return mc.level.getBlockState(p).getBlock() == Blocks.WATER;
    }

    @Override
    protected void onDisable() {
        if (mc.player != null) {
            mc.options.keyShift.setDown(false);
        }
    }
}