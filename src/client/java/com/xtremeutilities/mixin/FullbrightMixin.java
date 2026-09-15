package com.xtremeutilities.mixin;

import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class FullbrightMixin {
    @Inject(method = "nightVisionScale", at = @At("RETURN"), cancellable = true)
    private static void fullbrightNightVision(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (ModConfig.getInstance().fullbright) {
            cir.setReturnValue(1.0f);
        }
    }
}
