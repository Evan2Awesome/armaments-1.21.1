package net.armaments.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.armaments.Armaments;
import net.armaments.ArmamentsClient;
import net.armaments.item.component.ModDataComponents;
import net.armaments.item.custom.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin (InGameHud.class)
public class InGameHudMixin {
    @Unique
    private static final Identifier GUN_CROSSHAIR_TEXTURE = Armaments.id("hud/gun_crosshair");
    @Unique
    private static final Identifier ATTACK_INDICATOR_BULLET_FILL = Armaments.id("hud/bullet_attack_indicator_progress");
    @Unique
    private static final Identifier ATTACK_INDICATOR_BULLET_BACKGROUND = Armaments.id("hud/bullet_attack_indicator_background");
    @Unique
    private static final Identifier ATTACK_INDICATOR_BULLET_FULL = Armaments.id("hud/bullet_attack_indicator_full");

    @WrapOperation(method = "renderCrosshair",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0))
    private void armaments$renderCrosshair(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (MinecraftClient.getInstance().player instanceof PlayerEntity player && player.getMainHandStack().getItem() instanceof GunItem gunItem) {
            original.call(instance, GUN_CROSSHAIR_TEXTURE, x, y, width, height);
            ArmamentsClient.renderRelativeToCrosshair(instance, player.getMainHandStack().getOrDefault(ModDataComponents.AMMO,0), gunItem.getMaxAmmo(player.getMainHandStack()), x - 1, y + 26, 0xFFFFFF);
        } else original.call(instance, texture, x, y, width, height);
    }

    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 2))
    private void armaments$overrideCrosshairAttackBarBackground(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (MinecraftClient.getInstance().player instanceof PlayerEntity player && player.getMainHandStack().getItem() instanceof GunItem gun && gun.getAmmo(player.getMainHandStack()) > 0) {
            original.call(instance, ATTACK_INDICATOR_BULLET_BACKGROUND, x, y, width, height);
        } else original.call(instance, texture, x, y, width, height);
    }

    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1))
    private void armaments$overrideCrosshairAttackBarFull(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (MinecraftClient.getInstance().player instanceof PlayerEntity player && player.getMainHandStack().getItem() instanceof GunItem gun && gun.getAmmo(player.getMainHandStack()) > 0) {
            original.call(instance, ATTACK_INDICATOR_BULLET_FULL, x, y, width, height);
        } else original.call(instance, texture, x, y, width, height);
    }

    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V", ordinal = 0))
    private void armaments$overrideCrosshairAttackBarFill(DrawContext instance, Identifier texture, int i, int j, int k, int l, int x, int y, int width, int height, Operation<Void> original) {
        if (MinecraftClient.getInstance().player instanceof PlayerEntity player && player.getMainHandStack().getItem() instanceof GunItem gun && gun.getAmmo(player.getMainHandStack()) > 0) {
            original.call(instance, ATTACK_INDICATOR_BULLET_FILL, i, j, k, l, x, y, width, height);
        } else original.call(instance, texture, i, j, k, l, x, y, width, height);
    }
}