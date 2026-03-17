package net.armaments.item.component;

import com.google.common.collect.Lists;
import net.armaments.util.Functions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public interface StackHolder<T extends StackHolder<T>> {
    List<ItemStack> stacks();
    Fraction occupancy();
    T build(List<ItemStack> stacks, Fraction occupancy);

    @SuppressWarnings("unchecked")
    default T getThis() {
        return (T) this;
    }

    default boolean canInsert(ItemStack stack) {
        return stack.getItem().canBeNested();
    }

    default ItemStack get(int index) {
        return this.stacks().get(index);
    }

    default int size() {
        return this.stacks().size();
    }

    default Stream<ItemStack> stream() {
        return this.stacks().stream().map(ItemStack::copy);
    }

    default Iterable<ItemStack> iterate() {
        return this.stacks();
    }

    default Iterable<ItemStack> iterateCopy() {
        return Lists.transform(this.stacks(), ItemStack::copy);
    }

    default int getStackCount() {
        return 1;
    }

    default int stackCount() {
        return Math.max(this.getStackCount(), 1);
    }

    default boolean equalsHolder(StackHolder<?> holder) {
        return this.occupancy().equals(holder.occupancy()) && Functions.stacksMatch(this.stacks(), holder.stacks());
    }

    default int hashItems() {
        return Functions.listHashCode(this.stacks());
    }

    default String stringify() {
        return "StackHolder Items: " + this.stacks();
    }

    default boolean isEmpty() {
        return this.stacks().isEmpty();
    }

    static Fraction calculateOccupancy(List<ItemStack> stacks, int stackCount) {
        Fraction occupancy = Fraction.ZERO;
        for (ItemStack stack : stacks) {
            occupancy = occupancy.add(getOccupancy(stack, stackCount));
        }
        return occupancy;
    }

    default Fraction getOccupancy(ItemStack stack) {
        return getOccupancy(stack, this.stackCount());
    }

    static Fraction getOccupancy(ItemStack stack, int stackCount) {
        return Fraction.getFraction(stack.getCount(), stack.getMaxCount() * stackCount);
    }

    default Fraction getPerOccupancy(ItemStack stack) {
        return Fraction.getFraction(1, stack.getMaxCount() * this.stackCount());
    }

    static Fraction getPerOccupancy(ItemStack stack, int stackCount) {
        return Fraction.getFraction(1, stack.getMaxCount() * stackCount);
    }

    default boolean dropAllItems(PlayerEntity player) {
        if (!this.isEmpty()) {
            if (player instanceof ServerPlayerEntity) this.iterateCopy().forEach(stack -> player.dropItem(stack, true));
            return true;
        } else return false;
    }

    default Builder<T> builder() {
        return new Builder<>(this.getThis());
    }

    class Builder<T extends StackHolder<T>> {
        public final T holder;
        public final List<ItemStack> stacks;
        public Fraction occupancy;

        public Builder(T holder) {
            this.holder = holder;
            this.stacks = new ArrayList<>(holder.stacks());
            this.occupancy = holder.occupancy();
        }

        public Builder<T> clear() {
            this.stacks.clear();
            this.occupancy = Fraction.ZERO;
            return this;
        }

        public ItemStack removeFirst() {
            if (this.stacks.isEmpty()) return ItemStack.EMPTY;
            else {
                ItemStack stack = this.stacks.removeFirst().copy();
                this.occupancy = this.occupancy.subtract(this.holder.getOccupancy(stack));
                return stack;
            }
        }

        private int getMaxAllowed(ItemStack stack) {
            Fraction fraction = Fraction.ONE.subtract(this.occupancy);
            return Math.max(fraction.divideBy(this.holder.getPerOccupancy(stack)).intValue(), 0);
        }

        public int add(ItemStack stack) {
            if (!stack.isEmpty() && stack.getItem().canBeNested()) {
                int maxAllowed = Math.min(this.getMaxAllowed(stack), stack.getCount());
                if (maxAllowed == 0) return 0;
                this.occupancy = this.occupancy.add(this.holder.getPerOccupancy(stack).multiplyBy(Fraction.getFraction(maxAllowed, 1)));
                ItemStack insertingStack = stack.split(maxAllowed);
                while (!insertingStack.isEmpty()) {
                    for (ItemStack listedStack : this.stacks.reversed()) {
                        if (!insertingStack.isEmpty() && ItemStack.areItemsAndComponentsEqual(insertingStack, listedStack)) {
                            int decrementAmount = Math.min(insertingStack.getCount(), listedStack.getMaxCount() - listedStack.getCount());
                            insertingStack.decrement(decrementAmount);
                            listedStack.increment(decrementAmount);
                        }
                    }
                    if (!insertingStack.isEmpty()) {
                        this.stacks.addFirst(insertingStack.copy());
                        insertingStack.decrement(insertingStack.getCount());
                    }
                }
                return maxAllowed;
            } else return 0;
        }

        public int add(Slot slot, PlayerEntity player) {
            ItemStack itemStack = slot.getStack();
            int maxAllowed = this.getMaxAllowed(itemStack);
            return this.add(slot.takeStackRange(itemStack.getCount(), maxAllowed, player));
        }

        public T build() {
            return this.holder.build(this.stacks, this.occupancy);
        }
    }
}
