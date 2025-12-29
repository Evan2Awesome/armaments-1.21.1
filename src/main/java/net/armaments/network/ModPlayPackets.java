package net.armaments.network;

import net.armaments.item.custom.GunItem;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ModPlayPackets {
    public static void registerC2S() {
        PayloadTypeRegistry.playC2S().register(ShootC2SPacket.ID, ShootC2SPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(ShootC2SPacket.ID, ShootC2SPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ShootC2SPacket.ID, (packet, context) -> {
            ItemStack stack = context.player().getStackInHand(packet.mainhand() ? Hand.MAIN_HAND : Hand.OFF_HAND);
            if (stack.getItem() instanceof GunItem gun) gun.tryShoot(context.player(), stack);
        });
    }
}
