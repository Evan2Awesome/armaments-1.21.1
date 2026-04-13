package net.armaments.api.world;

import net.minecraft.world.World;

public interface TimedHitscanHandler extends HitscanHandler {
    default void tick(ProjectileHitscan hitscan, World world) {}
    default int ageLimit() {return 200;}
}
