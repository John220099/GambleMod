package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.module.Module;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class FullBright extends Module {

    public FullBright() {
        super("FullBright", "GET BLINDED!");
    }

    @Override
    public void onTick() {
        assert mc.player != null;
        mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 255, false, false));
    }

    @Override
    protected void onDisable() {
        assert mc.player != null;
        mc.player.removeEffect(MobEffects.NIGHT_VISION);
    }
}