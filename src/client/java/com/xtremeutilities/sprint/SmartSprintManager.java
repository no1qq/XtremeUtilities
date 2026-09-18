package com.xtremeutilities.sprint;

import com.mojang.blaze3d.platform.InputConstants;
import com.xtremeutilities.config.ModConfig;
import com.xtremeutilities.mixin.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SmartSprintManager {
    private static boolean smartSprintActive = false;

    public static boolean isSmartSprintActive() {
        return smartSprintActive;
    }

    public static void tick(Minecraft mc) {
        if (mc == null || mc.player == null || mc.level == null || mc.options == null) {
            smartSprintActive = false;
            return;
        }

        ModConfig config = ModConfig.getInstance();
        if (!config.smartSprint || mc.player.isDeadOrDying() || mc.gui.screen() != null) {
            if (smartSprintActive) {
                restoreForwardKey(mc);
            }
            return;
        }

        LocalPlayer player = mc.player;
        boolean inAirFalling = !player.onGround() && !player.onClimbable() && !player.isInWater() && player.getDeltaMovement().y < 0.0;

        if (inAirFalling && isTargetPlayerInFovAndReach(mc)) {
            smartSprintActive = true;
            mc.options.keyUp.setDown(false);
        } else if (smartSprintActive) {
            restoreForwardKey(mc);
        }
    }

    private static boolean isTargetPlayerInFovAndReach(Minecraft mc) {
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) {
            return false;
        }

        if (mc.crosshairPickEntity instanceof Player target && target != player && target.isAlive() && !target.isSpectator()) {
            return true;
        }

        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getViewVector(1.0f);

        for (AbstractClientPlayer other : mc.level.players()) {
            if (other == player || !other.isAlive() || other.isSpectator()) {
                continue;
            }

            if (player.isWithinEntityInteractionRange(other, 0.0)) {
                Vec3 dirToTarget = other.getEyePosition().subtract(eyePos).normalize();
                if (lookVec.dot(dirToTarget) > 0.5 && player.hasLineOfSight(other)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static void restoreForwardKey(Minecraft mc) {
        smartSprintActive = false;
        KeyMapping keyUp = mc.options.keyUp;
        if (keyUp != null) {
            InputConstants.Key key = ((KeyMappingAccessor) keyUp).getKey();
            if (key != null && key.getType() == InputConstants.Type.KEYBOARD) {
                if (InputConstants.isKeyDown(key.getValue())) {
                    keyUp.setDown(true);
                } else {
                    keyUp.setDown(false);
                }
            }
        }
    }
}
