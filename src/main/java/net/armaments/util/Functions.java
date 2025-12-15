package net.armaments.util;

import net.armaments.Armaments;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Functions {
    public static ModelIdentifier mId(String path) {
        return ModelIdentifier.ofInventoryVariant(Armaments.id(path));
    }

    @Nullable
    public static LivingEntity raycastEntity(PlayerEntity player, double distance) {
        Vec3d start = player.getCameraPosVec(1.0f);
        HitResult end = Functions.raycast(player, distance, 1.0f, false);
        EntityHitResult hitResult = ProjectileUtil.getEntityCollision(player.getWorld(), player, start, end.getPos(), new Box(start, end.getPos()), entity -> entity.canHit() && entity.isAlive() && !entity.equals(player));
        return hitResult != null && hitResult.getEntity() instanceof LivingEntity entity ? entity : null;
    }

    public static ItemStack getAmmo(Item type, PlayerEntity player) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isOf(type)) stacks.add(stack);
        }
        return stacks.isEmpty() ? ItemStack.EMPTY : stacks.getFirst();
    }

    public static HitResult raycast(Entity entity, double maxDistance, float tickDelta, boolean includeFluids) {
        Vec3d vec3d = entity.getCameraPosVec(tickDelta);
        Vec3d vec3d2 = entity.getRotationVec(tickDelta);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
        return entity.getWorld()
                .raycast(
                        new RaycastContext(
                                vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, entity
                        )
                );
    }
}
