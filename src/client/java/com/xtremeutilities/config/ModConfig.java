package com.xtremeutilities.config;

public class ModConfig {
    private static ModConfig instance = new ModConfig();

    public boolean fullbright = false;
    public SprintMode sprintMode = SprintMode.TOGGLE;
    public boolean sprintToggled = true;
    public boolean sprintHudEnabled = false;
    public boolean sprintNotification = true;
    public int sprintHudX = 4;
    public int sprintHudY = 4;
    public boolean smartSprint = false;
    public boolean darkLoadingScreen = true;
    public boolean fadelessReload = true;

    public boolean freelookEnabled = true;
    public FreelookMode freelookMode = FreelookMode.HOLD;
    public double freelookSpeed = 1.0;
    public boolean freelookInvertX = false;
    public boolean freelookInvertY = false;

    public boolean noHurtCamEnabled = true;
    public double hurtCamShake = 0.0;

    public enum SprintMode {
        TOGGLE,
        HOLD
    }

    public enum FreelookMode {
        HOLD,
        TOGGLE
    }

    public static ModConfig getInstance() {
        if (instance == null) {
            instance = new ModConfig();
        }
        return instance;
    }

    public static void setInstance(ModConfig newInstance) {
        if (newInstance != null) {
            instance = newInstance;
        }
    }
}
