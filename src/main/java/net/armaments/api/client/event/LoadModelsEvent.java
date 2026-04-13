package net.armaments.api.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

@FunctionalInterface
public interface LoadModelsEvent {
    Event<LoadModelsEvent> EVENT = EventFactory.createArrayBacked(LoadModelsEvent.class, events -> loader -> {
        for (LoadModelsEvent event : events) {
            event.loadModels(loader);
        }
    });

    void loadModels(Consumer<ModelIdentifier> loader);

    static ModelIdentifier model(Identifier modelId) {
        return ModelIdentifier.ofInventoryVariant(modelId);
    }
}
