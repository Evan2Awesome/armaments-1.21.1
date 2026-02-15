package net.armaments.mixin;


import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.armaments.item.custom.EchoGunItem;
import net.armaments.item.custom.GunItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin (HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow @Nullable
    private ItemStack mainHand;

    @WrapMethod(method = "applyEquipOffset")
    public void armaments$overrideEquipOffset(MatrixStack matrices, Arm arm, float equipProgress, Operation<Void> original) {
        if (mainHand.getItem() instanceof GunItem gunItem) {
            int i = arm == Arm.RIGHT ? 1 : -1;
            float kickback = gunItem.getKickback(mainHand);

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((75F * equipProgress) * kickback));
            matrices.translate(i * 0.56F, -0.52F + ((equipProgress * -0.2) * kickback), -0.72F + ((equipProgress * 0.7) * kickback));
        } else {
            original.call(matrices, arm, equipProgress);
        }
    }
}
