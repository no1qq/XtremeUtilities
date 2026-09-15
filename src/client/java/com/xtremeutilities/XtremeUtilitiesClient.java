package com.xtremeutilities;

import com.mojang.blaze3d.platform.InputConstants;
import com.xtremeutilities.config.ConfigManager;
import com.xtremeutilities.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

public class XtremeUtilitiesClient implements ClientModInitializer {
    public static KeyMapping fullbrightKey;
    public static KeyMapping sprintToggleKey;

    @Override
    public void onInitializeClient() {
        ConfigManager.load();

        fullbrightKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.xtremeutilities.fullbright",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_B,
                KeyMapping.Category.MISC
        ));

        sprintToggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.xtremeutilities.sprint_toggle",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_V,
                KeyMapping.Category.MOVEMENT
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ModConfig config = ModConfig.getInstance();

            while (fullbrightKey.consumeClick()) {
                config.fullbright = !config.fullbright;
                ConfigManager.save();
                if (client.player != null) {
                    client.player.sendOverlayMessage(Component.literal("Fullbright: " + (config.fullbright ? "ON" : "OFF")));
                }
            }

            while (sprintToggleKey.consumeClick()) {
                config.stickySprintActive = !config.stickySprintActive;
                ConfigManager.save();
                if (client.player != null) {
                    client.player.sendOverlayMessage(Component.literal("Sticky Sprint: " + (config.stickySprintActive ? "ON" : "OFF")));
                }
            }
        });
    }
}
