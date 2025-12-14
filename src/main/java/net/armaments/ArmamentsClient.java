package net.armaments;

import net.armaments.client.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;

public class ArmamentsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModModelPredicates.register();
    }
}
