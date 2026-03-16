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
        public static final TagKey<Item> ONE_HANDED_GUN = createTag("one_handed_gun"); //guns that should only be rendered as being held by one hand
        public static final TagKey<Item> TWO_HANDED_GUN = createTag("two_handed_gun"); //guns that should be rendered as being held in both hands
        public static final TagKey<Item> AMMO_ITEM = createTag("ammo_item"); //any item that can go into an ammo pouch

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Armaments.id(name));
        }
    }
}
