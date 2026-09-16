package com.xtremeutilities.gui;

import com.xtremeutilities.config.ConfigManager;
import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class NoHurtCamConfigScreen extends Screen {
    private final Screen parent;

    public NoHurtCamConfigScreen(Screen parent) {
        super(Component.literal("NoHurtCam Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = Math.max(32, this.height / 2 - 40);
        ModConfig config = ModConfig.getInstance();

        this.addRenderableWidget(Button.builder(
                Component.literal("NoHurtCam: " + (config.noHurtCamEnabled ? "ON" : "OFF")),
                button -> {
                    config.noHurtCamEnabled = !config.noHurtCamEnabled;
                    button.setMessage(Component.literal("NoHurtCam: " + (config.noHurtCamEnabled ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY, 200, 20).build());

        double initialSliderValue = Math.max(0.0, Math.min(1.0, config.hurtCamShake));
        this.addRenderableWidget(new AbstractSliderButton(centerX - 100, startY + 24, 200, 20, Component.empty(), initialSliderValue) {
            {
                this.updateMessage();
            }

            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal("Camera Hurt Shake: " + (int) Math.round(config.hurtCamShake * 100) + "%"));
            }

            @Override
            protected void applyValue() {
                config.hurtCamShake = this.value;
                ConfigManager.save();
            }
        });

        this.addRenderableWidget(Button.builder(
                CommonComponents.GUI_DONE,
                button -> onClose()
        ).bounds(centerX - 100, startY + 56, 200, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        boolean handled = super.mouseClicked(event, doubleClick);
        this.setFocused(null);
        this.clearFocus();
        return handled;
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        this.minecraft.setScreenAndShow(this.parent);
    }
}
