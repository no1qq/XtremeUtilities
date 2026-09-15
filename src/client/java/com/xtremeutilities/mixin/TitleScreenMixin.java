package com.xtremeutilities.mixin;

import com.xtremeutilities.gui.XtremeConfigScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addConfigButton(CallbackInfo ci) {
        int buttonWidth = 98;
        int buttonHeight = 20;
        int x = this.width / 2 + 104;
        int y = this.height / 4 + 48 + 72 + 12;

        this.addRenderableWidget(Button.builder(
                Component.literal("XtremeUtilities"),
                button -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreenAndShow(new XtremeConfigScreen(this));
                    }
                }
        ).bounds(x, y, buttonWidth, buttonHeight).build());
    }
}
