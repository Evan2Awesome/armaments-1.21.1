package net.armaments.client;

import net.armaments.Armaments;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent GUNSHOT = register("gunshot", 128);
    public static final SoundEvent ECHO_GUNSHOT = register("echo_gunshot", 16);

    public static SoundEvent register(String name, float range) {
        Identifier id = Armaments.id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {}
}
