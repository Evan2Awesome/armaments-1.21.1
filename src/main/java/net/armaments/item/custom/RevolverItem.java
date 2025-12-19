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
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class RevolverItem extends AbstractGunItem {
    public RevolverItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 50;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        user.playSound(SoundEvents.BLOCK_COPPER_TRAPDOOR_CLOSE, 1F, 1.2F);
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
            if (remainingUseTicks <= 40) this.bigShot(player, stack);
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
    public Item ammoItem(ItemStack stack) {
        return ModItems.REVOLVER_AMMO;
    }

    @Override
    public void shoot(PlayerEntity shooter, ItemStack stack) {
        if (this.getAmmo(stack) >= 1) {
            stack.damage(1, shooter, stack.equals(shooter.getMainHandStack()) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            stack.set(ModDataComponents.AMMO, this.getAmmo(stack) - 1);
            shooter.playSound(ModSounds.GUNSHOT);
            if (Functions.raycastEntity(shooter, 100d) instanceof LivingEntity entity) entity.damage(ModDamageSources.of(shooter).revolver(shooter), this.getDamage(stack, shooter));
            shooter.setPitch(shooter.getPitch() - shooter.getRandom().nextBetweenExclusive(1, 11));
            shooter.setYaw(shooter.getYaw() - shooter.getRandom().nextBetweenExclusive(-2, 3));
        }
    }

    public void bigShot(PlayerEntity shooter, ItemStack stack) {
        if (this.getAmmo(stack) >= 1) {
            stack.damage(1, shooter, stack.equals(shooter.getMainHandStack()) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            stack.set(ModDataComponents.AMMO, this.getAmmo(stack) - 1);
            shooter.playSound(ModSounds.GUNSHOT);
            if (Functions.raycastEntity(shooter, 100d) instanceof LivingEntity entity) entity.damage(ModDamageSources.of(shooter).revolver(shooter), this.getDamage(stack, shooter) * 1.5F);
            shooter.setPitch(shooter.getPitch() - shooter.getRandom().nextBetweenExclusive(5, 16));
            shooter.setYaw(shooter.getYaw() - shooter.getRandom().nextBetweenExclusive(-2, 3));
        }
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.IRON_INGOT);
    }
}