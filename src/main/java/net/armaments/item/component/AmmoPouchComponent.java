package net.armaments.item.component;

import com.mojang.serialization.Codec;
import net.armaments.api.item.stack_holder.BundleLike;
import net.armaments.api.item.stack_holder.HolderBuilder;
import net.armaments.util.ModTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;

public class AmmoPouchComponent extends BundleLike<AmmoPouchComponent> {
    public static final Fraction SCALE = Fraction.ONE_QUARTER;
    public static final AmmoPouchComponent EMPTY = new AmmoPouchComponent(List.of());
    public static final Codec<AmmoPouchComponent> CODEC = ItemStack.CODEC.listOf().xmap(AmmoPouchComponent::new, AmmoPouchComponent::stacks);
    public static final PacketCodec<RegistryByteBuf, AmmoPouchComponent> PACKET_CODEC = ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()).xmap(AmmoPouchComponent::new, AmmoPouchComponent::stacks);

    public AmmoPouchComponent(List<ItemStack> stacks) {
        super(stacks, SCALE);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return super.canInsert(stack) && stack.isIn(ModTags.Items.AMMO_ITEM);
    }

    @Override
    public Builder builder() {
        return new Builder(this);
    }

    @Override
    public AmmoPouchComponent build(HolderBuilder<AmmoPouchComponent> builder) {
        return new AmmoPouchComponent(builder.finalizeStacks());
    }

    public static class Builder extends HolderBuilder<AmmoPouchComponent> {
        public Builder(AmmoPouchComponent component) {
            super(component);
        }

        public int removeOfItem(Item item, int max) {
            int retrieved = 0;

            for (int index = 0; index < this.size(); index++) {
                ItemStack stack = this.get(index);
                if (stack.isOf(item)) retrieved += this.removeStack(index, max - retrieved).getCount();
                if (retrieved == max) return retrieved;
            }

            return retrieved;
        }
    }
}
