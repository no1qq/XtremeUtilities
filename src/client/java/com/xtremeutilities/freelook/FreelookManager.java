package com.xtremeutilities.freelook;

import com.xtremeutilities.config.ModConfig;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;

public class FreelookManager {
    private static final FreelookManager INSTANCE = new FreelookManager();

    private boolean active = false;
    private CameraType originalPerspective = CameraType.FIRST_PERSON;
    private float cameraYaw = 0.0f;
    private float cameraPitch = 0.0f;

    public static FreelookManager getInstance() {
        return INSTANCE;
    }

    public void startFreelook() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        active = true;
        cameraYaw = mc.player.getYRot();
        cameraPitch = mc.player.getXRot();
        originalPerspective = mc.options.getCameraType();

        if (originalPerspective == CameraType.FIRST_PERSON) {
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
    }

    public void stopFreelook() {
        if (!active) {
            return;
        }

        active = false;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.options != null) {
            mc.options.setCameraType(originalPerspective);
        }
    }

    public void updateRotation(double deltaX, double deltaY) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        ModConfig config = ModConfig.getInstance();
        double sensitivity = mc.options.sensitivity().get() * 0.6 + 0.2;
        double multiplier = sensitivity * sensitivity * sensitivity * 8.0 * config.freelookSpeed;

        double dx = config.freelookInvertX ? -deltaX : deltaX;
        double dy = config.freelookInvertY ? -deltaY : deltaY;

        cameraYaw += (float) (dx * multiplier * 0.15);
        cameraPitch += (float) (dy * multiplier * 0.15);
        cameraPitch = Math.max(-90.0f, Math.min(90.0f, cameraPitch));
    }

    public boolean isFreelookActive() {
        return active;
    }

    public float getCameraYaw() {
        return cameraYaw;
    }

    public float getCameraPitch() {
        return cameraPitch;
    }
}
