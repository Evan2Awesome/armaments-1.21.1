package net.armaments.item.custom;

import net.armaments.item.component.ModDataComponents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractGunItem extends Item implements GunItem {
    public AbstractGunItem(Settings settings) {
        super(settings.maxCount(1).component(ModDataComponents.AMMO, 0));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        player.setCurrentHand(hand);
        if (player.isSneaky() && this.getAmmo(player.getActiveItem()) < this.getMaxAmmo(player.getActiveItem())) {
            player.playSound(SoundEvents.BLOCK_COPPER_TRAPDOOR_CLOSE, 1F, 1.2F);
            reload(player.getActiveItem(), player);
            player.getItemCooldownManager().set(player.getActiveItem().getItem(), getReloadTime(player.getActiveItem()));
            player.stopUsingItem();
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        return super.use(world, player, hand);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return super.finishUsing(stack, world, user);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return super.isItemBarVisible(stack) || stack.getOrDefault(ModDataComponents.SELECTED_COMPONENT, false);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.SELECTED_COMPONENT, false) ? 0xffdc4d : super.getItemBarColor(stack);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.SELECTED_COMPONENT, false) ? 13 * this.getAmmo(stack) / getMaxAmmo(stack) : super.getItemBarStep(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        stack.set(ModDataComponents.SELECTED_COMPONENT, selected || slot == PlayerInventory.OFF_HAND_SLOT);
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public float get_ads_zoom(ItemStack stack) {
        return 0.5f;
    }
}