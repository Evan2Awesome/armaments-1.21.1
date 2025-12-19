package net.armaments;

import net.armaments.client.ModAngles;
import net.armaments.client.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import org.apache.commons.lang3.function.TriConsumer;

import java.util.HashMap;
import java.util.Map;

public class ArmamentsClient implements ClientModInitializer {
    public static final Map<Item, TriConsumer<LivingEntity, BipedEntityModel<LivingEntity>, Float>> ANGLES_MAP = new HashMap<>();

    @Override
    public void onInitializeClient() {
        ModModelPredicates.register();
        ModAngles.register();
    }

    public static void addAngles(Item item, TriConsumer<LivingEntity, BipedEntityModel<LivingEntity>, Float> consumer) {
        ANGLES_MAP.put(item, consumer);
    }
}
