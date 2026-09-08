package dev.john220099.GambleMod.mixin;

import dev.john220099.GambleMod.module.ModuleManager;
import dev.john220099.GambleMod.module.impl.NoRender;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {
    @Inject(method = "createParticle", at = @At(value = "HEAD"), cancellable = true)
    private void createParticleHook(ParticleOptions options, double x, double y, double z, double xa, double ya, double za, CallbackInfoReturnable<Particle> cir) {
        if (ModuleManager.INSTANCE.getModule(NoRender.class).isEnabled()) {
            cir.cancel();
            cir.setReturnValue(null);
        }
    }
}