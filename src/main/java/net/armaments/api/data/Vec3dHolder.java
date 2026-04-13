package net.armaments.api.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;

public interface Vec3dHolder extends Position {
    Vec3d getPos();
    void setPos(Vec3d positon);

    default BlockPos blockPos() {
        return BlockPos.ofFloored(this);
    }

    @Override
    default double getX() {
        return this.getPos().getX();
    }

    @Override
    default double getY() {
        return this.getPos().getY();
    }

    @Override
    default double getZ() {
        return this.getPos().getZ();
    }
}
