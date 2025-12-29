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
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class EchoGunItem extends AbstractGunItem{
    public EchoGunItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        user.playSound(SoundEvents.BLOCK_COPPER_TRAPDOOR_CLOSE, 1F, 1.2F);
        return super.finishUsing(stack, world, user);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 60;
    }

    @Override
    public float getDamage(ItemStack stack, LivingEntity shooter) {
        return 4F;
    }

    @Override
    public int getMaxAmmo(ItemStack stack) {
        return 8;
    }

    @Override
    public Item ammoItem(ItemStack stack) {
        return ModItems.BULLET;
    }

    @Override
    public void shoot(PlayerEntity shooter, ItemStack gun) {
        if (this.getAmmo(gun) >= 1) {
            gun.damage(1, shooter, gun.equals(shooter.getMainHandStack()) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            gun.set(ModDataComponents.AMMO, this.getAmmo(gun) - 1);
            shooter.playSound(ModSounds.ECHO_GUNSHOT);
            if (Functions.raycastEntity(shooter, 100d) instanceof LivingEntity entity) entity.damage(ModDamageSources.of(shooter).revolver(shooter), this.getDamage(gun, shooter));
            shooter.setPitch(shooter.getPitch() - shooter.getRandom().nextBetweenExclusive(1, 11));
            shooter.setYaw(shooter.getYaw() - shooter.getRandom().nextBetweenExclusive(-2, 3));
        }
    }
}
