package com.xtremeutilities.config;

public class ModConfig {
    private static ModConfig instance = new ModConfig();

    public boolean fullbright = false;
    public SprintMode sprintMode = SprintMode.TOGGLE;
    public boolean stickySprintActive = false;
    public boolean sprintToggled = true;
    public boolean darkLoadingScreen = true;
    public boolean fadelessReload = true;

    public enum SprintMode {
        TOGGLE,
        HOLD
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
