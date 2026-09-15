package com.xtremeutilities.mixin;

import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingOverlay.class)
public class FadelessReloadMixin {
    @Shadow
    private long fadeOutStart;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "tick", at = @At("TAIL"))
    private void bypassFadeOut(CallbackInfo ci) {
        if (ModConfig.getInstance().fadelessReload && this.fadeOutStart > -1L) {
            this.minecraft.gui.setOverlay(null);
        }
    }
}
