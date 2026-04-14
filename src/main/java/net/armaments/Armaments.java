package net.armaments;

import net.armaments.block.ModBlocks;
import net.armaments.client.ModSounds;
import net.armaments.item.ModItems;
import net.armaments.network.ModPlayPackets;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Armaments implements ModInitializer {
	public static final String MOD_ID = "armaments";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean DOOM = false;

	@Override
	public void onInitialize() {
		ModPlayPackets.register();
		ModItems.registerModItems();
		ModSounds.register();
		ModBlocks.registerModBlocks();
	}

	public static boolean isDoom() {return DOOM;}

	public static Identifier id(String id) {
		return Identifier.of(MOD_ID, id);
	}
}