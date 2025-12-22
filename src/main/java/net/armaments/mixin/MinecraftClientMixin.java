package net.armaments.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.armaments.item.ModItems;
import net.armaments.item.custom.GunItem;
import net.armaments.network.ShootC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow @Final public GameOptions options;

    @WrapMethod(method = "doAttack")
    private boolean armaments$overrideAttack(Operation<Boolean> original) {
        if (this.player != null && this.player.getMainHandStack().getItem() instanceof GunItem gun) {
            gun.shoot(this.player, this.player.getMainHandStack());
            ClientPlayNetworking.send(new ShootC2SPacket(true));
            return true;
        } else return original.call();
    }

    @WrapOperation(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;handleBlockBreaking(Z)V"))
    private void armaments$overrideInputEvents(MinecraftClient client, boolean breaking, Operation<Void> original) {
        original.call(client, breaking && this.player != null && !(this.player.getMainHandStack().getItem() instanceof GunItem));
    }

    @WrapOperation(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean armaments$customShooting(ClientPlayerEntity player, Operation<Boolean> original) {
        ItemStack stack = player.getActiveItem();
        if (stack.isOf(ModItems.SNIPER_RIFLE) && stack.getItem() instanceof GunItem gun && this.options.attackKey.wasPressed()) {
            gun.shoot(player, stack);
            ClientPlayNetworking.send(new ShootC2SPacket(player.getMainHandStack().equals(stack)));
        }
        return original.call(player);
    }
}