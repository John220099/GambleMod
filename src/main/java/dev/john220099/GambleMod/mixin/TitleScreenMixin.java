package dev.john220099.GambleMod.mixin;

import dev.john220099.GambleMod.gui.GambleModScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addGambleModButton(CallbackInfo ci) {
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("GambleMod"),
                        _ -> this.minecraft.setScreenAndShow(new GambleModScreen(this))
                ).bounds(this.width - 110, this.height - 30, 100, 20).build()
        );
    }
}