package net.armaments.api.world;

import net.armaments.Armaments;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class TickingObjects {
    public static final AttachmentType<List<TickingObject>> OBJECTS = AttachmentRegistry.createDefaulted(Armaments.id("ticking_objects"), ArrayList::new);

    public static void register() {
        ServerTickEvents.START_WORLD_TICK.register(world -> {
            Iterator<TickingObject> iterator = world.getAttachedOrCreate(OBJECTS).iterator();
            while (iterator.hasNext()) {
                TickingObject object = iterator.next();
                if (object.removed(world)) iterator.remove();
                else if (object.isActive(world)) object.baseTick(world);
            }
        });
    }
}
