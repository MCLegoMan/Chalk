package de.dafuqs.chalk.client.config;

import de.dafuqs.chalk.common.data.ChalkData;
import de.dafuqs.chalk.common.data.ChalkLoggerType;
import net.minecraft.client.MinecraftClient;

public class ChalkConfigHelper {
    protected static boolean SAVE_VIA_TICK;
    protected static int SAVE_VIA_TICK_TICKS;
    protected static final int SAVE_VIA_TICK_SAVE_TICK = 20;
    public static void init() {
        ChalkConfig.init();
        updateConfig();
    }
    public static void tick(MinecraftClient client) {
        if (SAVE_VIA_TICK_TICKS < SAVE_VIA_TICK_SAVE_TICK) SAVE_VIA_TICK_TICKS += 1;
        else {
            if (SAVE_VIA_TICK) {
                saveConfig(false);
                SAVE_VIA_TICK = false;
            }
            SAVE_VIA_TICK_TICKS = 0;
        }
    }
    protected static void updateConfig() {
        if ((int)getConfig("config_version") != ChalkConfigDefaults.CONFIG_VERSION) {
            ChalkData.sendToLog("Updating config to the latest version.", ChalkLoggerType.INFO);

            /*
            * If any config options values change how they are used, update them here.
            * example: if ((int)getConfig("config_version") < 2) { setConfig("configOption", (int)getConfig("configOption") / 20) }
            */

            setConfig("config_version", ChalkConfigDefaults.CONFIG_VERSION);
            saveConfig(false);
        }
    }
    public static void saveConfig(boolean onTick) {
        if (onTick) {
            SAVE_VIA_TICK = true;
        } else {
            ChalkData.sendToLog("Writing config to file.", ChalkLoggerType.INFO);
            ChalkConfig.save();
            ChalkConfig.CONFIG_PROVIDER.saveConfig(ChalkConfig.ID);
        }
    }
    public static void resetConfig() {
        setConfig("emit_particles", ChalkConfigDefaults.EMIT_PARTICLES);
        setConfig("config_version", ChalkConfigDefaults.CONFIG_VERSION);
    }
    public static void setConfig(String ID, Object VALUE) {
        switch (ID) {
            case "emit_particles" -> ChalkConfig.EMIT_PARTICLES = (boolean)VALUE;
            case "config_version" -> ChalkConfig.CONFIG_VERSION = (int)VALUE;
        }
    }
    public static Object getConfig(String ID) {
        switch (ID) {
            case "emit_particles" -> {return ChalkConfig.EMIT_PARTICLES;}
            case "config_version" -> {return ChalkConfig.CONFIG_VERSION;}
        }
        return new Object();
    }
}