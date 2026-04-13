package net.armaments.api.item.stack_holder;

import com.mojang.serialization.DataResult;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BundleLikeItem<T extends StackHolder<T>> extends Item implements WeightHolder {
    public static final int FULL_BAR_COLOR = ColorHelper.Argb.fromFloats(1.0F, 1.0F, 0.33F, 0.33F);
    public static final int BAR_COLOR = ColorHelper.Argb.fromFloats(1.0F, 0.44F, 0.53F, 1.0F);
    public static final Fraction BUNDLE_SIZE = Fraction.getFraction(1, 16);

    public final ComponentType<T> type;
    public final T empty;

    public BundleLikeItem(Settings settings, ComponentType<T> type, T empty) {
        super(settings);
        this.type = type;
        this.empty = empty;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType type, PlayerEntity player) {
        T holder = stack.get(this.type);
        if (holder == null) return false;
        ItemStack slotStack = slot.getStack();
        HolderBuilder<T> builder = holder.builder();

        if (type.equals(ClickType.LEFT) && !slotStack.isEmpty()) {
            if (!builder.trySlotAdd(player, slot).isEmpty()) this.playInsertSound(player);

            stack.set(this.type, builder.build());
            this.broadcastChanges(player);
            return true;
        } else if (type.equals(ClickType.RIGHT) && slotStack.isEmpty()) {
            ItemStack emptiedStack = builder.removeSelectedStack();

            if (!emptiedStack.isEmpty()) {
                ItemStack remainder = slot.insertStack(emptiedStack);
                if (remainder.getCount() > 0) builder.tryAdd(remainder);
                else this.playRemoveSound(player);
            }

            stack.set(this.type, builder.build());
            this.broadcastChanges(player);
            return true;
        } else return false;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack slotStack, Slot slot, ClickType type, PlayerEntity player, StackReference reference) {
        if (type.equals(ClickType.LEFT) && slotStack.isEmpty()) {
            return false;
        }

        T holder = stack.get(this.type);
        if (holder == null) return false;
        HolderBuilder<T> builder = holder.builder();

        if (type.equals(ClickType.LEFT) && !slotStack.isEmpty()) {
            if (slot.canTakePartial(player) && !builder.tryAdd(slotStack).isEmpty()) this.playInsertSound(player);
            stack.set(this.type, builder.build());
            this.broadcastChanges(player);
            return true;
        } else if (type.equals(ClickType.RIGHT) && slotStack.isEmpty()) {
            if (slot.canTakePartial(player)) {
                ItemStack removedStack = builder.removeSelectedStack();
                if (!removedStack.isEmpty()) {
                    this.playRemoveSound(player);
                    reference.set(removedStack);
                }
            }

            stack.set(this.type, builder.build());
            this.broadcastChanges(player);
            return true;
        } else return false;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        T holder = stack.get(this.type);
        if (holder == null) return false;
        return this.getWeight(holder).compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        T holder = stack.get(this.type);
        if (holder == null) return 0;
        return Math.min(1 + MathHelper.multiplyFraction(this.getWeight(holder), 12), 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        T holder = stack.get(this.type);
        if (holder == null) return BAR_COLOR;
        return this.getWeight(holder).compareTo(Fraction.ONE) >= 0 ? FULL_BAR_COLOR : BAR_COLOR;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        player.setCurrentHand(hand);
        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Override
    public void usageTick(World world, LivingEntity entity, ItemStack stack, int ticksRemaining) {
        if (entity instanceof PlayerEntity player) {
            int useDuration = this.getMaxUseTime(stack, entity);
            boolean firstTick = ticksRemaining == useDuration;
            if (firstTick || ticksRemaining < useDuration - 10 && ticksRemaining % 2 == 0) {
                this.dropItem(world, player, stack);
            }
        }
        super.usageTick(world, entity, stack, ticksRemaining);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 200;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BRUSH;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return Optional.ofNullable(stack.get(this.type));
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        ItemStack stack = entity.getStack();
        if (stack.get(this.type) instanceof T holder) {
            stack.set(this.type, this.empty);
            ItemUsage.spawnItemContents(entity, holder.stacks());
        }
        super.onItemEntityDestroyed(entity);
    }

    public Fraction getWeight(T holder) {
        return switch (holder.weight()) {
            case DataResult.Success<Fraction> success -> success.value();
            case DataResult.Error<?> ignored -> Fraction.ONE;
        };
    }

    public void dropItem(World world, PlayerEntity player, ItemStack stack) {
        T holder = stack.get(this.type);
        if (holder == null || holder.isEmpty()) return;
        HolderBuilder<T> builder = holder.builder();
        ItemStack droppedStack = builder.removeSelectedStack();
        if (droppedStack.isEmpty()) return;
        this.playRemoveSound(player);
        stack.set(this.type, builder.build());
        player.dropItem(droppedStack, true);
        this.playDropContentsSound(world, player);
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    public void broadcastChanges(PlayerEntity player) {
        if (player.currentScreenHandler instanceof ScreenHandler handler) {
            handler.onContentChanged(player.getInventory());
        }
    }

    public void playRemoveSound(Entity entity) {
        entity.playSound(
                SoundEvents.ITEM_BUNDLE_REMOVE_ONE,
                0.8f,
                0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f
        );
    }

    public void playInsertSound(Entity entity) {
        entity.playSound(
                SoundEvents.ITEM_BUNDLE_INSERT,
                0.8f,
                0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f
        );
    }

    public void playDropContentsSound(World world, Entity entity) {
        world.playSound(
                null,
                entity.getBlockPos(),
                SoundEvents.ITEM_BUNDLE_DROP_CONTENTS,
                SoundCategory.PLAYERS,
                0.8f,
                0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f
        );
    }

    @Override
    public @Nullable DataResult<Fraction> getWeight(ItemStack stack, Fraction multiplier) {
        if (stack.get(this.type) instanceof T holder) {
            return holder.weight().map(weight -> weight.add(BUNDLE_SIZE.multiplyBy(multiplier)));
        } else return null;
    }
}