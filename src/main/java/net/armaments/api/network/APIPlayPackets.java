package net.armaments.api.network;

import net.armaments.api.network.s2c.MultiParticleS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class APIPlayPackets {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(MultiParticleS2CPacket.ID, MultiParticleS2CPacket.PACKET_CODEC);
        ClientPlayNetworking.registerGlobalReceiver(MultiParticleS2CPacket.ID, MultiParticleS2CPacket::handle);
    }
}
