package net.armaments.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.armaments.item.custom.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public ClientPlayerEntity player;

    @WrapMethod(method = "doAttack")
    private boolean armaments$overrideAttack(Operation<Boolean> original) {
        if (this.player instanceof ClientPlayerEntity player && player.getMainHandStack().getItem() instanceof GunItem gun) {
            gun.shoot(player, player.getMainHandStack());
            return false;
        }
        return original.call();
    }
}