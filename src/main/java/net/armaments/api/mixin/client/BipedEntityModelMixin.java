package net.armaments.api.mixin.client;

import net.armaments.api.client.event.EntityAnglesEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> implements ModelWithArms, ModelWithHead {
    @Inject(method = "setAngles*", at = @At(value = "TAIL")) @SuppressWarnings("unchecked")
    private void armaments_api$setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        BipedEntityModel<LivingEntity> model = (BipedEntityModel<LivingEntity>)(Object)this;
        EntityAnglesEvent.BIPED_ANGLES.invoker().setAngles(livingEntity, model, f, g, h, i, j);
    }
}

