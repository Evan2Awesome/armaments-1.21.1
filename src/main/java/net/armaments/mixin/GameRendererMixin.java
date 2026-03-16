package net.armaments.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.armaments.item.custom.EchoGunItem;
import net.armaments.item.custom.GunItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    private static final double ZOOM_IN_TARGET = 0.5; // 4× zoom
    private static final double ZOOM_OUT_TARGET = 1.0;
    private static final double ZOOM_SPEED = 0.005; // lower = slower

    private double zoomFactor = 1.0;

    @ModifyReturnValue(
            method = "getFov",
            at = @At("RETURN")
    )
    private double zoomItem$applySmoothZoom(double originalFov, Camera camera, float tickDelta) {
        Entity entity = camera.getFocusedEntity();

        boolean zooming = false;

        if (entity instanceof PlayerEntity player && player.getActiveItem().getItem() instanceof GunItem gunItem) {
            zooming = player.isUsingItem() && gunItem.canADS(player.getActiveItem(), player);
        }

        double target = zooming ? ZOOM_IN_TARGET : ZOOM_OUT_TARGET;

        // Frame-rate independent interpolation
        zoomFactor += (target - zoomFactor) * (1.0 - Math.pow(1.0 - ZOOM_SPEED, tickDelta * 20.0));

        return originalFov * zoomFactor;
    }
}