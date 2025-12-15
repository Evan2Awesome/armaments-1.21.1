package net.armaments.network;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.armaments.Armaments;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public class ShootC2SPacket implements CustomPayload {
    public static final PacketCodec<ByteBuf, ShootC2SPacket> CODEC = PacketCodecs.codec(Codec.unit(ShootC2SPacket::new));

    public static final Id<ShootC2SPacket> ID = new Id<>(Armaments.id("packet_shoot"));

    @Override
    public Id<ShootC2SPacket> getId() {
        return ID;
    }
}
