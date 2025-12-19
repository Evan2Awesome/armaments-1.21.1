package net.armaments.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.armaments.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "isUsingSpyglass", at = @At("RETURN"))
    private boolean armaments$sniperScope(boolean original) {
        return original || (this.isUsingItem() && this.getActiveItem().isOf(ModItems.SNIPER_RIFLE) && !this.isSneaky());
    }
}
