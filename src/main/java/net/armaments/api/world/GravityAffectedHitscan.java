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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.*;

public class GravityAffectedHitscan implements GravityHitscan {
    private final Map<DataKey<?>, Object> dataMap = new Reference2ObjectOpenHashMap<>();
    private final Set<Entity> hitEntities = new HashSet<>();
    private final LivingEntity owner;
    private final TimedHitscanHandler handler;
    private final double updatesPerBlock;

    private Vec3d previousPosition;
    private Vec3d position;
    private Vec3d direction;
    private Vec3d force;
    private Vec3d counterforce = Vec3d.ZERO;
    private int age = 0;
    private double bptSqrd;
    private boolean removed = false;

    public GravityAffectedHitscan(LivingEntity entity, TimedHitscanHandler handler, Vec3d initialPos, Vec3d direction, double bps, double updatesPerBlock) {
        this.owner = entity;
        this.handler = handler;
        this.previousPosition = initialPos;
        this.position = initialPos;
        this.direction = direction;
        this.setBps(entity.getWorld(), bps);
        this.updatesPerBlock = updatesPerBlock;
    }

    @Override
    public void tick(World world) {
        GravityHitscan.super.tick(world);

        BlockHitResult blockResult = world.raycast(new RaycastContext(
                this.position, this.getNextStep(world),
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE,
                ShapeContext.absent()
        ));

        EntityHitResult entityResult = ProjectileUtil.raycast(
                this.owner, this.position, blockResult.getPos(),
                new Box(this.position, blockResult.getPos()), this::canHit, this.bptSqrd
        );

        if (entityResult != null) {
            this.setPos(entityResult.getPos());
            this.handler.onEntityHit(this, world, entityResult);
            this.hitEntities.add(entityResult.getEntity());
        } else {
            this.setPos(blockResult.getPos());
            if (blockResult.getType() == HitResult.Type.BLOCK) this.handler.onBlockHit(this, world, blockResult);
        }

        int updateCount = (int) (this.position.subtract(this.previousPosition).length() * this.updatesPerBlock);
        double lerp = 1d / updateCount;
        List<Vec3d> updates = new ArrayList<>();
        for (int update = 0; update <= updateCount; update++) {updates.add(this.previousPosition.lerp(this.position, lerp * update));}
        this.handler.update(this, world, updates);

        this.handler.tick(this, world);
    }

    public void setBps(World world, double bps) {
        this.bptSqrd = (bps * bps) * 0.05d;
        this.force = this.direction.multiply(bps * 0.05d);
    }

    public boolean canHit(Entity entity) {
        return entity.canBeHitByProjectile() && !entity.equals(this.owner) && !this.hitEntities.contains(entity);
    }

    @Override public Vec3d getForce(World world) {return this.force;}
    @Override public void setForce(World world, Vec3d force) {this.force = force;}
    @Override public Vec3d getCounterforce(World world) {return this.counterforce;}
    @Override public void setCounterforce(World world, Vec3d counterforce) {this.counterforce = counterforce;}

    @Override public Entity owner() {return this.owner;}
    @Override public int age() {return this.age;}
    @Override public int ageLimit() {return this.handler.ageLimit();}
    @Override public void incrementAge() {this.age++;}
    @Override public Vec3d getPos() {return this.position;}

    @Override
    public void setPos(Vec3d positon) {
        this.previousPosition = this.position;
        this.position = positon;
    }

    @Override public Map<DataKey<?>, Object> dataMap() {return this.dataMap;}

    @Override
    public void remove(World world) {this.removed = true;}
    @Override public boolean removed(World world) {return this.removed || GravityHitscan.super.removed(world);}

    public record Builder(TimedHitscanHandler handler, double bps, double updatesPerBlock) {
        public GravityAffectedHitscan build(LivingEntity entity, Vec3d positon, Vec3d direction) {
            GravityAffectedHitscan hitscan = new GravityAffectedHitscan(entity, this.handler, positon, direction, this.bps, this.updatesPerBlock);
            entity.getWorld().getAttachedOrCreate(TickingObjects.OBJECTS).add(hitscan);
            return hitscan;
        }
    }
}
