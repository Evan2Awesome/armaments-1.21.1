package net.armaments.item;
import net.armaments.item.custom.PistolItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.armaments.Armaments;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModItems {
    //copy line below to add items
    //make sure to update en_us.json, textures/item/<item_name.png>, and add it to itemgroup
    //public static final Item EXAMPLE = registerItem("example", new Item(new Item.Settings()));
    public static final Item REVOLVER = registerItem("revolver", new PistolItem(new Item.Settings().maxCount(1)));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(Armaments.MOD_ID,name),item);
    }

    public static void registerModItems(){
        Armaments.LOGGER.info("Registering mod items for " + Armaments.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(fabricItemGroupEntries -> {
            //fabricItemGroupEntries.add(EXAMPLE);
            fabricItemGroupEntries.add(REVOLVER);
        });
    }
}
