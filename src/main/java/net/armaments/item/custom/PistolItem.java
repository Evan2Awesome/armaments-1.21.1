package net.armaments.item.custom;

import net.armaments.client.ModSounds;
import net.armaments.item.component.AmmoComponent;
import net.armaments.item.component.ModDataComponents;
import net.armaments.util.Functions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import net.minecraft.util.math.random.Random;

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
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 50;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        stack.set(ModDataComponents.AMMO, new AmmoComponent(this.getMaxAmmo(stack)));
        if (user instanceof PlayerEntity player) player.getItemCooldownManager().set(stack.getItem(), 20);
        return super.finishUsing(stack, world, user);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        if (stack.getOrDefault(ModDataComponents.SELECTED_COMPONENT, false)) {
            return true;
        }else {
            return super.isItemBarVisible(stack);
        }
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        if (stack.getOrDefault(ModDataComponents.SELECTED_COMPONENT, false)) {
            return 0xffdc4d;
        }else {
            return super.getItemBarColor(stack);
        }
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        if (stack.getOrDefault(ModDataComponents.SELECTED_COMPONENT, false)) {
            return (int) (13 * this.getAmmo(stack)/(float) getMaxAmmo(stack));
        }else {
            return super.getItemBarStep(stack);
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) player.getItemCooldownManager().set(stack.getItem(), 10);
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if (user instanceof PlayerEntity player) this.shoot(player, stack);
    }

    @Override
    public float getDamage(ItemStack stack) {
        return 5f;
    }

    @Override
    public int getMaxAmmo(ItemStack stack) {
        return 6;
    }

    @Override
    public void shoot(PlayerEntity shooter, ItemStack stack) {
        if (this.getAmmo(stack) >= 1) {
            stack.damage(1, shooter, EquipmentSlot.MAINHAND);
            stack.set(ModDataComponents.AMMO, new AmmoComponent(this.getAmmo(stack) - 1));
            shooter.playSound(ModSounds.GUNSHOT);
            if (Functions.raycastEntity(shooter, 100d, 1f) instanceof LivingEntity entity) entity.damage(entity.getDamageSources().playerAttack(shooter), this.getDamage(stack));
            shooter.setPitch(shooter.getPitch() - Random.create().nextBetweenExclusive(1, 11));
            shooter.setYaw(shooter.getYaw() - Random.create().nextBetweenExclusive(-2, 3));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (selected) {
            stack.set(ModDataComponents.SELECTED_COMPONENT, true);
        }else {
            stack.set(ModDataComponents.SELECTED_COMPONENT, false);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}