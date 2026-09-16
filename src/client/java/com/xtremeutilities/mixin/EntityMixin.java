package com.xtremeutilities.mixin;

import com.xtremeutilities.freelook.FreelookManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    private void onTurn(double deltaX, double deltaY, CallbackInfo ci) {
        if ((Object) this instanceof LocalPlayer) {
            FreelookManager manager = FreelookManager.getInstance();
            if (manager.isFreelookActive()) {
                manager.updateRotation(deltaX, deltaY);
                ci.cancel();
            }
        }
    }
}
