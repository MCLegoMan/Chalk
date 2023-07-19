package de.dafuqs.chalk.common.data;

import com.mojang.logging.LogUtils;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.util.Random;

public class ChalkData {
    public static final String NAME;
    public static final String ID;
    public static final ChalkLoaderType LOADER_TYPE;
    public static final Logger LOGGER;
    public static String PREFIX;
    public static Random RANDOM;
    private static ChalkLoaderType getLoaderType() {
        if (FabricLoader.getInstance().getModContainer("quilt_loader").isPresent()) return ChalkLoaderType.QUILT;
        else if (FabricLoader.getInstance().getModContainer("fabricloader").isPresent()) return ChalkLoaderType.FABRIC;
        else return ChalkLoaderType.OTHER;
    }
    static {
        NAME = "Chalk";
        ID = "chalk";
        LOADER_TYPE = getLoaderType();
        LOGGER = LogUtils.getLogger();
        PREFIX = "[" + NAME + "] ";
        RANDOM = new Random();
    }
    public static void sendToLog(String message, ChalkLoggerType type) {
        if (type.equals(ChalkLoggerType.INFO)) ChalkData.LOGGER.info(ChalkData.PREFIX + message);
        if (type.equals(ChalkLoggerType.WARN)) ChalkData.LOGGER.warn(ChalkData.PREFIX + message);
        if (type.equals(ChalkLoggerType.ERROR)) ChalkData.LOGGER.error(ChalkData.PREFIX + message);
    }
}