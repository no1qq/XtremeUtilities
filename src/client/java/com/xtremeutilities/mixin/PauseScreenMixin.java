package com.xtremeutilities.mixin;

import com.xtremeutilities.gui.XtremeConfigScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {
    protected PauseScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addConfigButton(CallbackInfo ci) {
        int buttonWidth = 98;
        int buttonHeight = 20;
        int x = this.width - buttonWidth - 4;
        int y = this.height - buttonHeight - 4;

        this.addRenderableWidget(Button.builder(
                Component.literal("XtremeUtilities"),
                button -> {
                    if (this.minecraft != null) {
                        this.minecraft.gui.setScreen(new XtremeConfigScreen(this));
                    }
                }
        ).bounds(x, y, buttonWidth, buttonHeight).build());
    }
}
