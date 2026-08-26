package dev.john220099.GambleMod.module.impl;

import com.mojang.blaze3d.platform.InputConstants;
import dev.john220099.GambleMod.mixin.accessors.KeyMappingAccessor;
import dev.john220099.GambleMod.module.Module;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import static dev.john220099.GambleMod.utils.Maths.randomFloat;

public class CrystalClicker extends Module {

    public CrystalClicker() {
        super("Crystal Clicker", "Break crystals.");
    }

    private EntityHitResult entityHit;

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;

        if (!mc.options.keyUse.isDown()) return;

        HitResult hit = mc.hitResult;
        if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
            entityHit = (EntityHitResult) hit;

            if (canAttack()) {
                float delay = randomFloat(0.8f, 1.0f);
                boolean attack = mc.player.onGround();
                float cooldown = mc.player.getAttackStrengthScale(0.5f);

                if (attack || cooldown >= delay) {
                    pressKey(mc.options.keyAttack);
                }
            }
        } else {
            entityHit = null;
        }
    }

    private boolean canAttack() {
        return entityHit != null &&
                isHoldingCrystal() &&
                entityHit.getEntity() instanceof EndCrystal &&
                entityHit.getEntity().isAlive();
    }

    private boolean isHoldingCrystal() {
        if (mc.player == null) return false;
        return isHoldingItem(Items.END_CRYSTAL);
    }

    private void pressKey(KeyMapping key) {
        InputConstants.Key bind = ((KeyMappingAccessor) key).getKey();
        KeyMapping.click(bind);
    }

    public boolean isHoldingItem(Item item) {
        if (mc.player == null) return false;
        return mc.player.getMainHandItem().getItem() == item;
    }
}