package net.armaments;

import net.armaments.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModGroup {
    public static final ItemGroup ARMAMENTS_ITEMS = Registry.register(Registries.ITEM_GROUP, Armaments.id("armaments_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.REVOLVER))
                    .displayName(Text.translatable("itemgroup.armaments.armaments_items"))
                    .entries((displayContext, entries) -> {
                        //add items here
                        //entries.add(ModItems.EXAMPLE);
                        entries.add(ModItems.REVOLVER);
                        entries.add(ModItems.REVOLVER_AMMO);
                        entries.add(ModItems.CREATIVE_AMMO_POUCH);
                    })
                    .build());


    public static void registerItemGroups(){
        Armaments.LOGGER.info("Registering item groups for " + Armaments.MOD_ID);
    }
}