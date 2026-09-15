package com.xtremeutilities.gui;

import com.xtremeutilities.config.ConfigManager;
import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
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
        int startY = this.height / 6;
        ModConfig config = ModConfig.getInstance();

        this.addRenderableWidget(Button.builder(
                Component.literal("Fullbright: " + (config.fullbright ? "ON" : "OFF")),
                button -> {
                    config.fullbright = !config.fullbright;
                    button.setMessage(Component.literal("Fullbright: " + (config.fullbright ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Sprint Mode: " + config.sprintMode.name()),
                button -> {
                    config.sprintMode = config.sprintMode == ModConfig.SprintMode.TOGGLE ? ModConfig.SprintMode.HOLD : ModConfig.SprintMode.TOGGLE;
                    button.setMessage(Component.literal("Sprint Mode: " + config.sprintMode.name()));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY + 24, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Sticky Sprint: " + (config.stickySprintActive ? "ON" : "OFF")),
                button -> {
                    config.stickySprintActive = !config.stickySprintActive;
                    button.setMessage(Component.literal("Sticky Sprint: " + (config.stickySprintActive ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY + 48, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Dark Loading Screen: " + (config.darkLoadingScreen ? "ON" : "OFF")),
                button -> {
                    config.darkLoadingScreen = !config.darkLoadingScreen;
                    button.setMessage(Component.literal("Dark Loading Screen: " + (config.darkLoadingScreen ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY + 72, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Fadeless Reloading: " + (config.fadelessReload ? "ON" : "OFF")),
                button -> {
                    config.fadelessReload = !config.fadelessReload;
                    button.setMessage(Component.literal("Fadeless Reloading: " + (config.fadelessReload ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY + 96, 200, 20).build());

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
    public void onClose() {
        ConfigManager.save();
        this.minecraft.setScreenAndShow(this.parent);
    }
}
