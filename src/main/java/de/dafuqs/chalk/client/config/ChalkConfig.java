package de.dafuqs.chalk.client.config;

import com.mclegoman.simplefabric.fabric_simplelibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;
import de.dafuqs.chalk.common.data.ChalkData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ChalkConfig {
    protected static final String ID = ChalkData.ID;
    protected static SimpleConfig CONFIG;
    protected static ChalkConfigProvider CONFIG_PROVIDER;
    protected static boolean EMIT_PARTICLES;
    protected static int CONFIG_VERSION;
    protected static void init() {
        CONFIG_PROVIDER = new ChalkConfigProvider();
        create();
        CONFIG = SimpleConfig.of(ID).provider(CONFIG_PROVIDER).request();
        assign();
    }
    protected static void create() {
        CONFIG_PROVIDER.add(ID, new Pair<>("emit_particles", ChalkConfigDefaults.EMIT_PARTICLES));
        CONFIG_PROVIDER.add(ID, new Pair<>("config_version", ChalkConfigDefaults.CONFIG_VERSION));
    }
    protected static void assign() {
        EMIT_PARTICLES = CONFIG.getOrDefault("emit_particles", ChalkConfigDefaults.EMIT_PARTICLES);
        CONFIG_VERSION = CONFIG.getOrDefault("config_version", ChalkConfigDefaults.CONFIG_VERSION);
    }
    protected static void save() {
        CONFIG_PROVIDER.setConfig("emit_particles", EMIT_PARTICLES);
        CONFIG_PROVIDER.setConfig("config_version", CONFIG_VERSION);
    }
}