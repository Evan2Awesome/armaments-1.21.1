package net.armaments.api;

import net.armaments.api.network.APIPlayPackets;
import net.fabricmc.api.ClientModInitializer;

public final class APIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        APIPlayPackets.registerC2S();
    }
}
