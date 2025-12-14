package net.armaments.mixin;

import net.armaments.item.custom.PistolItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.MinecartItem;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.armaments.item.ModItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> implements ModelWithArms, ModelWithHead {
    @Shadow
    @Mutable
    @Final
    public ModelPart rightArm;
    @Shadow
    @Mutable
    @Final
    public ModelPart leftArm;
    @Shadow
    @Mutable
    @Final
    public ModelPart head;

    @Shadow public abstract void animateModel(T livingEntity, float f, float g, float h);

    @Shadow protected abstract void animateArms(T entity, float animationProgress);

    @Inject(method = "setAngles*", at = @At(value = "TAIL"))
    private void setAnglesMixin(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (livingEntity.getMainHandStack().getItem() instanceof PistolItem){
            if (livingEntity.getMainArm().equals(Arm.RIGHT)) {
                this.rightArm.yaw = -0.1F + this.head.yaw;
                this.rightArm.pitch = (float) (-Math.PI / 2) + this.head.pitch;
            }else {
                this.leftArm.yaw = 0.1F + this.head.yaw;
                this.leftArm.pitch = (float) (-Math.PI / 2) + this.head.pitch;
            }
            if (livingEntity.getOffHandStack().getItem() instanceof PistolItem){
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
}

