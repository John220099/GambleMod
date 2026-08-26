package dev.john220099.GambleMod.mixin;

import dev.john220099.GambleMod.module.ModuleManager;
import dev.john220099.GambleMod.module.impl.OpSec;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {

    @Inject(method = "extractLines", at = @At("HEAD"), cancellable = true)
    private void extractLines(CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModule(OpSec.class).isEnabled()) {
            ci.cancel();
        }
    }
}