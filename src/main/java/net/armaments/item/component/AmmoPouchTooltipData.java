package net.armaments.item.component;

import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.tooltip.TooltipData;

public record AmmoPouchTooltipData(AmmoPouchContentsComponent contents) implements TooltipData {
}
