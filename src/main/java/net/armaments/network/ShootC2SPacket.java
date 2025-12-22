package net.armaments.network;

import io.netty.buffer.ByteBuf;
import net.armaments.Armaments;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record ShootC2SPacket(boolean mainhand) implements CustomPayload {
    public static final PacketCodec<ByteBuf, ShootC2SPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL,
            ShootC2SPacket::mainhand,
            ShootC2SPacket::new
    );

    public static final Id<ShootC2SPacket> ID = new Id<>(Armaments.id("packet_shoot"));

    @Override
    public Id<ShootC2SPacket> getId() {
        return ID;
    }
}
