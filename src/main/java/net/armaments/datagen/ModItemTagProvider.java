package net.armaments.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import net.armaments.item.ModItems;
import net.armaments.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
     public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        super(output, registryLookupFuture);
    }



    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
         //watch kaupenjoe videos 7, 10, and 11 to understand tags
        getOrCreateTagBuilder(ModTags.Items.ONE_HANDED_GUN)
                .add(ModItems.REVOLVER);

    }
}
