package net.armaments.client;

import net.armaments.api.client.event.EntityAnglesEvent;
import net.armaments.util.ModTags;
import net.minecraft.client.model.ModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;

public class ModAngles {
    public static void register() {
        EntityAnglesEvent.BIPED_ANGLES.register((
                entity, model,
                limbAngle, limbDistance,
                animationProgress,
                headYaw, headPitch
        ) -> {
            boolean rightHanded = entity.getMainArm().equals(Arm.RIGHT);
            ModelPart mainArm = rightHanded ? model.rightArm : model.leftArm;
            ModelPart offArm = rightHanded ? model.leftArm : model.rightArm;

            if (entity.getMainHandStack().isIn(ModTags.Items.ONE_HANDED_GUN)) {
                mainArm.yaw = (rightHanded ? -0.1f : 0.1f) + model.head.yaw;
                mainArm.pitch = (float) (-Math.PI / 2f) + model.head.pitch;
            }
            if (entity.getOffHandStack().isIn(ModTags.Items.ONE_HANDED_GUN)) {
                offArm.yaw = (rightHanded ? 0.1f : -0.1f) + model.head.yaw;
                offArm.pitch = (float) (-Math.PI / 2f) + model.head.pitch;
            }
        });

        EntityAnglesEvent.BIPED_ANGLES.register((
                entity, model,
                limbAngle, limbDistance,
                animationProgress,
                headYaw, headPitch
        ) -> {
            boolean rightHanded = entity.getMainArm().equals(Arm.RIGHT);
            ItemStack mainStack = entity.getMainHandStack();
            ItemStack offStack = entity.getOffHandStack();

            if (mainStack.isIn(ModTags.Items.TWO_HANDED_GUN)) {
                holdTwoHanded(model.rightArm, model.leftArm, model.head, rightHanded);
                if (entity.getActiveItem().equals(mainStack)) sniperScope(model.rightArm, model.leftArm, rightHanded);
            } else if (offStack.isIn(ModTags.Items.TWO_HANDED_GUN)) {
                holdTwoHanded(model.rightArm, model.leftArm, model.head, !rightHanded);
                if (entity.getActiveItem().equals(offStack)) sniperScope(model.rightArm, model.leftArm, !rightHanded);
            }
        });
    }

    public static void holdTwoHanded(ModelPart rightArm, ModelPart leftArm, ModelPart head, boolean inRightArm) {
        ModelPart holding = inRightArm ? rightArm : leftArm;
        ModelPart supporting = inRightArm ? leftArm : rightArm;
        holding.yaw = (inRightArm ? -0.125f : 0.125f) + head.yaw;
        supporting.yaw = (inRightArm ? 0.725F : -0.725F) + head.yaw;
        holding.pitch = (float) (-Math.PI / 2) + head.pitch + 0.1F;
        supporting.pitch = -1.5F + head.pitch;
    }

    public static void sniperScope(ModelPart rightArm, ModelPart leftArm, boolean inRightArm) {
        ModelPart holding = inRightArm ? rightArm : leftArm;
        holding.roll += inRightArm ? 0.35f : -0.35f;
        holding.pitch += 0.075f;
        holding.yaw -= inRightArm ? 0.1f : -0.1f;
    }
}
