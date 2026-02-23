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
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SniperItem extends AbstractGunItem implements GunItem {
    public SniperItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!player.isSneaky())
            player.playSound(SoundEvents.ITEM_SPYGLASS_USE, 1.0F, 1.0F);
        return super.use(world, player, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        user.playSound(SoundEvents.BLOCK_COPPER_TRAPDOOR_CLOSE, 1F, 1.2F);
        return super.finishUsing(stack, world, user);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!user.isSneaky())
            user.playSound(SoundEvents.ITEM_SPYGLASS_STOP_USING, 1.0F, 1.0F);
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
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
    public int getReloadTime(ItemStack stack) {
        return 160;
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
    public void shoot(PlayerEntity shooter, ItemStack gun) {
        if (this.getAmmo(gun) >= 1) {
            gun.damage(1, shooter, gun.equals(shooter.getMainHandStack()) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            gun.set(ModDataComponents.AMMO, this.getAmmo(gun) - 1);
            shooter.playSound(ModSounds.GUNSHOT);
            if (Functions.raycastEntity(shooter, 200d) instanceof LivingEntity entity) entity.damage(ModDamageSources.of(shooter).revolver(shooter), this.getDamage(gun, shooter));
            shooter.setPitch(shooter.getPitch() - shooter.getRandom().nextBetweenExclusive(1, 11));
            shooter.setYaw(shooter.getYaw() - shooter.getRandom().nextBetweenExclusive(-2, 3));
        }
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.GOLD_INGOT);
    }
}