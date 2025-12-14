package net.armaments.item;

import net.armaments.Armaments;
import net.armaments.ModGroup;
import net.armaments.item.component.ModDataComponents;
import net.armaments.item.custom.PistolItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item REVOLVER = registerItem("revolver", new PistolItem(new Item.Settings()
            .maxCount(1)
            .maxDamage(250)));

    private static Item registerItem(String name, Item item){
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
