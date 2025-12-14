package net.armaments.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.armaments.item.custom.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientPlayerEntity player;

    @WrapMethod(method = "doAttack")
    private boolean armaments$overrideAttack(Operation<Boolean> original) {
        if (this.player instanceof ClientPlayerEntity player && player.getMainHandStack().getItem() instanceof GunItem gun) {
            gun.shoot(player, player.getMainHandStack());
            return false;
        }
        return original.call();
    }

    @WrapOperation(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;handleBlockBreaking(Z)V", ordinal = 0))
    private void armaments$overrideInputEvents(MinecraftClient instance, boolean breaking, Operation<Void> original) {
        if (this.player instanceof ClientPlayerEntity player && player.getMainHandStack().getItem() instanceof GunItem) {
            original.call(instance, false);
        }else original.call(instance, breaking);
    }
}