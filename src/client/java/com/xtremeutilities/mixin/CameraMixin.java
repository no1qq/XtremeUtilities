package com.xtremeutilities.mixin;

import com.xtremeutilities.freelook.FreelookManager;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    private Entity entity;

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(F)F"))
    private void onAlignWithEntity(float partialTick, CallbackInfo ci) {
        FreelookManager manager = FreelookManager.getInstance();
        if (manager.isFreelookActive() && this.entity instanceof LocalPlayer) {
            this.setRotation(manager.getCameraYaw(), manager.getCameraPitch());
        }
    }
}
