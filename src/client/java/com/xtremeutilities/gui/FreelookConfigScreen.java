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

public class FreelookConfigScreen extends Screen {
    private final Screen parent;

    public FreelookConfigScreen(Screen parent) {
        super(Component.literal("Freelook Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = Math.max(32, this.height / 2 - 76);
        ModConfig config = ModConfig.getInstance();

        this.addRenderableWidget(Button.builder(
                Component.literal("Freelook: " + (config.freelookEnabled ? "ON" : "OFF")),
                button -> {
                    config.freelookEnabled = !config.freelookEnabled;
                    button.setMessage(Component.literal("Freelook: " + (config.freelookEnabled ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Mode: " + config.freelookMode.name()),
                button -> {
                    config.freelookMode = config.freelookMode == ModConfig.FreelookMode.HOLD ? ModConfig.FreelookMode.TOGGLE : ModConfig.FreelookMode.HOLD;
                    button.setMessage(Component.literal("Mode: " + config.freelookMode.name()));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY + 24, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Invert X: " + (config.freelookInvertX ? "ON" : "OFF")),
                button -> {
                    config.freelookInvertX = !config.freelookInvertX;
                    button.setMessage(Component.literal("Invert X: " + (config.freelookInvertX ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY + 48, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Invert Y: " + (config.freelookInvertY ? "ON" : "OFF")),
                button -> {
                    config.freelookInvertY = !config.freelookInvertY;
                    button.setMessage(Component.literal("Invert Y: " + (config.freelookInvertY ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY + 72, 200, 20).build());

        double initialSliderValue = Math.max(0.0, Math.min(1.0, (config.freelookSpeed - 0.2) / 1.8));
        this.addRenderableWidget(new AbstractSliderButton(centerX - 100, startY + 96, 200, 20, Component.empty(), initialSliderValue) {
            {
                this.updateMessage();
            }

            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal("Freelook Speed: " + (int) Math.round(config.freelookSpeed * 100) + "%"));
            }

            @Override
            protected void applyValue() {
                config.freelookSpeed = 0.2 + this.value * 1.8;
                ConfigManager.save();
            }
        });

        this.addRenderableWidget(Button.builder(
                CommonComponents.GUI_DONE,
                button -> onClose()
        ).bounds(centerX - 100, startY + 128, 200, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        boolean handled = super.mouseClicked(event, doubleClick);
        if (!(this.getFocused() instanceof AbstractSliderButton)) {
            this.setFocused(null);
            this.clearFocus();
        }
        return handled;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        boolean handled = super.mouseReleased(event);
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
