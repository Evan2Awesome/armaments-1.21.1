package net.armaments.api.world;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface GravityHitscan extends ProjectileHitscan {
    default Vec3d getNextStep(World world) {
        return this.getPos().add(this.getForce(world)).add(this.getCounterforce(world));
    }

    Vec3d getForce(World world);
    void setForce(World world, Vec3d force);
    Vec3d getCounterforce(World world);
    void setCounterforce(World world, Vec3d counterforce);
}
