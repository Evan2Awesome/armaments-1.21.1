package net.armaments.item;

import net.armaments.Armaments;
import net.armaments.ModGroup;
import net.armaments.item.component.ModDataComponents;
import net.armaments.item.custom.RevolverItem;
import net.armaments.item.custom.SniperItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;

public class ModItems {
    public static final RevolverItem REVOLVER = registerItem("revolver", new RevolverItem(new Item.Settings().maxDamage(250)));
    public static final SniperItem SNIPER_RIFLE = registerItem("cogwork_sniper", new SniperItem(new Item.Settings()));

    public static final Item REVOLVER_AMMO = registerItem("revolver_ammo", new Item(new Item.Settings()));
    public static final Item CREATIVE_AMMO_POUCH = registerItem("creative_ammo_pouch", new Item(new Item.Settings().maxCount(1).rarity(Rarity.EPIC))
    {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("creative_ammo_pouch_tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });

    private static <T extends Item> T registerItem(String name, T item){
        return Registry.register(Registries.ITEM, Identifier.of(Armaments.MOD_ID,name),item);
    }

    public static void registerModItems(){
        Armaments.LOGGER.info("Registering mod items for " + Armaments.MOD_ID);
        ModDataComponents.register();
        ModGroup.registerItemGroups();
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(REVOLVER);
        });
    }
}
