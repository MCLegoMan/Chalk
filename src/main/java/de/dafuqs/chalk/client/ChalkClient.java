package de.dafuqs.chalk.client;

import de.dafuqs.chalk.client.config.ChalkConfigHelper;
import de.dafuqs.chalk.common.Chalk;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
public class ChalkClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ChalkConfigHelper.init();
        for (Chalk.ChalkVariant chalkVariant : Chalk.chalkVariants.values()) {
            chalkVariant.registerClient();
        }
        tick();
    }
    private static void tick() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ChalkConfigHelper.tick(client);
        });
    }
}
