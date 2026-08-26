package dev.john220099.GambleMod;

import dev.john220099.GambleMod.module.ModuleManager;
import dev.john220099.GambleMod.utils.Logs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public class GambleMod implements ModInitializer, ClientModInitializer {

    public Minecraft mc;

    @Override
    public void onInitializeClient() {
        Logs.log.info("Loading GambleMod");
    }

    @Override
    public void onInitialize() {
        mc = Minecraft.getInstance();

        //noinspection ResultOfMethodCallIgnored
        ModuleManager.INSTANCE.getModules();

        ClientTickEvents.END_CLIENT_TICK.register(_ -> onTick());
    }

    private void onTick() {
        if (mc == null || mc.player == null || mc.level == null) {
            return;
        }

        ModuleManager.INSTANCE.onTick();
    }
}