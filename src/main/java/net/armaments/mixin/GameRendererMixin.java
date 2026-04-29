package net.armaments.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.armaments.Armaments;
import net.armaments.item.custom.GunItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    public abstract MinecraftClient getClient();

    @Shadow
    public abstract Camera getCamera();

    @Shadow
    @Final
    private Camera camera;
    @Shadow
    @Final
    private MinecraftClient client;
    @Unique private static final double ZOOM_OUT_TARGET = 1.0;
    @Unique private static final double ZOOM_SPEED = 0.005; // lower = slower

    @Unique private double zoomFactor = 1.0;

    @ModifyReturnValue(
            method = "getFov",
            at = @At("RETURN")
    )
    private double zoomItem$applySmoothZoom(double originalFov, Camera camera, float tickDelta) {
        double target;
        Entity entity = camera.getFocusedEntity();
        boolean zooming = false;
        if (entity instanceof PlayerEntity player && player.getActiveItem().getItem() instanceof GunItem gunItem){
            if (player.isUsingItem() && gunItem.canADS(player.getActiveItem(), player)) {
                zooming = true;
            }

            target = zooming ? gunItem.get_ads_zoom(player.getActiveItem()) : ZOOM_OUT_TARGET;
        }else{
            target = ZOOM_OUT_TARGET;
        }
        zoomFactor += (target - zoomFactor) * (1.0 - Math.pow(1.0 - ZOOM_SPEED, tickDelta * 20.0));
        return originalFov * zoomFactor;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void overrideTick(CallbackInfo ci) {
        if (Armaments.isDoom()) {Objects.requireNonNull(getClient().player).setPitch(0f);}
    }
}