package net.armaments;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
//import net.armaments.block.ModBlocks;
import net.armaments.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModGroup {
    public static final ItemGroup IKEA_ITEMS = Registry.register(Registries.ITEM_GROUP, Identifier.of(Armaments.MOD_ID, "armaments_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.REVOLVER))
                    .displayName(Text.translatable("itemgroup.armaments.armaments_items"))
                    .entries((displayContext, entries) -> {
                        //add items here
                        //entries.add(ModItems.EXAMPLE);
                        entries.add(ModItems.REVOLVER);
                    })
                    .build());


    public static void registerItemGroups(){
        Armaments.LOGGER.info("Registering item groups for " + Armaments.MOD_ID);
    }
}
