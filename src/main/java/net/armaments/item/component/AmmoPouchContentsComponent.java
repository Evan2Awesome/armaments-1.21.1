package net.armaments.item.component;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

public final class AmmoPouchContentsComponent implements TooltipData {
    public static final AmmoPouchContentsComponent DEFAULT = new AmmoPouchContentsComponent(List.of());
    public static final Codec<AmmoPouchContentsComponent> CODEC = ItemStack.CODEC.listOf().xmap(AmmoPouchContentsComponent::new, component -> component.stacks);
    public static final PacketCodec<RegistryByteBuf, AmmoPouchContentsComponent> PACKET_CODEC = ItemStack.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(AmmoPouchContentsComponent::new, component -> component.stacks);
    private static final Fraction NESTED_BUNDLE_OCCUPANCY = Fraction.getFraction(1, 16);
    private static final int ADD_TO_NEW_SLOT = -1;
    final List<ItemStack> stacks;
    final Fraction occupancy;

    AmmoPouchContentsComponent(List<ItemStack> stacks, Fraction occupancy) {
        this.stacks = stacks;
        this.occupancy = occupancy;
    }

    public AmmoPouchContentsComponent(List<ItemStack> stacks) {
        this(stacks, calculateOccupancy(stacks));
    }

    private static Fraction calculateOccupancy(List<ItemStack> stacks) {
        Fraction fraction = Fraction.ZERO;

        for (ItemStack itemStack : stacks) {
            fraction = fraction.add(getOccupancy(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
        }

        return fraction;
    }

    static Fraction getOccupancy(ItemStack stack) {
        AmmoPouchContentsComponent ammoPouchContentsComponent = stack.get(ModDataComponents.AMMO_POUCH_CONTENTS);
        if (ammoPouchContentsComponent != null) {
            return NESTED_BUNDLE_OCCUPANCY.add(ammoPouchContentsComponent.getOccupancy());
        } else {
            List<BeehiveBlockEntity.BeeData> list = stack.getOrDefault(DataComponentTypes.BEES, List.of());
            return !list.isEmpty() ? Fraction.ONE : Fraction.getFraction(1, stack.getMaxCount() * 4);
        }
    }

    public ItemStack get(int index) {
        return (ItemStack)this.stacks.get(index);
    }

    public Stream<ItemStack> stream() {
        return this.stacks.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> iterate() {
        return this.stacks;
    }

    public Iterable<ItemStack> iterateCopy() {
        return Lists.<ItemStack, ItemStack>transform(this.stacks, ItemStack::copy);
    }

    public int size() {
        return this.stacks.size();
    }

    public Fraction getOccupancy() {
        return this.occupancy;
    }

    public boolean isEmpty() {
        return this.stacks.isEmpty();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof AmmoPouchContentsComponent ammoPouchContentsComponent)
                    ? false
                    : this.occupancy.equals(ammoPouchContentsComponent.occupancy) && ItemStack.stacksEqual(this.stacks, ammoPouchContentsComponent.stacks);
        }
    }

    public int hashCode() {
        return ItemStack.listHashCode(this.stacks);
    }

    public String toString() {
        return "BundleContents" + this.stacks;
    }

    public static class Builder {
        private final List<ItemStack> stacks;
        private Fraction occupancy;

        public Builder(AmmoPouchContentsComponent base) {
            this.stacks = new ArrayList(base.stacks);
            this.occupancy = base.occupancy;
        }

        public AmmoPouchContentsComponent.Builder clear() {
            this.stacks.clear();
            this.occupancy = Fraction.ZERO;
            return this;
        }

        private int addInternal(ItemStack stack) {
            if (!stack.isStackable()) {
                return -1;
            } else {
                for (int i = 0; i < this.stacks.size(); i++) {
                    if (ItemStack.areItemsAndComponentsEqual((ItemStack)this.stacks.get(i), stack)) {
                        return i;
                    }
                }

                return -1;
            }
        }

        private int getMaxAllowed(ItemStack stack) {
            Fraction fraction = Fraction.ONE.subtract(this.occupancy);
            return Math.max(fraction.divideBy(AmmoPouchContentsComponent.getOccupancy(stack)).intValue(), 0);
        }

        public int add(ItemStack stack) {
            if (!stack.isEmpty() && stack.getItem().canBeNested()) {
                int i = Math.min(stack.getCount(), this.getMaxAllowed(stack));
                if (i == 0) {
                    return 0;
                } else {
                    this.occupancy = this.occupancy.add(AmmoPouchContentsComponent.getOccupancy(stack).multiplyBy(Fraction.getFraction(i, 1)));
                    int j = this.addInternal(stack);
                    if (j != -1) {
                        ItemStack itemStack = (ItemStack)this.stacks.remove(j);
                        ItemStack itemStack2 = itemStack.copyWithCount(itemStack.getCount() + i);
                        stack.decrement(i);
                        this.stacks.add(0, itemStack2);
                    } else {
                        this.stacks.add(0, stack.split(i));
                    }

                    return i;
                }
            } else {
                return 0;
            }
        }

        public int add(Slot slot, PlayerEntity player) {
            ItemStack itemStack = slot.getStack();
            int i = this.getMaxAllowed(itemStack);
            return this.add(slot.takeStackRange(itemStack.getCount(), i, player));
        }

        @Nullable
        public ItemStack removeFirst() {
            if (this.stacks.isEmpty()) {
                return null;
            } else {
                ItemStack itemStack = ((ItemStack)this.stacks.remove(0)).copy();
                this.occupancy = this.occupancy.subtract(AmmoPouchContentsComponent.getOccupancy(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
                return itemStack;
            }
        }

        public Fraction getOccupancy() {
            return this.occupancy;
        }

        public AmmoPouchContentsComponent build() {
            return new AmmoPouchContentsComponent(List.copyOf(this.stacks), this.occupancy);
        }
    }
}
