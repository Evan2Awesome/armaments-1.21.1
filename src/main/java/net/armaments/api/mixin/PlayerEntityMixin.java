package net.armaments.api.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.armaments.api.entity.event.SpyglassEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private PlayerEntity getPlayer() {
        return (PlayerEntity)(LivingEntity)this;
    }

    @WrapMethod(method = "isUsingSpyglass")
    private boolean armaments_api$spyglassEvent(Operation<Boolean> original) {
        return original.call() || SpyglassEvent.EVENT.invoker().usingSpyglass(this.getPlayer());
    }
}
