package com.xtremeutilities.mixin;

import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.IntSupplier;

@Mixin(LoadingOverlay.class)
public class DarkLoadingScreenMixin {
    @Redirect(
            method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/function/IntSupplier;getAsInt()I"
            )
    )
    private int modifyBrandBackgroundColor(IntSupplier supplier) {
        if (ModConfig.getInstance().darkLoadingScreen) {
            return 0xFF121212;
        }
        return supplier.getAsInt();
    }
}
