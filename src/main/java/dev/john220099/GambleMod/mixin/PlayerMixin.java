package dev.john220099.GambleMod.mixin;

import dev.john220099.GambleMod.module.ModuleManager;
import dev.john220099.GambleMod.module.impl.Reach;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "entityInteractionRange", at = @At("HEAD"), cancellable = true)
    private void onEntityInteractionRange(CallbackInfoReturnable<Double> cir) {
        Reach reach = ModuleManager.INSTANCE.getModule(Reach.class);
        if (reach == null || !reach.isEnabled()) return;

        Player self = (Player) (Object) this;
        if (self != Minecraft.getInstance().player) return;

        double baseReach = 3.0;
        double extraReach = baseReach * reach.getReach();
        cir.setReturnValue(baseReach + extraReach);
    }

    @Inject(method = "blockInteractionRange", at = @At("HEAD"), cancellable = true)
    private void onBlockInteractionRange(CallbackInfoReturnable<Double> cir) {
        Reach reach = ModuleManager.INSTANCE.getModule(Reach.class);
        if (reach == null || !reach.isEnabled()) return;

        Player self = (Player) (Object) this;
        if (self != Minecraft.getInstance().player) return;

        double baseReach = 4.5;
        double extraReach = baseReach * reach.getReach();
        cir.setReturnValue(baseReach + extraReach);
    }
}