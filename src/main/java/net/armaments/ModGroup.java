package net.armaments;

import net.armaments.block.ModBlocks;
import net.armaments.item.ModItems;
import net.armaments.item.component.ModDataComponents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
                        //important items
                        entries.add(ModBlocks.FIREARM_TABLE);
                        entries.add(ModItems.CREATIVE_AMMO_POUCH);
                        entries.add(ModItems.AMMO_POUCH);
                        entries.add(ModItems.LIGHT_BULLET);
                        entries.add(ModItems.BULLET);
                        entries.add(ModItems.HEAVY_BULLET);
                        //guns
                        ItemStack flintlock = ModItems.FLINTLOCK.getDefaultStack(); flintlock.set(ModDataComponents.AMMO, ModItems.FLINTLOCK.getMaxAmmo(flintlock)); flintlock.set(ModDataComponents.SELECTED_COMPONENT, true);
                        entries.add(flintlock);
                        ItemStack revolver = ModItems.REVOLVER.getDefaultStack(); revolver.set(ModDataComponents.AMMO, ModItems.REVOLVER.getMaxAmmo(revolver)); revolver.set(ModDataComponents.SELECTED_COMPONENT, true);
                        entries.add(revolver);
                        ItemStack bolt_action_rifle = ModItems.BOLT_ACTION_RIFLE.getDefaultStack(); bolt_action_rifle.set(ModDataComponents.AMMO, ModItems.BOLT_ACTION_RIFLE.getMaxAmmo(bolt_action_rifle)); bolt_action_rifle.set(ModDataComponents.SELECTED_COMPONENT, true);
                        entries.add(bolt_action_rifle);
                        ItemStack sniper = ModItems.SNIPER_RIFLE.getDefaultStack(); sniper.set(ModDataComponents.AMMO, ModItems.SNIPER_RIFLE.getMaxAmmo(sniper)); sniper.set(ModDataComponents.SELECTED_COMPONENT, true);
                        entries.add(sniper);
                        ItemStack echo_gun = ModItems.ECHO_GUN.getDefaultStack(); echo_gun.set(ModDataComponents.AMMO, ModItems.ECHO_GUN.getMaxAmmo(sniper)); echo_gun.set(ModDataComponents.SELECTED_COMPONENT, true);
                        entries.add(echo_gun);
                        ItemStack charge_gun = ModItems.CHARGE_GUN.getDefaultStack(); charge_gun.set(ModDataComponents.AMMO, ModItems.CHARGE_GUN.getMaxAmmo(charge_gun)); charge_gun.set(ModDataComponents.SELECTED_COMPONENT, true);
                        entries.add(charge_gun);
                        //misc
                        entries.add(ModItems.REVOLVING_CYLINDER);
                    })
                    .build());



    public static void registerItemGroups(){
        Armaments.LOGGER.info("Registering item groups for " + Armaments.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            ItemStack flintlock = ModItems.FLINTLOCK.getDefaultStack(); flintlock.set(ModDataComponents.AMMO, ModItems.FLINTLOCK.getMaxAmmo(flintlock)); flintlock.set(ModDataComponents.SELECTED_COMPONENT, true);
            entries.add(flintlock);
            ItemStack revolver = ModItems.REVOLVER.getDefaultStack(); revolver.set(ModDataComponents.AMMO, ModItems.REVOLVER.getMaxAmmo(revolver)); revolver.set(ModDataComponents.SELECTED_COMPONENT, true);
            entries.add(revolver);
            ItemStack bolt_action_rifle = ModItems.BOLT_ACTION_RIFLE.getDefaultStack(); bolt_action_rifle.set(ModDataComponents.AMMO, ModItems.BOLT_ACTION_RIFLE.getMaxAmmo(bolt_action_rifle)); bolt_action_rifle.set(ModDataComponents.SELECTED_COMPONENT, true);
            entries.add(bolt_action_rifle);
            ItemStack sniper = ModItems.SNIPER_RIFLE.getDefaultStack(); sniper.set(ModDataComponents.AMMO, ModItems.SNIPER_RIFLE.getMaxAmmo(sniper)); sniper.set(ModDataComponents.SELECTED_COMPONENT, true);
            entries.add(sniper);
            ItemStack echo_gun = ModItems.ECHO_GUN.getDefaultStack(); echo_gun.set(ModDataComponents.AMMO, ModItems.ECHO_GUN.getMaxAmmo(echo_gun)); echo_gun.set(ModDataComponents.SELECTED_COMPONENT, true);
            entries.add(echo_gun);
            ItemStack charge_gun = ModItems.CHARGE_GUN.getDefaultStack(); charge_gun.set(ModDataComponents.AMMO, ModItems.CHARGE_GUN.getMaxAmmo(charge_gun)); charge_gun.set(ModDataComponents.SELECTED_COMPONENT, true);
            entries.add(charge_gun);
            entries.add(ModItems.CREATIVE_AMMO_POUCH);
            entries.add(ModItems.AMMO_POUCH);
            entries.add(ModItems.LIGHT_BULLET);
            entries.add(ModItems.BULLET);
            entries.add(ModItems.HEAVY_BULLET);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.addAfter(Blocks.FLETCHING_TABLE, ModBlocks.FIREARM_TABLE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.HEAVY_CORE, ModItems.REVOLVING_CYLINDER);
        });
    }
}