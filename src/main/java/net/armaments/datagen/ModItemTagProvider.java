package net.armaments.datagen;

import net.armaments.item.ModItems;
import net.armaments.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
     public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
         //watch kaupenjoe videos 7, 10, and 11 to understand tags
        getOrCreateTagBuilder(ModTags.Items.ONE_HANDED_GUN)
                .add(ModItems.REVOLVER)
                .add(ModItems.ECHO_GUN)
                .add(ModItems.FLINTLOCK);

        getOrCreateTagBuilder(ModTags.Items.TWO_HANDED_GUN)
                .add(ModItems.SNIPER_RIFLE)
                .add(ModItems.CHARGE_GUN)
                .add(ModItems.BOLT_ACTION_RIFLE);

        getOrCreateTagBuilder(ModTags.Items.AMMO_ITEM)
                .add(ModItems.LIGHT_BULLET)
                .add(ModItems.BULLET)
                .add(ModItems.HEAVY_BULLET)
                .add(Items.IRON_NUGGET);
    }
}
