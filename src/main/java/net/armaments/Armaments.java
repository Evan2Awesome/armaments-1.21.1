package net.armaments;

import net.armaments.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Armaments implements ModInitializer {
	public static final String MOD_ID = "armaments";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
	}

	public static Identifier id(String id) {
		return Identifier.of(MOD_ID, id);
	}
}