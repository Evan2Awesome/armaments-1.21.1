package net.armaments;

import net.armaments.api.client.item.stack_holder.BundleLikeTooltip;
import net.armaments.client.ModAngles;
import net.armaments.client.ModAttackHandlers;
import net.armaments.client.ModModelPredicates;
import net.armaments.client.ModModels;
import net.armaments.item.component.AmmoPouchComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ArmamentsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModModels.load();
        ModModelPredicates.register();
        ModAngles.register();
        ModAttackHandlers.register();

        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof AmmoPouchComponent component) return new BundleLikeTooltip<>(component);
            else return null;
        });
    }

    public static void renderRelativeToCrosshair(DrawContext drawContext, int ammo, int maxAmmo, int x, int y, int color) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        if (client.player == null) return;

        Text text = Text.literal(ammo + "/" + maxAmmo);

        drawContext.drawText(
                textRenderer,
                text,
                x,
                y,
                color,
                true // shadow
        );
    }
}
