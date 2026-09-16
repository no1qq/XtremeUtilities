package com.xtremeutilities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    private GameRenderState gameRenderState;

    @Inject(method = "extractOptions", at = @At("TAIL"))
    private void modifyDamageTilt(CallbackInfo ci) {
        ModConfig config = ModConfig.getInstance();
        if (config.noHurtCamEnabled) {
            this.gameRenderState.optionsRenderState.damageTiltStrength = config.hurtCamShake;
        }
    }

    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void onBobHurt(CameraRenderState cameraRenderState, PoseStack poseStack, CallbackInfo ci) {
        ModConfig config = ModConfig.getInstance();
        if (config.noHurtCamEnabled && config.hurtCamShake <= 0.0) {
            if (cameraRenderState.entityRenderState == null || !cameraRenderState.entityRenderState.isDeadOrDying) {
                ci.cancel();
            }
        }
    }
}
