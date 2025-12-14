package net.armaments.item.component;

import com.mojang.serialization.Codec;
import net.armaments.Armaments;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final ComponentType<Boolean> SELECTED_COMPONENT = register("selected_component",
            builder -> builder.codec(Codec.BOOL));
    public static final ComponentType<Integer> AMMO_COMPONENT = register("ammo_component",
            integerBuilder -> integerBuilder.codec(Codec.INT));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Armaments.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void register() {
        Armaments.LOGGER.info("Registering Data Component Types for " + Armaments.MOD_ID);
    }
}
