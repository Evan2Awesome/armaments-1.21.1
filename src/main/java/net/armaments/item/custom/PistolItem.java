package net.armaments.item.custom;

import net.armaments.client.ModSounds;
import net.armaments.util.Functions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class PistolItem extends Item implements GunItem {
    public PistolItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return super.use(world, user, hand);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if (user instanceof PlayerEntity player) this.shoot(player, stack);
    }

    @Override
    public float getDamage(ItemStack stack) {
        return 5f;
    }

    @Override
    public void shoot(PlayerEntity shooter, ItemStack stack) {
        shooter.playSound(ModSounds.GUNSHOT, 1f, 1f);
        if (Functions.raycastEntity(shooter, 50d, 1f) instanceof LivingEntity entity) entity.damage(entity.getDamageSources().playerAttack(shooter), this.getDamage(stack));
    }
}