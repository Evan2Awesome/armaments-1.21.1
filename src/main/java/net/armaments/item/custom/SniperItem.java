package net.armaments.item.custom;

import net.armaments.client.ModSounds;
import net.armaments.entity.ModDamageSources;
import net.armaments.item.component.ModDataComponents;
import net.armaments.util.Functions;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SniperItem extends AbstractGunItem implements GunItem {
    public SniperItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return user.isSneaky() ? 50 : 72000;
    }

    @Override
    public float getDamage(ItemStack stack, LivingEntity shooter) {
        return 15;
    }

    @Override
    public int getMaxAmmo(ItemStack stack) {
        return 4;
    }

    @Override
    public Item ammoItem(ItemStack stack) {
        return null;
    }

    @Override
    public void shoot(PlayerEntity shooter, ItemStack gun) {
        if (this.getAmmo(gun) >= 1) {
            gun.damage(1, shooter, gun.equals(shooter.getMainHandStack()) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            gun.set(ModDataComponents.AMMO, this.getAmmo(gun) - 1);
            shooter.playSound(ModSounds.GUNSHOT);
            if (Functions.raycastEntity(shooter, 100d) instanceof LivingEntity entity) entity.damage(ModDamageSources.of(shooter).revolver(shooter), this.getDamage(gun, shooter));
            shooter.setPitch(shooter.getPitch() - shooter.getRandom().nextBetweenExclusive(1, 11));
            shooter.setYaw(shooter.getYaw() - shooter.getRandom().nextBetweenExclusive(-2, 3));
        }
    }
}