package net.armaments.client;

import net.armaments.item.custom.GunItem;
import net.armaments.network.ShootC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.item.ItemStack;

public class ModAttackHandlers {
    public static void register() {
        ClientPreAttackCallback.EVENT.register((client, player, clickCount) -> {
            if (player.isSpectator()) return false;
            ItemStack stack = player.getMainHandStack();
            if (!(stack.getItem() instanceof GunItem gunItem)) return false;
            if (gunItem.getAmmo(stack) < 1) return false;
            if ((!gunItem.canUseAndShoot(stack, player) && player.isUsingItem()) ||
                (clickCount == 0 && !gunItem.fullyAutomatic(stack, player)) ||
                !gunItem.canShoot(player, stack)) return true;

            gunItem.tryShoot(player, stack);
            player.resetLastAttackedTicks();
            ClientPlayNetworking.send(ShootC2SPacket.INSTANCE);
            return true;
        });
    }
}