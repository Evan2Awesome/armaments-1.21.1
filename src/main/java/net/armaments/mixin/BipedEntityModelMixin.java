package net.armaments.mixin;

import net.armaments.util.ModTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> implements ModelWithArms, ModelWithHead {
    @Shadow public abstract void animateModel(T livingEntity, float f, float g, float h);

    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart head;

    @Inject(method = "setAngles*", at = @At(value = "TAIL"))
    private void setAnglesMixin(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (livingEntity.getMainHandStack().isIn(ModTags.Items.ONE_HANDED_GUN)) {
            if (livingEntity.getMainArm().equals(Arm.RIGHT)) {
                this.rightArm.yaw = -0.1F + this.head.yaw;
                this.rightArm.pitch = (float) (-Math.PI / 2) + this.head.pitch;
            }else {
                this.leftArm.yaw = 0.1F + this.head.yaw;
                this.leftArm.pitch = (float) (-Math.PI / 2) + this.head.pitch;
            }
        }
        if (livingEntity.getOffHandStack().isIn(ModTags.Items.ONE_HANDED_GUN)){
            if (livingEntity.getMainArm().equals(Arm.RIGHT)) {
                this.leftArm.yaw = 0.1F + this.head.yaw;
                this.leftArm.pitch = (float) (-Math.PI / 2) + this.head.pitch;
            }else {
                this.rightArm.yaw = -0.1F + this.head.yaw;
                this.rightArm.pitch = (float) (-Math.PI / 2) + this.head.pitch;
            }
        }
    }
}

