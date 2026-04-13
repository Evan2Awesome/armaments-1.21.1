package net.armaments.api.world;

import net.minecraft.world.World;

public interface TickingObject {
    default void baseTick(World world) {}
    default boolean isActive(World world) {return !this.removed(world);}
    void remove(World world);
    boolean removed(World world);
}
