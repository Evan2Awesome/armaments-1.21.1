package net.armaments.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.armaments.item.ModDataComponents;
import net.armaments.item.custom.PistolItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import net.armaments.item.ModItems;
import net.armaments.Armaments;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    @Final
    private ItemModels models;

    @Shadow
    public abstract ItemModels getModels();

    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    public BakedModel renderItem(BakedModel bakedModel, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) ModelTransformationMode renderMode) {
        //if (stack.getItem() == ModItems.EXAMPLE && (renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED)) {
        //    return getModels().getModelManager().getModel(ModelIdentifier.ofInventoryVariant(ExampleMod.id("example")));
        //}

        if (stack.getItem() == ModItems.REVOLVER && (renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED)) {
            return getModels().getModelManager().getModel(ModelIdentifier.ofInventoryVariant(Armaments.id("revolver")));
        }
        if (stack.getOrDefault(ModDataComponents.USE_COMPONENT, false) && stack.getItem() instanceof PistolItem && (renderMode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND || renderMode == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND)) {
            return getModels().getModelManager().getModel(ModelIdentifier.ofInventoryVariant(Armaments.id("revolver_spin_2")));
        }
        if (stack.getOrDefault(ModDataComponents.USE_COMPONENT, false) && stack.getItem() instanceof PistolItem && (renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND || renderMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND)) {
            return getModels().getModelManager().getModel(ModelIdentifier.ofInventoryVariant(Armaments.id("revolver_spin")));
        }

        return bakedModel;
    }

    @ModifyVariable(
            method = "getModel",
            at = @At(value = "STORE"),
            ordinal = 1
    )
    public BakedModel getHeldItemModelMixin(BakedModel bakedModel, @Local(argsOnly = true) ItemStack stack) {
        if (stack.getItem() == ModItems.REVOLVER) {
            return this.models.getModelManager().getModel(ModelIdentifier.ofInventoryVariant(Armaments.id("revolver_3d")));
        }

        return bakedModel;
    }

    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V"))
    private void renderItemMixin(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo ci) {
        if (entity != null && (renderMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND) && entity.isUsingItem() && (stack.getItem() instanceof PistolItem)) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(world.getTime()*-200));
        }
        if (entity != null && (renderMode == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND || renderMode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND) && entity.isUsingItem() && (stack.getItem() instanceof PistolItem)) {
            matrices.translate(0, Math.cos(world.getTime()) * 0.02,0);
        }
    }
}