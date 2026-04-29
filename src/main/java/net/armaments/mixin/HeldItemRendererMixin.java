package net.armaments.mixin;


import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.armaments.item.custom.GunItem;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin (HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow @Nullable
    private ItemStack mainHand;

    @WrapMethod(method = "applyEquipOffset")
    public void armaments$overrideEquipOffset(MatrixStack matrices, Arm arm, float equipProgress, Operation<Void> original) {
        if (mainHand.getItem() instanceof GunItem gunItem) {
            int i = arm == Arm.RIGHT ? 1 : -1;
            float kickback = gunItem.getKickback(mainHand);
            //19.1548 x - 97.7262 x^2 + 148.81 x^3 - 70.2381 x^4
            //float interpolatedProgress = (float)(-(70.2381*Math.pow(equipProgress,4)) + (148.81*Math.pow(equipProgress,3)) - (97.7262*Math.pow(equipProgress,2)) + (19.1548*equipProgress))/;
            //if(interpolatedProgress != 0) System.out.println(interpolatedProgress);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((75F * equipProgress * kickback)));
            matrices.translate(i * 0.56F, -0.52F + ((equipProgress * -0.2) * kickback), -0.72F + ((equipProgress * 0.7) * kickback));
        } else {
            original.call(matrices, arm, equipProgress);
        }
    }
}
