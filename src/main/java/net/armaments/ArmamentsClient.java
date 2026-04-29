package net.armaments;

import net.armaments.api.client.item.stack_holder.BundleLikeTooltip;
import net.armaments.client.*;
import net.armaments.item.component.AmmoPouchComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

public class ArmamentsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModModels.load();
        ModModelPredicates.register();
        ModAngles.register();
        ModAttackHandlers.register();
        ModGui.register();

        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof AmmoPouchComponent component) return new BundleLikeTooltip<>(component);
            else return null;
        });
    }
}
