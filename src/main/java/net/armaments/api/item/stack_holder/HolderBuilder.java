package net.armaments.api.item.stack_holder;

import com.mojang.serialization.DataResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HolderBuilder<T extends StackHolder<T>> implements StackHolder<T> {
    public final T holder;
    public final List<ItemStack> stacks;
    public Fraction weight;

    public HolderBuilder(T holder) {
        this.holder = holder;
        DataResult<Fraction> weight = holder.weight();

        if (weight.isError()) {
            this.stacks = new ArrayList<>();
            this.weight = Fraction.ZERO;
        } else {
            this.stacks = holder.stackStream().collect(Collectors.toList());
            this.weight = weight.getOrThrow();
        }
    }

    @Override public List<ItemStack> stacks() {return this.stacks;}
    @Override public Supplier<DataResult<Fraction>> weightSupplier() {return this::weight;}
    @Override public Fraction scale() {return this.holder.scale();}
    @Override public int maxStackSize(ItemStack stack) {return this.holder.maxStackSize(stack);}
    @Override public boolean canInsert(ItemStack stack) {return this.holder.canInsert(stack);}
    @Override public Stream<ItemStack> stackStream() {return this.stacks.stream().map(ItemStack::copy);}
    @Override public DataResult<Fraction> weight() {return DataResult.success(this.weight);}
    @Override public HolderBuilder<T> builder() {return this;}
    @Override public @Nullable DataResult<Fraction> overrideWeight(ItemStack stack) {return this.holder.overrideWeight(stack);}
    @Override public DataResult<Fraction> getPerWeight(ItemStack stack) {return this.holder.getPerWeight(stack);}
    @Override public DataResult<Fraction> getWeight(ItemStack stack) {return this.holder.getWeight(stack);}

    public int getMaxAmount(Fraction fraction) {return Math.max(Fraction.ONE.subtract(this.weight).divideBy(fraction).intValue(), 0);}
    public void addWeight(Fraction weight, int count) {this.weight = this.weight.add(weight.multiplyBy(Fraction.getFraction(count, 1)));}
    public void subtractWeight(Fraction weight, int count) {this.addWeight(weight, -count);}

    public ItemStack tryAdd(ItemStack addingStack) {
        if (!this.canInsert(addingStack)) return ItemStack.EMPTY;
        DataResult<Fraction> result = this.getPerWeight(addingStack);
        if (result.isError()) return ItemStack.EMPTY;
        Fraction itemWeight = result.getOrThrow();
        int adding = Math.min(this.getMaxAmount(itemWeight), addingStack.getCount());
        if (adding == 0) return ItemStack.EMPTY;
        this.subtractWeight(weight, adding);
        ItemStack returningStack = addingStack.copyWithCount(adding);
        ItemStack insertingStack = addingStack.split(adding);

        for (ItemStack listedStack : this.stacks.reversed()) {
            if (insertingStack.isEmpty() || !insertingStack.isStackable()) break;
            if (listedStack.isEmpty() || !listedStack.isStackable() || !ItemStack.areItemsAndComponentsEqual(insertingStack, listedStack)) continue;
            int decrement = Math.min(insertingStack.getCount(), this.maxStackSize(listedStack) - listedStack.getCount());
            if (decrement <= 0) continue;
            listedStack.increment(decrement);
            insertingStack.decrement(decrement);
        }

        while (!insertingStack.isEmpty()) {
            int decrement = Math.min(insertingStack.getCount(), this.maxStackSize(insertingStack));
            this.stacks.addFirst(insertingStack.split(decrement));
        }

        return returningStack;
    }

    public ItemStack trySlotAdd(PlayerEntity player, Slot slot) {
        ItemStack slottedStack = slot.getStack();
        if (!this.canInsert(slottedStack)) return ItemStack.EMPTY;
        DataResult<Fraction> itemWeight = this.getPerWeight(slottedStack);
        if (itemWeight.isError()) return ItemStack.EMPTY;
        int adding = this.getMaxAmount(itemWeight.getOrThrow());
        return this.tryAdd(slot.takeStackRange(slottedStack.getCount(), adding, player));
    }

    public ItemStack removeStack(int index, int count) {
        if (count == 0 || this.stacks.isEmpty() || (index >= this.stacks.size() || index < 0)) return ItemStack.EMPTY;
        ItemStack removingStack = this.stacks.get(index);
        int removing = count == -1 ? removingStack.getCount() : Math.min(removingStack.getCount(), count);
        if (removing == 0) return ItemStack.EMPTY;
        DataResult<Fraction> itemWeight = this.getPerWeight(removingStack);
        if (itemWeight.isError()) return ItemStack.EMPTY;
        ItemStack removedStack = removingStack.split(removing);
        if (removedStack.isEmpty()) this.stacks.remove(index);
        this.subtractWeight(itemWeight.getOrThrow(), removing);
        return removedStack;
    }

    public ItemStack removeSelectedStack() {return this.removeStack(0, -1);}

    public List<ItemStack> finalizeStacks() {return this.stacks.stream().filter(stack -> !stack.isEmpty()).toList();}

    public T build() {return this.build(this);}
    @Override public T build(HolderBuilder<T> builder) {return this.holder.build(builder);}
}
