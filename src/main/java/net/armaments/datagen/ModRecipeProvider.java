package net.armaments.datagen;

import net.armaments.Armaments;
import net.armaments.block.ModBlocks;
import net.armaments.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.FIREARM_TABLE)
                .pattern("gg")
                .pattern("pp")
                .input('p', ItemTags.PLANKS)
                .input('g', Items.GUNPOWDER)
                .criterion(hasItem(Items.GUNPOWDER), conditionsFromItem(Items.GUNPOWDER))
                .offerTo(recipeExporter, Identifier.of(Armaments.MOD_ID, "firearm_table"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, Items.BUNDLE)
                .pattern("s")
                .pattern("l")
                .input('s', Items.STRING)
                .input('l', Items.LEATHER)
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .offerTo(recipeExporter, Identifier.ofVanilla("bundle"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.AMMO_POUCH)
                .pattern("lil")
                .pattern("lbl")
                .pattern("lll")
                .input('b', Items.BUNDLE)
                .input('i', Items.IRON_INGOT)
                .input('l', Items.LEATHER)
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .offerTo(recipeExporter, Identifier.ofVanilla("ammo_pouch"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BULLET,8)
                .pattern(" i ")
                .pattern(" c ")
                .pattern(" g ")
                .input('c', Items.COPPER_INGOT)
                .input('i', Items.IRON_INGOT)
                .input('g', Items.GUNPOWDER)
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.GUNPOWDER), conditionsFromItem(Items.GUNPOWDER))
                .offerTo(recipeExporter, Identifier.of(Armaments.MOD_ID, "bullet"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.LIGHT_BULLET,12)
                .pattern(" i ")
                .pattern(" c ")
                .pattern(" g ")
                .input('c', Items.COPPER_INGOT)
                .input('i', Items.IRON_NUGGET)
                .input('g', Items.GUNPOWDER)
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion(hasItem(Items.IRON_NUGGET), conditionsFromItem(Items.IRON_NUGGET))
                .criterion(hasItem(Items.GUNPOWDER), conditionsFromItem(Items.GUNPOWDER))
                .offerTo(recipeExporter, Identifier.of(Armaments.MOD_ID, "light_bullet"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.HEAVY_BULLET,4)
                .pattern(" i ")
                .pattern(" c ")
                .pattern(" g ")
                .input('c', Items.GOLD_INGOT)
                .input('i', Items.IRON_INGOT)
                .input('g', Items.GUNPOWDER)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.GUNPOWDER), conditionsFromItem(Items.GUNPOWDER))
                .offerTo(recipeExporter, Identifier.of(Armaments.MOD_ID, "heavy_bullet"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.REVOLVER)
                .pattern("  i")
                .pattern(" i ")
                .pattern("np ")
                .input('i', Items.IRON_INGOT)
                .input('n', Items.IRON_NUGGET)
                .input('p', ItemTags.PLANKS)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.IRON_NUGGET), conditionsFromItem(Items.IRON_NUGGET))
                .offerTo(recipeExporter, Identifier.of(Armaments.MOD_ID, "revolver"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.SNIPER_RIFLE)
                .pattern("  i")
                .pattern("sg ")
                .pattern("gi ")
                .input('g', Items.GOLD_INGOT)
                .input('i', Items.IRON_INGOT)
                .input('s', Items.SPYGLASS)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.SPYGLASS), conditionsFromItem(Items.SPYGLASS))
                .offerTo(recipeExporter, Identifier.of(Armaments.MOD_ID, "cogwork_sniper"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ECHO_GUN)
                .pattern(" ie")
                .pattern(" ei")
                .pattern("di ")
                .input('i', Items.IRON_INGOT)
                .input('e', Items.ECHO_SHARD)
                .input('d', Items.DIAMOND)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.ECHO_SHARD), conditionsFromItem(Items.ECHO_SHARD))
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(recipeExporter, Identifier.of(Armaments.MOD_ID, "echo_gun"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.FLINTLOCK)
                .pattern("  i")
                .pattern(" in")
                .pattern("fp ")
                .input('i', Items.IRON_INGOT)
                .input('n', Items.IRON_NUGGET)
                .input('f', Items.FLINT)
                .input('p', ItemTags.PLANKS)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.IRON_NUGGET), conditionsFromItem(Items.IRON_NUGGET))
                .criterion(hasItem(Items.FLINT), conditionsFromItem(Items.FLINT))
                .offerTo(recipeExporter, Identifier.of(Armaments.MOD_ID, "flintlock"));
    }
}
