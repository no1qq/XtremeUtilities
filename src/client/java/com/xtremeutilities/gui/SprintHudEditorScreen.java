package com.xtremeutilities.gui;

import com.xtremeutilities.config.ConfigManager;
import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SprintHudEditorScreen extends Screen {
    private final Screen parent;
    private boolean dragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public SprintHudEditorScreen(Screen parent) {
        super(Component.literal("Sprint HUD Position Editor"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int buttonY = this.height - 28;

        this.addRenderableWidget(Button.builder(
                Component.literal("Reset Position"),
                button -> {
                    ModConfig config = ModConfig.getInstance();
                    config.sprintHudX = 4;
                    config.sprintHudY = 4;
                    ConfigManager.save();
                }
        ).bounds(centerX - 105, buttonY, 100, 20).build());

        this.addRenderableWidget(Button.builder(
                CommonComponents.GUI_DONE,
                button -> onClose()
        ).bounds(centerX + 5, buttonY, 100, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        ModConfig config = ModConfig.getInstance();
        String text = config.sprintMode == ModConfig.SprintMode.HOLD ? "[Sprinting (Holding)]" : "[Sprinting (Toggled)]";
        int textWidth = this.font.width(text);
        int textHeight = 9;

        graphics.centeredText(this.font, this.title, this.width / 2, 12, 0xFFFFFF);
        graphics.centeredText(this.font, Component.literal("Click and drag the indicator box to reposition it"), this.width / 2, 24, 0xAAAAAA);
        graphics.centeredText(this.font, Component.literal("X: " + config.sprintHudX + "  Y: " + config.sprintHudY), this.width / 2, 36, 0x888888);

        int x = config.sprintHudX;
        int y = config.sprintHudY;

        graphics.fill(x - 3, y - 3, x + textWidth + 3, y + textHeight + 3, 0x70000000);
        graphics.fill(x - 3, y - 3, x + textWidth + 3, y - 2, 0xFF55FF55);
        graphics.fill(x - 3, y + textHeight + 2, x + textWidth + 3, y + textHeight + 3, 0xFF55FF55);
        graphics.fill(x - 3, y - 3, x - 2, y + textHeight + 3, 0xFF55FF55);
        graphics.fill(x + textWidth + 2, y - 3, x + textWidth + 3, y + textHeight + 3, 0xFF55FF55);

        graphics.text(this.font, Component.literal(text), x, y, 0xFFFFFF, true);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        ModConfig config = ModConfig.getInstance();
        String text = config.sprintMode == ModConfig.SprintMode.HOLD ? "[Sprinting (Holding)]" : "[Sprinting (Toggled)]";
        int textWidth = this.font.width(text);
        int textHeight = 9;

        int x = config.sprintHudX;
        int y = config.sprintHudY;

        if (event.button() == 0 && event.x() >= x - 3 && event.x() <= x + textWidth + 3 && event.y() >= y - 3 && event.y() <= y + textHeight + 3) {
            this.dragging = true;
            this.dragOffsetX = (int) event.x() - x;
            this.dragOffsetY = (int) event.y() - y;
            return true;
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        if (this.dragging) {
            ModConfig config = ModConfig.getInstance();
            String text = config.sprintMode == ModConfig.SprintMode.HOLD ? "[Sprinting (Holding)]" : "[Sprinting (Toggled)]";
            int textWidth = this.font.width(text);
            int textHeight = 9;

            config.sprintHudX = Math.max(0, Math.min(this.width - textWidth, (int) event.x() - this.dragOffsetX));
            config.sprintHudY = Math.max(0, Math.min(this.height - textHeight, (int) event.y() - this.dragOffsetY));
            return true;
        }

        return super.mouseDragged(event, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (this.dragging) {
            this.dragging = false;
            ConfigManager.save();
            return true;
        }

        return super.mouseReleased(event);
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        this.minecraft.setScreenAndShow(this.parent);
    }
}
