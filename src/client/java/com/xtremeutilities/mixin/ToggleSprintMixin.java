package com.xtremeutilities.mixin;

import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.Minecraft;
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
        LocalPlayer player = (LocalPlayer) (Object) this;

        boolean shouldSprint = false;
        if (config.stickySprintActive) {
            shouldSprint = true;
        } else if (config.sprintMode == ModConfig.SprintMode.TOGGLE) {
            shouldSprint = config.sprintToggled || Minecraft.getInstance().options.keySprint.isDown();
        } else if (config.sprintMode == ModConfig.SprintMode.HOLD) {
            shouldSprint = Minecraft.getInstance().options.keySprint.isDown();
        }

        if (shouldSprint) {
            if (!player.isSprinting() && this.canStartSprinting()) {
                player.setSprinting(true);
            }
        } else {
            if (player.isSprinting() && !player.isSwimming()) {
                player.setSprinting(false);
            }
        }
    }
}
