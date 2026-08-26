package dev.john220099.GambleMod.mixin;

import dev.john220099.GambleMod.module.ModuleManager;
import dev.john220099.GambleMod.module.impl.CameraFix;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private float xRot;

    @Shadow
    private float yRot;

    @Shadow
    private boolean detached;

    @Shadow
    protected abstract void move(float forwards, float up, float right);

    @Shadow
    protected abstract void setRotation(float yRot, float xRot);

    @Inject(method = "alignWithEntity", at = @At("TAIL"))
    private void NierCamera(float partialTicks, CallbackInfo ci) {
        CameraFix cameraFix = ModuleManager.INSTANCE.getModule(CameraFix.class);
        if (cameraFix == null || !cameraFix.isEnabled()) return;

        if (this.detached) {
            this.move(-2.5f, -1.0f, 1.5f);

            this.setRotation(this.yRot + 5.0f, this.xRot - 10.0f);
        }
    }
}