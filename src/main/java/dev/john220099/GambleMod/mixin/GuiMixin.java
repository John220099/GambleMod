package dev.john220099.GambleMod.mixin;

import dev.john220099.GambleMod.module.ModuleManager;
import dev.john220099.GambleMod.module.impl.ESP;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow @Final private GuiRenderState guiRenderState;

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void render2D(DeltaTracker deltaTracker, boolean shouldRenderLevel, boolean resourcesLoaded, CallbackInfo ci) {
        ESP esp = ModuleManager.INSTANCE.getModule(ESP.class);
        if (esp != null && esp.isEnabled() && shouldRenderLevel) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) return;

            int xMouse = (int)mc.mouseHandler.getScaledXPos(mc.getWindow());
            int yMouse = (int)mc.mouseHandler.getScaledYPos(mc.getWindow());

            GuiGraphicsExtractor graphics = new GuiGraphicsExtractor(
                    mc,
                    this.guiRenderState,
                    xMouse,
                    yMouse
            );

            esp.onRender2D(graphics, deltaTracker.getGameTimeDeltaPartialTick(true));
        }
    }
}