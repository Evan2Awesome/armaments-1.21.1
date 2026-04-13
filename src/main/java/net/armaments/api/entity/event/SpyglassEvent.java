package net.armaments.api.entity.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface SpyglassEvent {
    Event<SpyglassEvent> EVENT = EventFactory.createArrayBacked(SpyglassEvent.class, events -> player -> {
        for (SpyglassEvent event : events) {
            if (event.usingSpyglass(player)) return true;
        }
        return false;
    });

    boolean usingSpyglass(PlayerEntity player);
}
