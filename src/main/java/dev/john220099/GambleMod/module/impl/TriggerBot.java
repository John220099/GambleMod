package dev.john220099.GambleMod.module.impl;

import com.mojang.blaze3d.platform.InputConstants;
import dev.john220099.GambleMod.mixin.accessors.KeyMappingAccessor;
import dev.john220099.GambleMod.module.Module;
import net.minecraft.client.KeyMapping;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Random;

public class TriggerBot extends Module {
    private final Random random = new Random();
    private long nextAttackTime = 0;

    public TriggerBot() {
        super("TriggerBot", "Heal other players");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        if (!mc.options.keyAttack.isDown()) return;
        if (!isHoldingWeapon()) return;

        if (mc.player.getAttackStrengthScale(0.5f) < 1.0f) {
            return;
        }

        if (System.currentTimeMillis() < nextAttackTime) return;

        HitResult hit = mc.hitResult;
        if (hit == null || hit.getType() != HitResult.Type.ENTITY) return;

        EntityHitResult entityHit = (EntityHitResult) hit;
        Entity target = entityHit.getEntity();
        if (!isValidTarget(target)) return;

        double maxReach = mc.player.entityInteractionRange();
        if (mc.player.distanceTo(target) > maxReach) return;
        if (!mc.player.hasLineOfSight(target)) return;

        pressKey(mc.options.keyAttack);

        int cps = 8 + random.nextInt(5);
        nextAttackTime = System.currentTimeMillis() + (1000 / cps);
    }

    private boolean isValidTarget(Entity entity) {
        return entity instanceof LivingEntity && entity.isAlive() && entity != mc.player;
    }

    private boolean isHoldingWeapon() {
        assert mc.player != null;
        var stack = mc.player.getMainHandItem();
        return stack.is(ItemTags.SWORDS) || stack.getItem() instanceof AxeItem;
    }

    private void pressKey(KeyMapping key) {
        InputConstants.Key bind = ((KeyMappingAccessor) key).getKey();
        KeyMapping.click(bind);
    }
}