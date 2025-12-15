package net.armaments.item.custom;

import net.armaments.client.ModSounds;
import net.armaments.item.ModItems;
import net.armaments.item.component.AmmoComponent;
import net.armaments.item.component.ModDataComponents;
import net.armaments.util.Functions;
import net.armaments.util.ModDamages;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 50;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        this.reload(stack, user);
        user.playSound(SoundEvents.BLOCK_COPPER_TRAPDOOR_CLOSE, 1F, 1.2F);
        if (user instanceof PlayerEntity player) player.getItemCooldownManager().set(stack.getItem(), 20);
        return super.finishUsing(stack, world, user);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return super.isItemBarVisible(stack) || stack.getOrDefault(ModDataComponents.SELECTED_COMPONENT, false);
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
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        user.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.4F, 1.2F);
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);

        if (user instanceof PlayerEntity player) {
            //player.getItemCooldownManager().set(stack.getItem(), 10);
            if (remainingUseTicks <= 40) this.bigShot(player, stack);
        }
    }

    @Override
    public float getDamage(ItemStack stack) {
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
            stack.set(ModDataComponents.AMMO, new AmmoComponent(this.getAmmo(stack) - 1));
            shooter.playSound(ModSounds.GUNSHOT);
            if (Functions.raycastEntity(shooter, 100d) instanceof LivingEntity entity) entity.damage(((ModDamages)entity.getDamageSources()).armaments$sources().revolver(shooter), this.getDamage(stack));
            shooter.setPitch(shooter.getPitch() - shooter.getRandom().nextBetweenExclusive(1, 11));
            shooter.setYaw(shooter.getYaw() - shooter.getRandom().nextBetweenExclusive(-2, 3));
        }
    }

    public void bigShot(PlayerEntity shooter, ItemStack stack) {
        if (this.getAmmo(stack) >= 1) {
            stack.damage(1, shooter, stack.equals(shooter.getMainHandStack()) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            stack.set(ModDataComponents.AMMO, new AmmoComponent(this.getAmmo(stack) - 1));
            shooter.playSound(ModSounds.GUNSHOT);
            if (Functions.raycastEntity(shooter, 100d) instanceof LivingEntity entity) entity.damage(((ModDamages)entity.getDamageSources()).armaments$sources().revolver(shooter), 9);
            shooter.setPitch(shooter.getPitch() - shooter.getRandom().nextBetweenExclusive(5, 16));
            shooter.setYaw(shooter.getYaw() - shooter.getRandom().nextBetweenExclusive(-2, 3));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        stack.set(ModDataComponents.SELECTED_COMPONENT, selected || (entity instanceof LivingEntity livingEntity && stack.equals(livingEntity.getOffHandStack())));
        super.inventoryTick(stack, world, entity, slot, selected);
    }


    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        if (ingredient.getItem() == Items.IRON_INGOT) {
            return true;
        } else return false;
    }
}