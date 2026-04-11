package net.armaments.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
@Environment(EnvType.CLIENT)
public class CameraMixin {

    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    public void armaments$doom_camera(Camera instance, float yaw, float pitch, Operation<Void> original) {
        original.call(instance, yaw, 0f);
    }
}
