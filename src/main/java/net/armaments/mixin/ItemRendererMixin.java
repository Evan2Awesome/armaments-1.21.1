package net.armaments.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.armaments.item.ModItems;
import net.armaments.item.custom.EchoGunItem;
import net.armaments.item.custom.SniperItem;
import net.armaments.util.Functions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow public abstract ItemModels getModels();

    @WrapMethod(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V")
    private void armaments$gun(ItemStack stack, ModelTransformationMode mode, boolean left, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, Operation<Void> original) {

        boolean two_dimensional = mode.equals(ModelTransformationMode.GUI) || mode.equals(ModelTransformationMode.GROUND) || mode.equals(ModelTransformationMode.FIXED);

        if (two_dimensional) {
            if (stack.isOf(ModItems.REVOLVER)) model = this.getModels().getModelManager().getModel(Functions.mId("revolver_2d"));
            if (stack.isOf(ModItems.SNIPER_RIFLE)) model = this.getModels().getModelManager().getModel(Functions.mId("cogwork_sniper_2d"));
            if (stack.isOf(ModItems.ECHO_GUN)) model = this.getModels().getModelManager().getModel(Functions.mId("echo_gun_2d"));
        }

        original.call(stack, mode, left, matrices, vertexConsumers, light, overlay, model);
    }

    @WrapOperation(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;"))
    private BakedModel armaments$gun(ItemRenderer renderer, ItemStack stack, World world, LivingEntity entity, int seed, Operation<BakedModel> original, @Local(argsOnly = true) ModelTransformationMode mode, @Local(argsOnly = true) boolean left, @Local(argsOnly = true) MatrixStack matrices) {
        if (stack.isOf(ModItems.REVOLVER) && entity.isUsingItem() && stack.equals(entity.getActiveItem())) {
            if (mode.isFirstPerson()) {
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(left ? 40F : -40F));
                matrices.translate(-0.1, Math.cos(world.getTime()) * 0.025,0);

                BakedModel model = this.getModels().getModelManager().getModel(Functions.mId("revolver_fp"));
                ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld)world : null;
                return model.getOverrides().apply(model, stack, clientWorld, entity, seed);
            } else if (mode.equals(ModelTransformationMode.THIRD_PERSON_LEFT_HAND) || mode.equals(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND)) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(world.getTime()*-200));
            }
        }

        return original.call(renderer, stack, world, entity, seed);
    }

    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V"))
    private void renderItemMixin(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo ci) {
        if (entity != null && (renderMode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND || renderMode == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND) && entity.isUsingItem() && (stack.getItem() instanceof SniperItem)) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(leftHanded ? -30F : 30F));
        }
        if (entity != null && (renderMode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND || renderMode == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND) && entity.isUsingItem() && (stack.getItem() instanceof EchoGunItem)) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(leftHanded ? -30F : 30F));
        }
    }
}