package net.armaments.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.armaments.Armaments;
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

    @WrapOperation(method = "renderCrosshair",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0))
    private void armaments$renderCrosshair(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (MinecraftClient.getInstance().player instanceof PlayerEntity player && player.getMainHandStack().getItem() instanceof GunItem) {
            original.call(instance, GUN_CROSSHAIR_TEXTURE, x, y, width, height);
        }else original.call(instance, texture, x, y, width, height);
    }
}
