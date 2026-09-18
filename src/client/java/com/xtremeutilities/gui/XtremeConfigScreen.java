package com.xtremeutilities.gui;

import com.xtremeutilities.config.ConfigManager;
import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class XtremeConfigScreen extends Screen {
    private final Screen parent;

    public XtremeConfigScreen(Screen parent) {
        super(Component.literal("XtremeUtilities Configuration"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = Math.max(48, this.height / 2 - 40);
        ModConfig config = ModConfig.getInstance();

        this.addRenderableWidget(Button.builder(
                Component.literal("Fullbright: " + (config.fullbright ? "ON" : "OFF")),
                button -> {
                    config.fullbright = !config.fullbright;
                    button.setMessage(Component.literal("Fullbright: " + (config.fullbright ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 155, startY, 150, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Dark Screen: " + (config.darkLoadingScreen ? "ON" : "OFF")),
                button -> {
                    config.darkLoadingScreen = !config.darkLoadingScreen;
                    button.setMessage(Component.literal("Dark Screen: " + (config.darkLoadingScreen ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 155, startY + 24, 150, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Fadeless: " + (config.fadelessReload ? "ON" : "OFF")),
                button -> {
                    config.fadelessReload = !config.fadelessReload;
                    button.setMessage(Component.literal("Fadeless: " + (config.fadelessReload ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 155, startY + 48, 150, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Toggle Sprint..."),
                button -> this.minecraft.gui.setScreen(new ToggleSprintConfigScreen(this))
        ).bounds(centerX + 5, startY, 150, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Freelook..."),
                button -> this.minecraft.gui.setScreen(new FreelookConfigScreen(this))
        ).bounds(centerX + 5, startY + 24, 150, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("NoHurtCam..."),
                button -> this.minecraft.gui.setScreen(new NoHurtCamConfigScreen(this))
        ).bounds(centerX + 5, startY + 48, 150, 20).build());

        this.addRenderableWidget(Button.builder(
                CommonComponents.GUI_DONE,
                button -> onClose()
        ).bounds(centerX - 100, startY + 80, 200, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        int centerX = this.width / 2;
        int startY = Math.max(48, this.height / 2 - 40);
        graphics.centeredText(this.font, this.title, centerX, 18, 0xFFFFFFFF);
        graphics.centeredText(this.font, Component.literal("Toggles"), centerX - 80, startY - 14, 0xFFAAAAAA);
        graphics.centeredText(this.font, Component.literal("Features"), centerX + 80, startY - 14, 0xFFAAAAAA);
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
        this.minecraft.gui.setScreen(this.parent);
    }
}
