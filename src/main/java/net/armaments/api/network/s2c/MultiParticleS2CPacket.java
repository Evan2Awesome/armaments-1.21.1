package net.armaments.api.network.s2c;

import net.armaments.api.API;
import net.armaments.api.network.APIPacketCodecs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.List;

public record MultiParticleS2CPacket<T extends ParticleEffect>(
        T particle,
        boolean longDistance,
        List<Vec3d> points,
        Vector3f distance,
        float speed,
        int count
) implements CustomPayload {
    private static final Random RANDOM = Random.create();

    public static final Id<MultiParticleS2CPacket<?>> ID = new Id<>(API.id("multiparticle"));
    public static final PacketCodec<RegistryByteBuf, MultiParticleS2CPacket<?>> PACKET_CODEC = PacketCodec.tuple(
            ParticleTypes.PACKET_CODEC, MultiParticleS2CPacket::particle,
            PacketCodecs.BOOL, MultiParticleS2CPacket::longDistance,
            APIPacketCodecs.VEC3D_PACKET_CODEC.collect(PacketCodecs.toList()), MultiParticleS2CPacket::points,
            PacketCodecs.VECTOR3F, MultiParticleS2CPacket::distance,
            PacketCodecs.FLOAT, MultiParticleS2CPacket::speed,
            PacketCodecs.INTEGER, MultiParticleS2CPacket::count,
            MultiParticleS2CPacket::new
    );

    @Override
    public Id<MultiParticleS2CPacket<?>> getId() {
        return ID;
    }

    public static <T extends ParticleEffect> void handle(MultiParticleS2CPacket<T> packet, ClientPlayNetworking.Context context) {
        if (packet.count() > 0) {
            Vector3f distance = packet.distance();
            float speed = packet.speed();
            World level = context.player().getWorld();

            for (Vec3d point : packet.points()) {
                double xVariance = RANDOM.nextGaussian() * distance.x();
                double yVariance = RANDOM.nextGaussian() * distance.y();
                double zVariance = RANDOM.nextGaussian() * distance.z();
                double xa = RANDOM.nextGaussian() * speed;
                double ya = RANDOM.nextGaussian() * speed;
                double za = RANDOM.nextGaussian() * speed;

                try {
                    level.addParticle(
                            packet.particle(), packet.longDistance(),
                            point.getX() + xVariance, point.getY() + yVariance, point.getZ() + zVariance,
                            xa, ya, za
                    );
                } catch (Throwable ignore) {}
            }
        }
    }

    public static <T extends ParticleEffect> void sendParticles(
            ServerWorld world, T particle,
            boolean longDistance,
            List<Vec3d> points, Vector3f distance,
            float speed, int count
    ){
        for (ServerPlayerEntity player : world.getPlayers()) {
            if (!player.getWorld().equals(world)) continue;
            BlockPos playerPos = player.getBlockPos();
            List<Vec3d> filteredPoints = points.stream().filter(point -> playerPos.isWithinDistance(point, longDistance ? 512d : 32d)).toList();
            if (filteredPoints.isEmpty()) continue;
            ServerPlayNetworking.send(player, new MultiParticleS2CPacket<>(particle, longDistance, filteredPoints, distance, speed, count));
        }
    }
}
