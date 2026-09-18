package com.xtremeutilities.gui;

import com.xtremeutilities.config.ConfigManager;
import com.xtremeutilities.config.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToggleSprintConfigScreen extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger("XtremeUtilities");
    private final Screen parent;

    public ToggleSprintConfigScreen(Screen parent) {
        super(Component.literal("Toggle Sprint Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = Math.max(32, this.height / 2 - 76);
        ModConfig config = ModConfig.getInstance();

        this.addRenderableWidget(Button.builder(
                Component.literal("Sprint Mode: " + config.sprintMode.name()),
                button -> {
                    config.sprintMode = config.sprintMode == ModConfig.SprintMode.TOGGLE ? ModConfig.SprintMode.HOLD : ModConfig.SprintMode.TOGGLE;
                    button.setMessage(Component.literal("Sprint Mode: " + config.sprintMode.name()));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Toggle Sprint: " + (config.sprintToggled ? "ON" : "OFF")),
                button -> {
                    config.sprintToggled = !config.sprintToggled;
                    button.setMessage(Component.literal("Toggle Sprint: " + (config.sprintToggled ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY + 24, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Sprint HUD Indicator: " + (config.sprintHudEnabled ? "ON" : "OFF")),
                button -> {
                    config.sprintHudEnabled = !config.sprintHudEnabled;
                    button.setMessage(Component.literal("Sprint HUD Indicator: " + (config.sprintHudEnabled ? "ON" : "OFF")));
                    ConfigManager.save();
                }
        ).bounds(centerX - 100, startY + 48, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Edit HUD Position..."),
                button -> this.minecraft.setScreenAndShow(new SprintHudEditorScreen(this))
        ).bounds(centerX - 100, startY + 72, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Smart Sprint: " + (config.smartSprint ? "ON" : "OFF")).withStyle(ChatFormatting.RED),
                button -> {
                    config.smartSprint = !config.smartSprint;
                    button.setMessage(Component.literal("Smart Sprint: " + (config.smartSprint ? "ON" : "OFF")).withStyle(ChatFormatting.RED));
                    ConfigManager.save();
                    LOGGER.info("[XtremeUtilities] Smart Sprint toggled: {}", config.smartSprint ? "ON" : "OFF");
                    System.out.println("[XtremeUtilities] Smart Sprint toggled: " + (config.smartSprint ? "ON" : "OFF"));
                }
        ).tooltip(Tooltip.create(Component.literal("Releases forward movement while falling toward an opponent in reach to land critical hits. This may be prohibited on some servers or trigger anti-cheat detection."))).bounds(centerX - 100, startY + 96, 200, 20).build());

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
