package net.armaments.api.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.Vec3d;

public final class APIPacketCodecs {
    public static final PacketCodec<ByteBuf, Vec3d> VEC3D_PACKET_CODEC = PacketCodec.of((value, output) -> {
        output.writeDouble(value.getX());
        output.writeDouble(value.getY());
        output.writeDouble(value.getZ());
    }, input -> new Vec3d(input.readDouble(), input.readDouble(), input.readDouble()));
}
