package net.armaments.api.world;

import net.armaments.api.data.Vec3dHolder;
import net.armaments.api.data.mapped_data.DataMapper;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface ProjectileHitscan extends TickingObject, DataMapper, Vec3dHolder {
    @Nullable
    default Entity owner() {
        return null;
    }

    @Override
    default void baseTick(World world) {
        this.incrementAge();
        this.tick(world);
    }

    @Override
    default boolean removed(World world) {
        return (this.ageLimit() != -1 && this.age() >= this.ageLimit()) || !world.shouldTickBlockPos(this.blockPos());
    }

    default void tick(World world) {}

    int age();
    void incrementAge();
    default int ageLimit() {return 200;}
}
