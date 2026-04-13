package net.armaments.api.item.stack_holder;

import com.mojang.serialization.DataResult;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

public interface WeightHolder {
    Fraction BUNDLE_SIZE = Fraction.getFraction(1, 16);

    @Nullable DataResult<Fraction> getWeight(ItemStack stack, Fraction multiplier);
}
