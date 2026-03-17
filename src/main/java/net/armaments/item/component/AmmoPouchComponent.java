package net.armaments.item.component;

import com.mojang.serialization.Codec;
import net.armaments.util.ModTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public record AmmoPouchComponent(List<ItemStack> stacks, Fraction occupancy) implements StackHolder<AmmoPouchComponent>, TooltipData {
    public static final AmmoPouchComponent DEFAULT = new AmmoPouchComponent(List.of(), Fraction.ZERO);
    public static final Codec<AmmoPouchComponent> CODEC = ItemStack.CODEC.listOf().xmap(AmmoPouchComponent::new, AmmoPouchComponent::stacks);
    public static final PacketCodec<RegistryByteBuf, AmmoPouchComponent> PACKET_CODEC = ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()).xmap(AmmoPouchComponent::new, AmmoPouchComponent::stacks);

    public AmmoPouchComponent(List<ItemStack> stacks) {
        this(stacks, StackHolder.calculateOccupancy(stacks, 4));
    }

    @Override
    public int getStackCount() {
        return 4;
    }

    @Override
    public Builder builder() {
        return new Builder(this);
    }

    @Override
    public AmmoPouchComponent build(List<ItemStack> stacks, Fraction occupancy) {
        return new AmmoPouchComponent(stacks, occupancy);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isIn(ModTags.Items.AMMO_ITEM) && StackHolder.super.canInsert(stack);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof StackHolder<?> holder && this.equalsHolder(holder));
    }

    @Override
    public int hashCode() {
        return this.hashItems();
    }

    @Override @NotNull
    public String toString() {
        return this.stringify();
    }

    public static class Builder extends StackHolder.Builder<AmmoPouchComponent> {
        public Builder(AmmoPouchComponent holder) {
            super(holder);
        }

        public int removeOfItem(Item item, int max) {
            int retrieved = 0;
            Iterator<ItemStack> iterator = this.stacks.iterator();
            while (iterator.hasNext()) {
                ItemStack stack = iterator.next();
                if (stack.isOf(item)) {
                    int decrementAmount = Math.min(stack.getCount(), max - retrieved);
                    retrieved += decrementAmount;
                    stack.decrement(decrementAmount);
                    if (stack.isEmpty()) iterator.remove();
                    if (retrieved == max) return retrieved;
                }
            }
            return retrieved;
        }
    }
}
