package de.dafuqs.chalk.common.data;

import net.fabricmc.loader.api.FabricLoader;

public class ChalkCompatibilityData {
    public static boolean COLORFUL_ADDON;
    public static boolean CONTINUITY;
    static {
        COLORFUL_ADDON = (ChalkData.LOADER_TYPE == ChalkLoaderType.FABRIC || ChalkData.LOADER_TYPE == ChalkLoaderType.QUILT) && FabricLoader.getInstance().isModLoaded("chalk-colorful-addon");
        CONTINUITY = (ChalkData.LOADER_TYPE == ChalkLoaderType.FABRIC || ChalkData.LOADER_TYPE == ChalkLoaderType.QUILT) && FabricLoader.getInstance().isModLoaded("continuity");
    }
}