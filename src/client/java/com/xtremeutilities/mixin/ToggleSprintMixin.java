package com.xtremeutilities.mixin;

import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;
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

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V", shift = At.Shift.AFTER))
    private void handleToggleSprint(CallbackInfo ci) {
        ModConfig config = ModConfig.getInstance();
        LocalPlayer player = (LocalPlayer) (Object) this;

        boolean shouldSprint = false;
        if (config.sprintMode == ModConfig.SprintMode.TOGGLE) {
            shouldSprint = config.sprintToggled;
        } else if (config.sprintMode == ModConfig.SprintMode.HOLD) {
            shouldSprint = Minecraft.getInstance().options.keySprint.isDown();
        }

        if (player.input != null && player.input.keyPresses != null) {
            Input old = player.input.keyPresses;
            player.input.keyPresses = new Input(
                    old.forward(),
                    old.backward(),
                    old.left(),
                    old.right(),
                    old.jump(),
                    old.shift(),
                    shouldSprint
            );
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
