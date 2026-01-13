package net.armaments;

import net.armaments.item.ModItems;
import net.armaments.item.component.ModDataComponents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModGroup {
    public static final ItemGroup ARMAMENTS_ITEMS = Registry.register(Registries.ITEM_GROUP, Armaments.id("armaments_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.BULLET))
                    .displayName(Text.translatable("itemgroup.armaments.armaments_items"))
                    .entries((displayContext, entries) -> {
                        //add items here
                        //entries.add(ModItems.EXAMPLE);
                        ItemStack revolver = ModItems.REVOLVER.getDefaultStack(); revolver.set(ModDataComponents.AMMO, ModItems.REVOLVER.getMaxAmmo(revolver)); revolver.set(ModDataComponents.SELECTED_COMPONENT, true);
                        entries.add(ModItems.CREATIVE_AMMO_POUCH);
                        entries.add(ModItems.BULLET);
                        entries.add(revolver);
                        ItemStack sniper = ModItems.SNIPER_RIFLE.getDefaultStack(); sniper.set(ModDataComponents.AMMO, ModItems.SNIPER_RIFLE.getMaxAmmo(sniper)); sniper.set(ModDataComponents.SELECTED_COMPONENT, true);
                        entries.add(sniper);
                        ItemStack echo_gun = ModItems.ECHO_GUN.getDefaultStack(); echo_gun.set(ModDataComponents.AMMO, ModItems.ECHO_GUN.getMaxAmmo(sniper)); echo_gun.set(ModDataComponents.SELECTED_COMPONENT, true);
                        entries.add(echo_gun);
                    })
                    .build());


    public static void registerItemGroups(){
        Armaments.LOGGER.info("Registering item groups for " + Armaments.MOD_ID);
    }
}