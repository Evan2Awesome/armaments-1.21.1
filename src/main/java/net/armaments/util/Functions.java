package net.armaments.util;

import net.armaments.Armaments;
import net.minecraft.client.util.ModelIdentifier;

public class Functions {
    public static ModelIdentifier mId(String path) {
        return ModelIdentifier.ofInventoryVariant(Armaments.id(path));
    }
}
