package net.armaments.api.world;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.armaments.api.data.mapped_data.DataKey;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.*;

public class BasicTimedHitscan implements ProjectileHitscan {
    public final LivingEntity owner;
    private final TimedHitscanHandler handler;
    private final Set<Entity> hit = new HashSet<>();
    private final Map<DataKey<?>, Object> dataMap = new Reference2ObjectOpenHashMap<>();

    private Vec3d position;
    private Vec3d previousPosition;
    private Vec3d delta;
    private Vec3d direction;
    private final double updatesPerBlock;
    private double bpt;
    private int age;
    private boolean removed = false;

    public BasicTimedHitscan(LivingEntity owner, TimedHitscanHandler handler, Vec3d position, Vec3d direction, double bps, double updatesPerBlock) {
        this.owner = owner;
        this.handler = handler;
        this.position = position;
        this.direction = direction;
        this.updatesPerBlock = updatesPerBlock;
        this.setBps(bps);
    }

    @Override
    public Entity owner() {
        return this.owner;
    }

    @Override
    public void tick(World world) {
        BlockHitResult blockResult = world.raycast(new RaycastContext(
                this.position, this.position.add(this.delta),
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE,
                ShapeContext.absent()
        ));

        Vec3d blockHitPos = blockResult.getPos();

        EntityHitResult entityResult = ProjectileUtil.getEntityCollision(
                world, this.owner,
                this.position, blockHitPos,
                new Box(this.position, blockHitPos),
                this::canHit,
                0.3f
        );

        if (entityResult != null) {
            this.hit.add(entityResult.getEntity());
            this.handler.onEntityHit(this, world, entityResult);
            this.setPosition(entityResult.getPos());
        } else {
            if (blockResult.getType() == HitResult.Type.BLOCK) this.handler.onBlockHit(this, world, blockResult);
            this.setPosition(blockHitPos);
        }

        int updateCount = (int) (this.position.subtract(this.previousPosition).length() * this.updatesPerBlock);
        double lerp = 1d / updateCount;
        List<Vec3d> updates = new ArrayList<>();
        for (int update = 0; update <= updateCount; update++) {updates.add(this.previousPosition.lerp(this.position, lerp * update));}
        this.handler.update(this, world, updates);

        this.handler.tick(this, world);
        ProjectileHitscan.super.tick(world);
    }

    @Override
    public int ageLimit() {
        return this.handler.ageLimit();
    }

    public void setBps(double bps) {
        this.bpt = bps * 0.05d;
        this.delta = this.direction.multiply(this.bpt);
    }

    public double getBps() {
        return this.bpt * 20d;
    }

    public void setDirection(Vec3d direction) {
        this.direction = direction;
        this.delta = direction.multiply(this.bpt);
    }

    public Vec3d getDirection() {
        return this.direction;
    }

    public void setPosition(Vec3d position) {
        if (this.position == null) this.previousPosition = position;
        else this.previousPosition = this.position;
        this.position = position;
    }

    public boolean canHit(Entity entity) {
        return entity.canBeHitByProjectile() && !entity.equals(this.owner) && !this.hit.contains(entity);
    }

    @Override public int age() {return this.age;}
    @Override public void incrementAge() {this.age++;}
    @Override public Vec3d getPos() {return this.position;}

    @Override
    public void setPos(Vec3d positon) {
        this.position = positon;
    }

    @Override public void remove(World world) {this.removed = true;}
    @Override public boolean removed(World world) {return this.removed || ProjectileHitscan.super.removed(world);}

    @Override public Map<DataKey<?>, Object> dataMap() {return this.dataMap;}

    public static Vec3d getCloser(Vec3d origin, Vec3d first, Vec3d second) {
        return first.subtract(origin).lengthSquared() < second.subtract(origin).lengthSquared() ? first : second;
    }

    public record Builder(TimedHitscanHandler handler, double bps, double updatesPerBlock) {
        public BasicTimedHitscan build(LivingEntity entity, Vec3d position, Vec3d direction) {
            BasicTimedHitscan hitscan = new BasicTimedHitscan(entity, this.handler, position, direction, this.bps, this.updatesPerBlock);
            entity.getWorld().getAttachedOrCreate(TickingObjects.OBJECTS).add(hitscan);
            return hitscan;
        }
    }
}
