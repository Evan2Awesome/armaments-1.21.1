package net.armaments;

import net.armaments.datagen.ModItemTagProvider;
import net.armaments.datagen.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ArmamentsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack  = fabricDataGenerator.createPack();
		//pack.addProvider(ExampleProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModItemTagProvider::new);
	}
}
