package net.armaments.network;

import net.armaments.item.custom.GunItem;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModPlayPackets {
    public static void registerC2S() {
        PayloadTypeRegistry.playC2S().register(ShootC2SPacket.ID, ShootC2SPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(ShootC2SPacket.ID, ShootC2SPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ShootC2SPacket.ID, (shootC2SPacket, context) -> {
            if (context.player().getMainHandStack().getItem() instanceof GunItem gun) gun.shoot(context.player(), context.player().getMainHandStack());
        });
    }
}
