package net.armaments.api;

import net.armaments.Armaments;
import net.armaments.api.network.APIPlayPackets;
import net.armaments.api.world.TickingObjects;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public final class API implements ModInitializer {
    public static final String MOD_ID = Armaments.MOD_ID + "_api";

    @Override
    public void onInitialize() {
        APIPlayPackets.registerS2C();
        TickingObjects.register();
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
