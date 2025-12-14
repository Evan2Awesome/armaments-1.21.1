package net.armaments.util;

import net.armaments.Armaments;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(Armaments.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> ONE_HANDED_GUN = createTag("one_handed_gun");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Armaments.id(name));
        }
    }
}
