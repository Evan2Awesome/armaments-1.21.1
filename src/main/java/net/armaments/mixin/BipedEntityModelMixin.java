package net.armaments.mixin;

import net.armaments.ArmamentsClient;
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
    private void setAnglesMixin(T entity, float f, float g, float h, float i, float j, CallbackInfo info) {
        BipedEntityModel<LivingEntity> model = (BipedEntityModel<LivingEntity>)(Object) this;
        float tickDelta = h - entity.age;
        ArmamentsClient.ANGLES_MAP.forEach((item, consumer) -> {
            if (entity.getMainHandStack().isOf(item) || entity.getOffHandStack().isOf(item)) consumer.accept(entity, model, tickDelta);
        });
        ArmamentsClient.TAG_ANGLES_MAP.forEach((tag, consumer) -> {
            if (entity.getMainHandStack().isIn(tag) || entity.getOffHandStack().isIn(tag)) consumer.accept(entity, model, tickDelta);
        });
    }
}

