package net.armaments.api.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

@FunctionalInterface
public interface EntityAnglesEvent<T extends Entity, M extends EntityModel<T>> {
    Event<EntityAnglesEvent<LivingEntity, BipedEntityModel<LivingEntity>>> BIPED_ANGLES = EventFactory.createArrayBacked(EntityAnglesEvent.class, events -> (entity, model, limbAngle, limbDistance, animationProgress, headYaw, headPitch) -> {
        for (EntityAnglesEvent<LivingEntity, BipedEntityModel<LivingEntity>> event : events) {
            event.setAngles(entity, model, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        }
    });

    void setAngles(T entity, M model, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch);

    static float tickDelta(Entity entity, float animationProgress) {
        return entity.age - animationProgress;
    }
}
