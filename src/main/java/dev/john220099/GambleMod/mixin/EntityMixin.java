package dev.john220099.GambleMod.mixin;

import dev.john220099.GambleMod.module.ModuleManager;
import dev.john220099.GambleMod.module.impl.HitBox;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "getBoundingBox", at = @At("RETURN"), cancellable = true)
    private void getBoundingBox(CallbackInfoReturnable<AABB> cir) {
        HitBox hitBox = ModuleManager.INSTANCE.getModule(HitBox.class);
        if (hitBox == null || !hitBox.isEnabled()) return;

        Entity self = (Entity) (Object) this;
        if (self == Minecraft.getInstance().player) return;
        if (!(self instanceof LivingEntity)) return;
        if (!self.isAlive()) return;

        AABB original = cir.getReturnValue();
        float expansion = hitBox.getExpansion();
        if (expansion > 0) {
            cir.setReturnValue(original.inflate(expansion));
        }
    }
}