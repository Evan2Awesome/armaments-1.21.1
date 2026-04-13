package net.armaments.client;

import net.armaments.Armaments;
import net.armaments.api.client.event.ChangeModelEvent;
import net.armaments.api.client.event.LoadModelsEvent;
import net.armaments.item.ModItems;
import net.armaments.item.custom.GunItem;
import net.minecraft.client.util.ModelIdentifier;

public final class ModModels {
    public static final ModelIdentifier REVOLVER_2D = LoadModelsEvent.model(Armaments.id("revolver_2d"));
    public static final ModelIdentifier COGWORK_SNIPER_2D = LoadModelsEvent.model(Armaments.id("cogwork_sniper_2d"));
    public static final ModelIdentifier ECHO_GUN_2D = LoadModelsEvent.model(Armaments.id("echo_gun_2d"));
    public static final ModelIdentifier CHARGE_GUN_2D = LoadModelsEvent.model(Armaments.id("charge_gun_2d"));
    public static final ModelIdentifier FLINTLOCK_2D = LoadModelsEvent.model(Armaments.id("flintlock_2d"));

    public static final ModelIdentifier REVOLVER_FP = LoadModelsEvent.model(Armaments.id("revolver_fp"));
    public static final ModelIdentifier FLINTLOCK_LOADED = LoadModelsEvent.model(Armaments.id("flintlock_loaded"));

    public static void load() {
        LoadModelsEvent.EVENT.register(loader -> {
            loader.accept(REVOLVER_2D);
            loader.accept(COGWORK_SNIPER_2D);
            loader.accept(ECHO_GUN_2D);
            loader.accept(CHARGE_GUN_2D);
            loader.accept(FLINTLOCK_2D);

            loader.accept(REVOLVER_FP);
            loader.accept(FLINTLOCK_LOADED);
        });

        ChangeModelEvent.EVENT.register((
                stack, renderMode, leftHanded,
                matrices, vertexConsumers,
                light, overlay, model, models
        ) -> {
            if (ChangeModelEvent.is2d(renderMode)) {
                if (stack.isOf(ModItems.REVOLVER)) return models.getModelManager().getModel(REVOLVER_2D);
                if (stack.isOf(ModItems.SNIPER_RIFLE)) return models.getModelManager().getModel(COGWORK_SNIPER_2D);
                if (stack.isOf(ModItems.ECHO_GUN)) return models.getModelManager().getModel(ECHO_GUN_2D);
                if (stack.isOf(ModItems.CHARGE_GUN)) return models.getModelManager().getModel(CHARGE_GUN_2D);
                if (stack.isOf(ModItems.FLINTLOCK)) return models.getModelManager().getModel(FLINTLOCK_2D);
            }

            if (stack.isOf(ModItems.FLINTLOCK) && stack.getItem() instanceof GunItem gunItem && gunItem.getAmmo(stack) > 0) {
                return models.getModelManager().getModel(FLINTLOCK_LOADED);
            }

            return null;
        });
    }
}
