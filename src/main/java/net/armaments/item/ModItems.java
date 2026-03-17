package net.armaments.item;

import net.armaments.Armaments;
import net.armaments.ModGroup;
import net.armaments.item.component.AmmoPouchComponent;
import net.armaments.item.component.ModDataComponents;
import net.armaments.item.custom.*;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
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
    public static final RevolverItem REVOLVER = registerItem("revolver", new RevolverItem(new Item.Settings().maxDamage(250)
            .attributeModifiers(new AttributeModifiersComponent(List.of(
                    new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(
                            Item.BASE_ATTACK_SPEED_MODIFIER_ID,0.5F, EntityAttributeModifier.Operation.ADD_VALUE
                    ), AttributeModifierSlot.MAINHAND),
                    new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(
                            Item.BASE_ATTACK_DAMAGE_MODIFIER_ID,2F, EntityAttributeModifier.Operation.ADD_VALUE
                    ), AttributeModifierSlot.MAINHAND)),true))));
    public static final SniperItem SNIPER_RIFLE = registerItem("cogwork_sniper", new SniperItem(new Item.Settings().maxDamage(160)
            .attributeModifiers(new AttributeModifiersComponent(List.of(
                    new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(
                            Item.BASE_ATTACK_SPEED_MODIFIER_ID,-2F, EntityAttributeModifier.Operation.ADD_VALUE
                    ), AttributeModifierSlot.MAINHAND),
                    new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(
                            Item.BASE_ATTACK_DAMAGE_MODIFIER_ID,2F, EntityAttributeModifier.Operation.ADD_VALUE
                    ), AttributeModifierSlot.MAINHAND)),true))));
    public static final EchoGunItem ECHO_GUN = registerItem("echo_gun", new EchoGunItem(new Item.Settings().maxDamage(320)
            .attributeModifiers(new AttributeModifiersComponent(List.of(
                    new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(
                            Item.BASE_ATTACK_SPEED_MODIFIER_ID,-1F, EntityAttributeModifier.Operation.ADD_VALUE
                    ), AttributeModifierSlot.MAINHAND),
                    new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(
                            Item.BASE_ATTACK_DAMAGE_MODIFIER_ID,2F, EntityAttributeModifier.Operation.ADD_VALUE
                    ), AttributeModifierSlot.MAINHAND)),true))));
    public static final ChargeGunItem CHARGE_GUN = registerItem("charge_gun", new ChargeGunItem(new Item.Settings().maxDamage(981)
            .attributeModifiers(new AttributeModifiersComponent(List.of(
                    new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(
                            Item.BASE_ATTACK_SPEED_MODIFIER_ID,2.4F, EntityAttributeModifier.Operation.ADD_VALUE
                    ), AttributeModifierSlot.MAINHAND),
                    new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(
                            Item.BASE_ATTACK_DAMAGE_MODIFIER_ID,2F, EntityAttributeModifier.Operation.ADD_VALUE
                    ), AttributeModifierSlot.MAINHAND)),true))));
    public static final FlintlockItem FLINTLOCK = registerItem("flintlock", new FlintlockItem(new Item.Settings().maxDamage(80)
            .attributeModifiers(new AttributeModifiersComponent(List.of(
                    new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(
                            Item.BASE_ATTACK_SPEED_MODIFIER_ID,-3F, EntityAttributeModifier.Operation.ADD_VALUE
                    ), AttributeModifierSlot.MAINHAND),
                    new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(
                            Item.BASE_ATTACK_DAMAGE_MODIFIER_ID,4F, EntityAttributeModifier.Operation.ADD_VALUE
                    ), AttributeModifierSlot.MAINHAND)),true))));


    public static final Item BULLET = registerItem("bullet", new Item(new Item.Settings().maxCount(32)));
    public static final Item LIGHT_BULLET = registerItem("light_bullet", new Item(new Item.Settings()));
    public static final Item HEAVY_BULLET = registerItem("heavy_bullet", new Item(new Item.Settings().maxCount(16)));
    public static final Item CREATIVE_AMMO_POUCH = registerItem("creative_ammo_pouch", new Item(new Item.Settings().maxCount(1).rarity(Rarity.EPIC))
    {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("creative_ammo_pouch_tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });

    public static final AmmoPouchItem AMMO_POUCH = registerItem("ammo_pouch", new AmmoPouchItem(new Item.Settings().maxCount(1).component(ModDataComponents.AMMO_POUCH, AmmoPouchComponent.DEFAULT)));

    public static final Item SCREEN_SHAKE_TEST = registerItem("screen_shake_test", new ScreenShakeTestItem(new Item.Settings()));

    private static <T extends Item> T registerItem(String name, T item){
        return Registry.register(Registries.ITEM, Identifier.of(Armaments.MOD_ID,name),item);
    }

    public static void registerModItems(){
        Armaments.LOGGER.info("Registering mod items for " + Armaments.MOD_ID);
        ModDataComponents.register();
        ModGroup.registerItemGroups();
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(BULLET);
        });
        AmmoPouchItem.registerTooltip();
    }
}
