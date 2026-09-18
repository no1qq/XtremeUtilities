package com.xtremeutilities;

import com.mojang.blaze3d.platform.InputConstants;
import com.xtremeutilities.config.ConfigManager;
import com.xtremeutilities.config.ModConfig;
import com.xtremeutilities.freelook.FreelookManager;
import com.xtremeutilities.gui.SprintHudEditorScreen;
import com.xtremeutilities.sprint.SmartSprintManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class XtremeUtilitiesClient implements ClientModInitializer {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("xtremeutilities", "general")
    );

    public static KeyMapping fullbrightKey;
    public static KeyMapping freelookKey;
    public static KeyMapping noHurtCamKey;

    @Override
    public void onInitializeClient() {
        ConfigManager.load();

        fullbrightKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.xtremeutilities.fullbright",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_B,
                CATEGORY
        ));

        freelookKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.xtremeutilities.freelook",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_LCONTROL,
                CATEGORY
        ));

        noHurtCamKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.xtremeutilities.nohurtcam",
                InputConstants.Type.KEYBOARD,
                InputConstants.UNKNOWN.getValue(),
                CATEGORY
        ));

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath("xtremeutilities", "sprint_hud"),
                (graphics, deltaTracker) -> {
                    ModConfig config = ModConfig.getInstance();
                    if (!config.sprintHudEnabled) {
                        return;
                    }

                    Minecraft mc = Minecraft.getInstance();
                    if (mc.player == null || mc.level == null) {
                        return;
                    }

                    if (mc.gui.screen() != null && !(mc.gui.screen() instanceof SprintHudEditorScreen)) {
                        return;
                    }

                    String text = null;
                    if (config.sprintMode == ModConfig.SprintMode.TOGGLE) {
                        if (config.sprintToggled) {
                            text = "[Sprinting (Toggled)]";
                        }
                    } else if (config.sprintMode == ModConfig.SprintMode.HOLD) {
                        if (mc.options.keySprint.isDown() || mc.player.isSprinting()) {
                            text = "[Sprinting (Holding)]";
                        }
                    }

                    if (text != null) {
                        graphics.text(mc.font, Component.literal(text), config.sprintHudX, config.sprintHudY, 0xFFFFFFFF, true);
                    }
                }
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            SmartSprintManager.tick(client);
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

            FreelookManager freelookManager = FreelookManager.getInstance();

            if (client.player == null || client.gui.screen() != null || client.player.isDeadOrDying()) {
                if (freelookManager.isFreelookActive()) {
                    freelookManager.stopFreelook();
                }
            } else {
                if (config.freelookEnabled) {
                    if (config.freelookMode == ModConfig.FreelookMode.HOLD) {
                        if (freelookKey.isDown()) {
                            if (!freelookManager.isFreelookActive()) {
                                freelookManager.startFreelook();
                            }
                        } else {
                            if (freelookManager.isFreelookActive()) {
                                freelookManager.stopFreelook();
                            }
                        }
                    } else if (config.freelookMode == ModConfig.FreelookMode.TOGGLE) {
                        while (freelookKey.consumeClick()) {
                            if (freelookManager.isFreelookActive()) {
                                freelookManager.stopFreelook();
                            } else {
                                freelookManager.startFreelook();
                            }
                        }
                    }
                } else if (freelookManager.isFreelookActive()) {
                    freelookManager.stopFreelook();
                }

                while (noHurtCamKey.consumeClick()) {
                    config.noHurtCamEnabled = !config.noHurtCamEnabled;
                    ConfigManager.save();
                    client.player.sendOverlayMessage(Component.literal("NoHurtCam: " + (config.noHurtCamEnabled ? "ON" : "OFF")));
                }

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
