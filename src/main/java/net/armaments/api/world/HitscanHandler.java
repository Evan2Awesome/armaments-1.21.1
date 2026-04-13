package net.armaments.api.world;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public interface HitscanHandler {
    default void onEntityHit(ProjectileHitscan hitscan, World world, EntityHitResult result) {
        hitscan.remove(world);
    }

    default void onBlockHit(ProjectileHitscan hitscan, World world, BlockHitResult result) {
        hitscan.remove(world);
    }

    default void update(ProjectileHitscan hitscan, World world, List<Vec3d> positions) {
    }
}
