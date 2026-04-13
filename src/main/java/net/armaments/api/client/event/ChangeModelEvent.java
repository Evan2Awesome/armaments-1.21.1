package net.armaments.api.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ChangeModelEvent {
    Event<ChangeModelEvent> EVENT = EventFactory.createArrayBacked(ChangeModelEvent.class, events -> (stack, renderMode, leftHanded, matrices, vertexConsumers, light, overlay, model, models) -> {
        for (ChangeModelEvent event : events) {
            BakedModel bakedModel = event.changeModel(stack, renderMode, leftHanded, matrices, vertexConsumers, light, overlay, model, models);
            if (bakedModel != null && !bakedModel.equals(model)) return bakedModel;
        }
        return model;
    });

    @Nullable
    BakedModel changeModel(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, ItemModels models);

    static boolean is2d(ModelTransformationMode renderMode) {
        return renderMode.equals(ModelTransformationMode.GUI) || renderMode.equals(ModelTransformationMode.GROUND) || renderMode.equals(ModelTransformationMode.FIXED);
    }
}
