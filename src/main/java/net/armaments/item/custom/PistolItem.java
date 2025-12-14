package net.armaments.item.custom;

import net.armaments.item.ModDataComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        stack.set(ModDataComponents.USE_COMPONENT, true);
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        stack.set(ModDataComponents.USE_COMPONENT, false);
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        stack.set(ModDataComponents.USE_COMPONENT, false);
        return super.finishUsing(stack, world, user);
    }

    @Override
    public float getDamage() {
        return 5f;
    }

    @Override
    public void shoot(LivingEntity entity) {
        entity.addVelocity(0, 2d, 0);
    }
}