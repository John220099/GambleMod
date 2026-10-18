package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.module.Module;

import java.util.Objects;

public class CreativeFly extends Module {

    public CreativeFly() {
        super("CreativeFly", "Its a bird! Its a plane! Its a hacker.");
    }

    float flySpeed = 1.4f;
    private float originalFlySpeed;
    private boolean originalMayfly;

    @Override
    protected void onEnable() {
        if (mc.player != null && mc.level != null && !(Objects.requireNonNull(mc.player.gameMode()).isCreative())) {
            originalFlySpeed = mc.player.getAbilities().getFlyingSpeed();
            originalMayfly = mc.player.getAbilities().mayfly;
            mc.player.getAbilities().mayfly = true;
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().setFlyingSpeed(flySpeed);
            mc.player.onUpdateAbilities();
        }
    }

    @Override
    protected void onDisable() {
        if (mc.player != null && mc.level != null && !(Objects.requireNonNull(mc.player.gameMode()).isCreative())) {
            mc.player.getAbilities().mayfly = originalMayfly;
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().setFlyingSpeed(originalFlySpeed);
            mc.player.onUpdateAbilities();
        }
    }

    @Override
    public void onTick() {
        if (!isEnabled() || mc.player == null || mc.level == null || Objects.requireNonNull(mc.player.gameMode()).isCreative()) return;
        float currentSpeed = mc.player.getAbilities().getFlyingSpeed();
        float desiredSpeed = flySpeed;
        if (Math.abs(currentSpeed - desiredSpeed) > 0.01f) {
            mc.player.getAbilities().setFlyingSpeed(desiredSpeed);
        }

        if (!mc.player.getAbilities().flying) {
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().mayfly = true;
            mc.player.onUpdateAbilities();
        }
    }
}
