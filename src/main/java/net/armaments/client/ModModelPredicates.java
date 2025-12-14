package net.armaments.client;

import net.armaments.Armaments;
import net.armaments.item.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;

public class ModModelPredicates {
    public static void register() {
        ModelPredicateProviderRegistry.register(ModItems.REVOLVER, Armaments.id("spinning"), (stack, world, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getActiveItem().equals(stack) ? 1.0f : 0.0f);
    }
}
