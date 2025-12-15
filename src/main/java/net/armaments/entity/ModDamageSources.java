package net.armaments.entity;

import net.armaments.Armaments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModDamageSources {
    public static final RegistryKey<DamageType> REVOLVER_SHOT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Armaments.id("revolver_shot"));
    private final DamageSources sources;

    public ModDamageSources(DamageSources sources) {
        this.sources = sources;
    }

    public DamageSource revolver(LivingEntity entity) {
        return this.sources.create(REVOLVER_SHOT, entity);
    }
}
