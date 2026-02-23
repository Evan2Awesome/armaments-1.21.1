package net.armaments.block;

import net.armaments.Armaments;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block FIREARM_TABLE = registerBlock("firearm_table",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN).instrument(NoteBlockInstrument.BASS).strength(2.5f).sounds(BlockSoundGroup.WOOD).burnable()
            ));


    private static Block registerBlock(String name, Block block){
        registerBlockItem(name,block);
        return Registry.register(Registries.BLOCK,Identifier.of(Armaments.MOD_ID,name), block);
    }

    private static  void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of(Armaments.MOD_ID, name),
            new BlockItem(block,new Item.Settings()));
    }

    public static void registerModBlocks(){
        Armaments.LOGGER.info("Registering mod blocks for " + Armaments.MOD_ID);
    }

}
