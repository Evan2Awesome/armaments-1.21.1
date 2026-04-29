package net.armaments.item.custom;

import net.armaments.client.ModSounds;
import net.armaments.entity.ModDamageSources;
import net.armaments.item.ModItems;
import net.armaments.item.component.ModDataComponents;
import net.armaments.util.Functions;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class BoltActionRifleItem extends AbstractGunItem{
    public BoltActionRifleItem(Settings settings) {
        super(settings);
    }

    @Override
    public float getDamage(ItemStack stack, LivingEntity shooter) {
        return 10;
    }

    @Override
    public int getMaxAmmo(ItemStack stack) {
        return 1;
    }

    @Override
    public int getReloadTime(ItemStack stack) {
        return 80;
    }

    @Override
    public float getKickback(ItemStack stack) {
        return 0.6f;
    }

    @Override
    public Item ammoItem(ItemStack stack) {
        return ModItems.HEAVY_BULLET;
    }

    @Override
    public float get_ads_zoom(ItemStack stack) {
        return 0.3f;
    }

    @Override
    public void shoot(PlayerEntity shooter, ItemStack stack) {
        Vec3d vec3d = shooter.getCameraPosVec(1.0f).add(shooter.getRotationVec(1.0f).multiply(1));
        if (this.getAmmo(stack) >= 1) {
            shooter.getWorld().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, vec3d.x, vec3d.y, vec3d.z, 0.0f + ((double) shooter.getRandom().nextBetweenExclusive(-10, 11)/500), 0.05f, 0.0f + ((double) shooter.getRandom().nextBetweenExclusive(-10, 11)/500));
            stack.damage(1, shooter, stack.equals(shooter.getMainHandStack()) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            stack.set(ModDataComponents.AMMO, this.getAmmo(stack) - 1);
            shooter.playSound(ModSounds.GUNSHOT, 1f, 0.7f);
            shooter.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 1.5f, 1.5f);
            if (Functions.raycastEntity(shooter, 100d) instanceof LivingEntity entity)
                entity.damage(ModDamageSources.of(shooter).revolver(shooter), this.getDamage(stack, shooter));
            shooter.setPitch(shooter.getPitch() - shooter.getRandom().nextBetweenExclusive(5, 16));
            shooter.setYaw(shooter.getYaw() - shooter.getRandom().nextBetweenExclusive(-5, 6));
        }
    }

    @Override
    public boolean canADS(ItemStack gun, LivingEntity entity) {
        return true;
    }
}
