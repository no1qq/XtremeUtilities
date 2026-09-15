package com.xtremeutilities.mixin;

import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class ToggleSprintMixin {
    @Shadow
    private boolean canStartSprinting() {
        return false;
    }

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    public abstract void setSprinting(boolean sprinting);

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void handleToggleSprint(CallbackInfo ci) {
        ModConfig config = ModConfig.getInstance();
        if (config.stickySprintActive && config.sprintMode == ModConfig.SprintMode.TOGGLE) {
            if (!this.isSprinting() && this.canStartSprinting()) {
                this.setSprinting(true);
            }
        }
    }
}
