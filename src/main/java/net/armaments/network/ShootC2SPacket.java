package net.armaments.network;

import io.netty.buffer.ByteBuf;
import net.armaments.Armaments;
import net.armaments.item.custom.GunItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Hand;

public record ShootC2SPacket(boolean mainhand) implements CustomPayload {
    public static final Id<ShootC2SPacket> ID = new Id<>(Armaments.id("packet_shoot"));
    public static final PacketCodec<ByteBuf, ShootC2SPacket> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, ShootC2SPacket::mainhand,
            ShootC2SPacket::new
    );

    @Override
    public Id<ShootC2SPacket> getId() {
        return ID;
    }

    public static void handle(ShootC2SPacket packet, ServerPlayNetworking.Context context) {
        ItemStack stack = context.player().getStackInHand(packet.mainhand() ? Hand.MAIN_HAND : Hand.OFF_HAND);
        if (stack.getItem() instanceof GunItem gun) gun.tryShoot(context.player(), stack);
    }
}
