package net.armaments;

import net.armaments.client.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;

public class ArmamentsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModModelPredicates.register();
    }
}
