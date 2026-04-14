package net.armaments.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class ModPlayPackets {
    public static void register() {
        PayloadTypeRegistry.playC2S().register(ShootC2SPacket.ID, ShootC2SPacket.PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ShootC2SPacket.ID, ShootC2SPacket::handle);
    }
}
