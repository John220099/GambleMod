package dev.john220099.GambleMod.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import dev.john220099.GambleMod.gui.GambleModScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin extends Screen {

    protected InventoryScreenMixin(Component title) {
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