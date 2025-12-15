package net.armaments.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public record AmmoComponent(int ammo) implements TooltipAppender {
    public static final Codec<AmmoComponent> CODEC = Codec.INT.xmap(AmmoComponent::new, AmmoComponent::ammo);
    public static final AmmoComponent DEFAULT = new AmmoComponent(0);

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        tooltip.accept(Text.literal("Ammo: " + this.ammo()));
    }
}
