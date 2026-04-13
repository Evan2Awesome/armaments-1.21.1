package net.armaments.api.item.stack_holder;

import com.mojang.serialization.DataResult;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface StackHolder<T extends StackHolder<T>> extends TooltipData {
    List<ItemStack> stacks();
    Supplier<DataResult<Fraction>> weightSupplier();
    T build(HolderBuilder<T> builder);

    default Fraction scale() {return Fraction.ONE;}
    default int maxStackSize(ItemStack stack) {return stack.getMaxCount();}
    default boolean canInsert(ItemStack stack) {return !stack.isEmpty() && stack.getItem().canBeNested();}
    default Stream<ItemStack> stackStream() {return this.stacks().stream().map(ItemStack::copy);}
    default int size() {return this.stacks().size();}
    default boolean isEmpty() {return this.stacks().isEmpty();}
    default DataResult<Fraction> weight() {return this.weightSupplier().get();}
    default ItemStack get(int index) {return this.stacks().get(index);}

    @Nullable
    default DataResult<Fraction> overrideWeight(ItemStack stack) {
        if (stack.getItem() instanceof WeightHolder holder) {
            return holder.getWeight(stack, this.scale());
        } else if (stack.get(DataComponentTypes.BUNDLE_CONTENTS) instanceof BundleContentsComponent contents) {
            return DataResult.success(contents.getOccupancy().add(WeightHolder.BUNDLE_SIZE.multiplyBy(this.scale())));
        } else if (stack.get(DataComponentTypes.BEES) instanceof List<BeehiveBlockEntity.BeeData> bees && !bees.isEmpty()) {
            return DataResult.success(Fraction.ONE.multiplyBy(this.scale()));
        } else return null;
    }

    default DataResult<Fraction> getPerWeight(ItemStack stack) {
        DataResult<Fraction> override = this.overrideWeight(stack);
        return override == null ? DataResult.success(Fraction.getFraction(1, this.maxStackSize(stack)).multiplyBy(this.scale())) : override;
    }

    default DataResult<Fraction> getWeight(ItemStack stack) {
        return this.getPerWeight(stack).map(weight -> weight.multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
    }

    default DataResult<Fraction> computeMyWeight() {
        return this.computeWeight(this.stacks());
    }

    default DataResult<Fraction> computeWeight(List<ItemStack> stacks) {
        Fraction totalWeight = Fraction.ZERO;
        for (ItemStack stack : stacks) {
            DataResult<Fraction> weight = this.getWeight(stack);
            if (weight.isError()) return weight;
            totalWeight = totalWeight.add(weight.getOrThrow());
        }
        return DataResult.success(totalWeight);
    }

    @SuppressWarnings("unchecked")
    default T getThis() {
        return (T) this;
    }

    default HolderBuilder<T> builder() {
        return new HolderBuilder<>(this.getThis());
    }
}
