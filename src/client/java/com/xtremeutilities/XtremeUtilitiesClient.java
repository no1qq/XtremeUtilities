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

    @Override
    public void onInitializeClient() {
        ConfigManager.load();

        fullbrightKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.xtremeutilities.fullbright",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_B,
                KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ModConfig config = ModConfig.getInstance();

            if (client.options != null && client.options.toggleSprint().get()) {
                client.options.toggleSprint().set(false);
            }

            while (fullbrightKey.consumeClick()) {
                config.fullbright = !config.fullbright;
                ConfigManager.save();
                if (client.player != null) {
                    client.player.sendOverlayMessage(Component.literal("Fullbright: " + (config.fullbright ? "ON" : "OFF")));
                }
            }

            if (client.player != null && client.gui.screen() == null) {
                if (config.sprintMode == ModConfig.SprintMode.TOGGLE) {
                    while (client.options.keySprint.consumeClick()) {
                        config.sprintToggled = !config.sprintToggled;
                        ConfigManager.save();
                        if (!config.sprintToggled) {
                            client.options.keySprint.setDown(false);
                            if (!client.player.isSwimming()) {
                                client.player.setSprinting(false);
                            }
                        }
                        client.player.sendOverlayMessage(Component.literal("Toggle Sprint: " + (config.sprintToggled ? "ON" : "OFF")));
                    }
                }
            }
        });
    }
}
