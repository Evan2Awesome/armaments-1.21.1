package net.armaments.client;

import net.armaments.ArmamentsClient;
import net.armaments.item.ModItems;
import net.armaments.util.ModTags;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

public class ModAngles {
    public static void register() {
        ArmamentsClient.addAngles(ModItems.SNIPER_RIFLE, (entity, model, tickDelta) -> {
            ModelPart rightArm = model.rightArm;
            ModelPart leftArm = model.leftArm;

            boolean mainHand = entity.getMainHandStack().isOf(ModItems.SNIPER_RIFLE);
            boolean offHand  = entity.getOffHandStack().isOf(ModItems.SNIPER_RIFLE);

            if (mainHand || offHand) {
                boolean rightArmed = mainHand == entity.getMainArm().equals(Arm.RIGHT);
                int multiplier = rightArmed ? 1 : -1;

                ModelPart primary = rightArmed ? rightArm : leftArm;
                ModelPart secondary = rightArmed ? leftArm : rightArm;

                if (entity.isUsingItem() && entity.getActiveItem().isOf(ModItems.SNIPER_RIFLE) && entity.isSneaky()) {
                    primary.yaw = -0.8F * multiplier;
                    primary.pitch = -0.97079635F;
                    secondary.pitch = primary.pitch;

                    float pullTime = MathHelper.floor(EnchantmentHelper.getCrossbowChargeTime(entity.getMainHandStack(), entity, 2.5f) * 20f);
                    float useTime = MathHelper.clamp(entity.getItemUseTime() + tickDelta, 0.0F, pullTime);
                    float progress = useTime / pullTime;

                    secondary.yaw = MathHelper.lerp(progress, 0.4F, 0.85F) * multiplier;
                    secondary.pitch = MathHelper.lerp(progress, secondary.pitch, (float)(-Math.PI / 2));
                } else {
                    CrossbowPosing.hold(rightArm, leftArm, model.head, rightArmed);

                    if (entity.isUsingItem() && entity.getActiveItem().isOf(ModItems.SNIPER_RIFLE)) {
                        primary.roll  = 0.4F * multiplier;
                        primary.pitch += 0.1F;
                        primary.yaw   += 0.15F * multiplier;
                    } else {
                        primary.roll  = 0.0F;
                        primary.pitch += 0.2F;
                        primary.yaw   += 0.2F * multiplier;
                    }
                }
            }
        });

        ArmamentsClient.addAngles(ModTags.Items.ONE_HANDED_GUN, (entity, model, tickDelta) -> {
            boolean rightArm = entity.getMainArm().equals(Arm.RIGHT);

            if (entity.getMainHandStack().isIn(ModTags.Items.ONE_HANDED_GUN)) {
                if (rightArm) {
                    model.rightArm.yaw = -0.1F + model.head.yaw;
                    model.rightArm.pitch = (float) (-Math.PI / 2) + model.head.pitch;
                } else {
                    model.leftArm.yaw = 0.1F + model.head.yaw;
                    model.leftArm.pitch = (float) (-Math.PI / 2) + model.head.pitch;
                }
            }
            if (entity.getOffHandStack().isIn(ModTags.Items.ONE_HANDED_GUN)){
                if (rightArm) {
                    model.leftArm.yaw = 0.1F + model.head.yaw;
                    model.leftArm.pitch = (float) (-Math.PI / 2) + model.head.pitch;
                } else {
                    model.rightArm.yaw = -0.1F + model.head.yaw;
                    model.rightArm.pitch = (float) (-Math.PI / 2) + model.head.pitch;
                }
            }
        });
    }
}
