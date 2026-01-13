package net.armaments.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.armaments.Armaments;
import net.armaments.ArmamentsClient;
import net.armaments.item.component.ModDataComponents;
import net.armaments.item.custom.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin (InGameHud.class)
public class InGameHudMixin {
    @Unique
    private static final Identifier GUN_CROSSHAIR_TEXTURE = Armaments.id("hud/gun_crosshair");

    @WrapOperation(method = "renderCrosshair",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0))
    private void armaments$renderCrosshair(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (MinecraftClient.getInstance().player instanceof PlayerEntity player && player.getMainHandStack().getItem() instanceof GunItem gunItem) {
            original.call(instance, GUN_CROSSHAIR_TEXTURE, x, y, width, height);
            ArmamentsClient.renderRelativeToCrosshair(instance, player.getMainHandStack().getOrDefault(ModDataComponents.AMMO,0), gunItem.getMaxAmmo(player.getMainHandStack()), x - 1, y + 24, 0xFFFFFF);
        } else original.call(instance, texture, x, y, width, height);
    }
}
