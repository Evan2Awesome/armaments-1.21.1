package net.armaments.util;

import net.armaments.Armaments;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Functions {
    public static ModelIdentifier mId(String path) {
        return ModelIdentifier.ofInventoryVariant(Armaments.id(path));
    }

    @Nullable
    public static LivingEntity raycastEntity(PlayerEntity player, double distance, float margin) {
        Vec3d start = player.getCameraPosVec(1.0f);
        HitResult end = player.raycast(distance, 1.0f, false);

        EntityHitResult hitResult = ProjectileUtil.raycast(player, start, end.getPos(), new Box(start, end.getPos()), entity -> entity.canHit() && !entity.equals(player), distance * distance);
        EntityHitResult hitResult1 = ProjectileUtil.getEntityCollision(player.getWorld(), player, start, end.getPos(), new Box(start, end.getPos()),
                entity -> entity.canHit() && !entity.equals(player), margin);

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
}
