package net.armaments.network;

import io.netty.buffer.ByteBuf;
import net.armaments.Armaments;
import net.armaments.item.custom.GunItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record ShootC2SPacket() implements CustomPayload {
    public static final Id<ShootC2SPacket> ID = new Id<>(Armaments.id("shoot"));
    public static final ShootC2SPacket INSTANCE = new ShootC2SPacket();
    public static final PacketCodec<ByteBuf, ShootC2SPacket> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public Id<ShootC2SPacket> getId() {
        return ID;
    }

    public static void handle(ShootC2SPacket packet, ServerPlayNetworking.Context context) {
        ItemStack stack = context.player().getMainHandStack();
        if (stack.getItem() instanceof GunItem gun) gun.tryShoot(context.player(), stack);
    }
}
