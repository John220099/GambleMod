package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.module.Module;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.Comparator;
import java.util.stream.StreamSupport;

public class AimAssist extends Module {

    private final float reach = 4.5f;
    private final boolean onlyMoving = true;

    private float currentYawSpeed = 0.0f;
    private float currentPitchSpeed = 0.0f;

    public AimAssist() {
        super("AimAssist", "Look funny at things.");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;

        LivingEntity target = getTarget();
        if (target == null) {
            currentYawSpeed = 0.0f;
            currentPitchSpeed = 0.0f;
            return;
        }

        boolean onlyWeapons = true;
        boolean weaponOk = !onlyWeapons || isHoldingWeapon() || isHoldingMace();
        if (!weaponOk || mc.player.isUsingItem()) return;

        if (getDistanceToCentreOfEntity(target) > reach) return;
        if (!isInAngle(target)) return;

        float[] current = {mc.player.getYRot(), mc.player.getXRot()};
        float[] toTarget = getCentreRotations(current, mc.player.getEyePosition(), target);

        float deltaYaw = Mth.wrapDegrees(toTarget[0] - current[0]);
        float deltaPitch = toTarget[1] - current[1];


        float maxSpeed = 8.0f;
        float targetYawSpeed = Mth.clamp(deltaYaw * 0.15f, -maxSpeed, maxSpeed);
        float targetPitchSpeed = Mth.clamp(deltaPitch * 0.15f, -maxSpeed, maxSpeed);

        float smoothFactor = 0.3f;
        currentYawSpeed = Mth.lerp(smoothFactor, currentYawSpeed, targetYawSpeed);
        currentPitchSpeed = Mth.lerp(smoothFactor, currentPitchSpeed, targetPitchSpeed);

        mc.player.setYRot(mc.player.getYRot() + currentYawSpeed);
        mc.player.setXRot(mc.player.getXRot() + currentPitchSpeed);
    }

    private LivingEntity getTarget() {
        if (mc.level == null || mc.player == null) return null;

        return StreamSupport.stream(mc.level.entitiesForRendering().spliterator(), false)
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .filter(entity -> entity != mc.player)
                .filter(Entity::isAlive)
                .filter(entity -> entity.getHealth() > 0)
                .filter(entity -> mc.player.distanceTo(entity) <= reach)
                .filter(_ -> !onlyMoving || isPlayerMoving())
                .min(Comparator.comparingDouble(this::getDistanceToCentreOfEntity))
                .orElse(null);
    }

    private boolean isPlayerMoving() {
        assert mc.player != null;
        return mc.options.keyUp.isDown()
                || mc.options.keyDown.isDown()
                || mc.options.keyLeft.isDown()
                || mc.options.keyRight.isDown();
    }

    private boolean isInAngle(LivingEntity target) {
        assert mc.player != null;
        float playerYaw = mc.player.getYRot();
        float targetYaw = getRotations(new float[]{playerYaw, mc.player.getXRot()}, mc.player.getEyePosition(), target)[0];
        float diff = Mth.wrapDegrees(targetYaw - playerYaw);
        float angle = 60.0f;
        return Math.abs(diff) <= angle / 2f;
    }

    private boolean isHoldingWeapon() {
        assert mc.player != null;
        var stack = mc.player.getMainHandItem();
        return stack.is(ItemTags.SWORDS) || stack.is(ItemTags.AXES);
    }

    private boolean isHoldingMace() {
        assert mc.player != null;
        Item item = mc.player.getMainHandItem().getItem();
        return item instanceof MaceItem;
    }

    private float[] getRotations(float[] last, Vec3 eye, Entity entity) {
        Vec3 to = getClosestPoint(entity);
        Vec3 diff = to.subtract(eye);
        double dist = Math.sqrt(diff.x * diff.x + diff.z * diff.z);
        float pitch = (float) Math.toDegrees(-Math.atan2(diff.y, dist));
        float yaw = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90;
        yaw = unwrap(last[0], yaw);
        return new float[]{yaw, pitch};
    }

    private float[] getCentreRotations(float[] last, Vec3 eye, Entity entity) {
        Vec3 to = entity.getBoundingBox().getCenter();
        Vec3 diff = to.subtract(eye);
        double dist = Math.sqrt(diff.x * diff.x + diff.z * diff.z);
        float pitch = (float) Math.toDegrees(-Math.atan2(diff.y, dist));
        float yaw = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90;
        yaw = unwrap(last[0], yaw);
        return new float[]{yaw, pitch};
    }

    private static float unwrap(float oldYaw, float currentYaw) {
        float diff = currentYaw - (oldYaw % 360);
        if (diff > 180) diff -= 360;
        if (diff < -180) diff += 360;
        return oldYaw + diff;
    }

    private Vec3 getClosestPoint(Entity target) {
        AABB hb = target.getBoundingBox();
        assert mc.player != null;
        Vec3 eyePos = mc.player.getEyePosition();
        double cx = Mth.clamp(eyePos.x, hb.minX, hb.maxX);
        double cy = Mth.clamp(eyePos.y, hb.minY, hb.maxY);
        double cz = Mth.clamp(eyePos.z, hb.minZ, hb.maxZ);
        return new Vec3(cx, cy, cz);
    }

    private double getDistanceToCentreOfEntity(Entity target) {
        assert mc.player != null;
        return mc.player.getEyePosition().distanceTo(target.getBoundingBox().getCenter());
    }
}
