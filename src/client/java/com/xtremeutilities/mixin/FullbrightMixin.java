package com.xtremeutilities.mixin;

import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapRenderStateExtractor.class)
public class FullbrightMixin {
    @Shadow
    private boolean needsUpdate;

    private boolean lastFullbright;

    @Inject(method = "extract", at = @At("HEAD"))
    private void beforeExtract(LightmapRenderState state, float partialTick, CallbackInfo ci) {
        boolean fullbright = ModConfig.getInstance().fullbright;
        if (fullbright != this.lastFullbright) {
            this.needsUpdate = true;
            this.lastFullbright = fullbright;
        }
        if (fullbright) {
            this.needsUpdate = true;
        }
    }

    @Inject(method = "extract", at = @At("TAIL"))
    private void afterExtract(LightmapRenderState state, float partialTick, CallbackInfo ci) {
        if (ModConfig.getInstance().fullbright) {
            state.needsUpdate = true;
            state.nightVisionEffectIntensity = 1.0f;
            state.brightness = 1.0f;
            state.darknessEffectScale = 0.0f;
            state.bossOverlayWorldDarkening = 0.0f;
            state.blockFactor = 1.0f;
            state.skyFactor = 1.0f;
        }
    }
}
