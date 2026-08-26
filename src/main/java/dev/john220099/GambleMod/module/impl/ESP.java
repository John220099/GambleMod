
package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.module.Module;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ESP extends Module {

    public ESP() {
        super("ESP", "Show boxes.");
    }

    public void onRender2D(GuiGraphicsExtractor graphics, float partialTicks) {
        if (mc.player == null || mc.level == null) return;

        Camera camera = mc.gameRenderer.mainCamera();
        Vec3 camPos = camera.position();

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity == mc.player || !(entity instanceof LivingEntity) || !entity.isAlive()) continue;

            AABB box = entity.getBoundingBox();
            Vec3 pos = entity.getPosition(partialTicks);

            Vec3 entityCenter = pos.add(0, box.getYsize() / 2, 0);
            if (entityCenter.subtract(camPos).dot(new Vec3(camera.forwardVector())) < 0) continue;

            Matrix4f viewProjMatrix = new Matrix4f();
            camera.getViewRotationProjectionMatrix(viewProjMatrix);

            float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE;
            boolean anyBehind = false;

            for (int i = 0; i < 8; i++) {
                double x = (i & 1) == 0 ? box.minX : box.maxX;
                double y = (i & 2) == 0 ? box.minY : box.maxY;
                double z = (i & 4) == 0 ? box.minZ : box.maxZ;

                Vec3 corner = new Vec3(x, y, z);
                Vec3 relative = corner.subtract(camPos);

                Vector4f clip = new Vector4f((float) relative.x, (float) relative.y, (float) relative.z, 1.0f);
                clip.mul(viewProjMatrix);

                if (clip.w <= 0) {
                    anyBehind = true;
                    break;
                }

                float screenX = (clip.x / clip.w * 0.5f + 0.5f) * mc.getWindow().getGuiScaledWidth();
                float screenY = (1.0f - (clip.y / clip.w * 0.5f + 0.5f)) * mc.getWindow().getGuiScaledHeight();

                minX = Math.min(minX, screenX);
                minY = Math.min(minY, screenY);
                maxX = Math.max(maxX, screenX);
                maxY = Math.max(maxY, screenY);
            }

            if (anyBehind) continue;

            int color = 0xFF00FF00;
            graphics.outline((int) minX, (int) minY, (int) (maxX - minX), (int) (maxY - minY), color);
        }
    }
}