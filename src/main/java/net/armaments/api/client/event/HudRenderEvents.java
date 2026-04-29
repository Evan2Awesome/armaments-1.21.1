package net.armaments.api.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface HudRenderEvents {
    Event<ModifyCrosshairEvent> MODIFY_CROSSHAIR = EventFactory.createArrayBacked(ModifyCrosshairEvent.class, events -> (client, texture) -> {
        for (ModifyCrosshairEvent event : events) {
            Identifier newTexture = event.modify(client, texture);
            if (newTexture != null && !newTexture.equals(texture)) return newTexture;
        }
        return texture;
    });

    Event<RenderCrosshairAdditionEvent> RENDER_CROSSHAIR_ADDITION = EventFactory.createArrayBacked(RenderCrosshairAdditionEvent.class, events -> (client, context, texture, x, y, width, height) -> {
        for (RenderCrosshairAdditionEvent event : events) {
            event.render(client, context, texture, x, y, width, height);
        }
    });

    Event<ModifyAttackIndicatorEvent> MODIFY_ATTACK_INDICATOR = EventFactory.createArrayBacked(ModifyAttackIndicatorEvent.class, events -> (client, defaultPack, crosshairTexture) -> {
        for (ModifyAttackIndicatorEvent event : events) {
            AttackIndicatorPack pack = event.modify(client, defaultPack, crosshairTexture);
            if (pack != null && !pack.equals(defaultPack)) return pack;
        }
        return defaultPack;
    });

    @FunctionalInterface
    interface ModifyCrosshairEvent {
        @Nullable Identifier modify(MinecraftClient client, Identifier texture);
    }

    @FunctionalInterface
    interface RenderCrosshairAdditionEvent {
        void render(MinecraftClient client, DrawContext context, Identifier texture, int x, int y, int width, int height);
    }

    interface ModifyAttackIndicatorEvent {
        @Nullable AttackIndicatorPack modify(MinecraftClient client, AttackIndicatorPack defaultPack, Identifier crosshairTexture);
    }

    record AttackIndicatorPack(Identifier full, Identifier background, Identifier progress) {
        public static final AttackIndicatorPack DEFAULT = new AttackIndicatorPack(
                Identifier.ofVanilla("hud/crosshair_attack_indicator_full"),
                Identifier.ofVanilla("hud/crosshair_attack_indicator_background"),
                Identifier.ofVanilla("hud/crosshair_attack_indicator_progress")
        );

        @Override
        public boolean equals(Object obj) {
            return obj instanceof AttackIndicatorPack(Identifier full1, Identifier background1, Identifier progress1)
                    && full1.equals(this.full) && background1.equals(this.background) && progress1.equals(this.progress);
        }
    }
}
