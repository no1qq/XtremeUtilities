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

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void handleToggleSprint(CallbackInfo ci) {
        ModConfig config = ModConfig.getInstance();
        if (config.stickySprintActive && config.sprintMode == ModConfig.SprintMode.TOGGLE) {
            LocalPlayer player = (LocalPlayer) (Object) this;
            if (!player.isSprinting() && this.canStartSprinting()) {
                player.setSprinting(true);
            }
        }
    }
}
