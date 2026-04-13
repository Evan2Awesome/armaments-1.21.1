package net.armaments.api.item.stack_holder;

import com.google.common.base.Suppliers;
import com.mojang.serialization.DataResult;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;
import java.util.function.Supplier;

public abstract class BundleLike<T extends StackHolder<T>> implements StackHolder<T> {
    public final Supplier<DataResult<Fraction>> weight = Suppliers.memoize(this::computeMyWeight);
    public final List<ItemStack> stacks;
    public final Fraction scale;

    public BundleLike(List<ItemStack> stacks, Fraction scale) {
        this.stacks = stacks;
        this.scale = scale;
    }

    public BundleLike(List<ItemStack> stacks, double scale) {
        this(stacks, Fraction.getFraction(scale).invert());
    }

    @Override
    public List<ItemStack> stacks() {
        return this.stacks;
    }

    @Override
    public Fraction scale() {
        return this.scale;
    }

    @Override
    public Supplier<DataResult<Fraction>> weightSupplier() {
        return this.weight;
    }
}
