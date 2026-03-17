package net.armaments.datagen;

import net.armaments.Armaments;
import net.armaments.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {
        AdvancementEntry getRevolver = Advancement.Builder.create()
                .display(
                        ModItems.REVOLVER,
                        Text.literal("It's... Gun Hat, right?"),
                        Text.literal("Obtain a revolver"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("got_revolver", InventoryChangedCriterion.Conditions.items(ModItems.REVOLVER))
                .build(consumer, Armaments.MOD_ID + "/get_revolver");
    }
}