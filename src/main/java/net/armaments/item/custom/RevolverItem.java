package net.armaments.item.custom;

import net.armaments.api.data.mapped_data.DataKey;
import net.armaments.api.network.s2c.MultiParticleS2CPacket;
import net.armaments.api.world.GravityAffectedHitscan;
import net.armaments.api.world.ProjectileHitscan;
import net.armaments.api.world.TimedHitscanHandler;
import net.armaments.client.ModSounds;
import net.armaments.entity.ModDamageSources;
import net.armaments.item.ModItems;
import net.armaments.item.component.ModDataComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.List;

public class RevolverItem extends AbstractGunItem {
    private static final DataKey<Float> DAMAGE = DataKey.create();
    private static final GravityAffectedHitscan.Builder BUILDER = new GravityAffectedHitscan.Builder(new TimedHitscanHandler() {
        @Override
        public void update(ProjectileHitscan hitscan, World world, List<Vec3d> positions) {
            if (world instanceof ServerWorld serverWorld) {
                MultiParticleS2CPacket.sendParticles(serverWorld, ParticleTypes.CRIT, true, positions, new Vector3f(), 0f, 3);
            }
        }

        @Override
        public void tick(ProjectileHitscan hitscan, World world) {
            if (hitscan instanceof GravityAffectedHitscan gravity) {
                if (hitscan.age() >= 3) {
                    gravity.setForce(world, gravity.getForce(world).multiply(0.98d, 1d, 0.98d));
                    gravity.setCounterforce(world, gravity.getCounterforce(world).add(0d, -0.02d, 0d));
                }
            }
        }

        @Override
        public void onEntityHit(ProjectileHitscan hitscan, World world, EntityHitResult result) {
            Entity entity = result.getEntity();
            entity.damage(ModDamageSources.of(entity).revolver(hitscan.owner()), hitscan.getDataOrDefault(DAMAGE, 6f));
            TimedHitscanHandler.super.onEntityHit(hitscan, world, result);
        }
    }, 20d * 20, 2d);

    public RevolverItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 30;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        user.playSound(SoundEvents.BLOCK_COPPER_TRAPDOOR_CLOSE, 1F, 1.2F);
        this.reload(stack, user);
        if (user instanceof PlayerEntity player) player.getItemCooldownManager().set(stack.getItem(), 10);
        return super.finishUsing(stack, world, user);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        user.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.4F, 1.2F);
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if (user instanceof PlayerEntity player) {
            if (remainingUseTicks <= 20) this.bigShot(player, stack);
        }
    }

    @Override
    public float getDamage(ItemStack stack, LivingEntity shooter) {
        return 6f;
    }

    @Override
    public int getMaxAmmo(ItemStack stack) {
        return 6;
    }

    @Override
    public int getReloadTime(ItemStack stack) {
        return 50;
    }

    @Override
    public float getKickback(ItemStack stack) {
        return 1;
    }

    @Override
    public Item ammoItem(ItemStack stack) {
        return ModItems.BULLET;
    }

    @Override
    public void shoot(PlayerEntity shooter, ItemStack stack) {
        if (this.getAmmo(stack) >= 1) {
            Vec3d vec3d = shooter.getCameraPosVec(1.0f).add(shooter.getRotationVec(1.0f).multiply(1));
            shooter.getWorld().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, vec3d.x, vec3d.y, vec3d.z, 0.0f + ((double) shooter.getRandom().nextBetweenExclusive(-10, 11)/500), 0.05f, 0.0f + ((double) shooter.getRandom().nextBetweenExclusive(-10, 11)/500));

            stack.damage(1, shooter, stack.equals(shooter.getMainHandStack()) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            stack.set(ModDataComponents.AMMO, this.getAmmo(stack) - 1);
            shooter.playSound(ModSounds.GUNSHOT);
            ProjectileHitscan hitscan = BUILDER.build(shooter, shooter.getCameraPosVec(1f), shooter.getRotationVec(1f));
            hitscan.setData(DAMAGE, this.getDamage(stack, shooter));
//            if (Functions.raycastEntity(shooter, 100d) instanceof LivingEntity entity) entity.damage(ModDamageSources.of(shooter).revolver(shooter), this.getDamage(stack, shooter));
            shooter.setPitch(shooter.getPitch() - shooter.getRandom().nextBetweenExclusive(1, 11));
            shooter.setYaw(shooter.getYaw() - shooter.getRandom().nextBetweenExclusive(-2, 3));
        }
    }

    public void bigShot(PlayerEntity shooter, ItemStack stack) {
        if (this.getAmmo(stack) >= 1) {
            Vec3d vec3d = shooter.getCameraPosVec(1.0f).add(shooter.getRotationVec(1.0f).multiply(1));
            shooter.getWorld().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, vec3d.x, vec3d.y, vec3d.z, 0.0f + ((double) shooter.getRandom().nextBetweenExclusive(-10, 11)/500), 0.05f, 0.0f + ((double) shooter.getRandom().nextBetweenExclusive(-10, 11)/500));

            stack.damage(1, shooter, stack.equals(shooter.getMainHandStack()) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            stack.set(ModDataComponents.AMMO, this.getAmmo(stack) - 1);
            shooter.playSound(ModSounds.GUNSHOT);
            ProjectileHitscan hitscan = BUILDER.build(shooter, shooter.getCameraPosVec(1f), shooter.getRotationVec(1f));
            hitscan.setData(DAMAGE, this.getDamage(stack, shooter) * 1.5f);
//            if (Functions.raycastEntity(shooter, 100d) instanceof LivingEntity entity) entity.damage(ModDamageSources.of(shooter).revolver(shooter), this.getDamage(stack, shooter) * 1.5F);
            shooter.setPitch(shooter.getPitch() - shooter.getRandom().nextBetweenExclusive(5, 16));
            shooter.setYaw(shooter.getYaw() - shooter.getRandom().nextBetweenExclusive(-2, 3));
        }
    }

    @Override
    public boolean canUseAndShoot(ItemStack gun, LivingEntity entity) {
        return false;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.IRON_INGOT);
    }
}